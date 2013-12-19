package de.rentoudu.mensa.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import de.rentoudu.mensa.MainActivity;
import de.rentoudu.mensa.R;
import de.rentoudu.mensa.Utils;
import de.rentoudu.mensa.model.Day;
import de.rentoudu.mensa.model.Diet;
import de.rentoudu.mensa.model.Menu;


public class DietFetchTask extends AsyncTask<String, Void, Diet> {

	//The MainActivity instance invoking this DietFetchTask
	private MainActivity activity;

	//The ProgressDialog shown when loading the RSS feed
	private ProgressDialog progressDialog;

	/**
	 * Creates a new DieFetchTask instance.
	 * @param mainActivity The MainActivity instance invoking this contructor.
	 */
	public DietFetchTask(MainActivity mainActivity) {
		this.activity = mainActivity;
	}

	@Override
	protected Diet doInBackground(String... args) {

		//Show the LoadingDialog
		startDietFetchNotification(); // Ends in onPostExecute(..)

		//Create Rss Cache instance
		RssCache rssCach = new RssCache();

		//Load Diet for first week
		Diet d1 = null, d2 = null;
		try {
			InputStream in = rssCach.fetchRssFeed(Integer.valueOf(args[0]), args[1], this.getDateOfNextSunday());
			d1 = parseRss(in, false);

		} catch(Exception e) {
			e.printStackTrace();
		}

		//Load diet for second week
		try {
			InputStream in = rssCach.fetchRssFeed(Integer.valueOf(args[0])+1, args[2], this.getDateOfNextSunday());
			d2 = parseRss(in, false);

		} catch(Exception e) {
			e.printStackTrace();
		}


		//Merge the two weeks' diets, if they are not null
		Diet mergedDiet = new Diet();
		mergedDiet.setLastSynced(new Date());
		
		if(d1 != null)
			mergedDiet.addDays(d1.getDays());
		
		if(d2 != null)
		mergedDiet.addDays(d2.getDays());

		//return the merged one
		return mergedDiet;
	}

	@Override
	protected void onPostExecute(Diet result) {

		//Check if the resut if valid
		if(result == null) {

			//if not, show an error toast and dismiss the loading dialog
			endDietFetchNotification(false);
		} else {

			//if so, update the shown diet and dismiss the loading dialog
			this.getActivity().updateDietAndView(result);
			endDietFetchNotification(true);
		}
	}

	/**
	 * Parses the given InputStream to a diet.
	 * @param in The Inputstream that should be parsed.
	 * @param isSecondWeek A flag, indicating if the given stream represents the first or the second week
	 * @return The parsed Diet object
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	protected Diet parseRss(InputStream in, boolean isSecondWeek) throws ParserConfigurationException, SAXException, IOException {
		//Set up Parser
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(in);

		//Get week and create new Diet
		int currentWeek = Utils.getWeek();
		Diet diet = new Diet();

		//create note list for <item>, every item node represents a day
		NodeList itemElements = document.getElementsByTagName("item");
		for(int i=0; i<itemElements.getLength(); i++) {
			//Get current Element (current day)
			Element itemElement = (Element) itemElements.item(i);

			//Get info
			String title = itemElement.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
			String guid = itemElement.getElementsByTagName("guid").item(0).getFirstChild().getNodeValue();
			String description = itemElement.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();

			//create day, set guid and title (never used?)
			Day d = this.parseDay(description);
			d.setGuid(guid);
			d.setTitle(title);

			//set day:  index equals Calender.DAYXX + 2 -> WHOOT? :D
			d.setDay(i + 2);

			//set week
			if(isSecondWeek) {
				d.setWeek(currentWeek + 1);
			} else {
				d.setWeek(currentWeek);
			}

			//add day to diet
			diet.addDay(d);

		}

		//set last sync date and return
		diet.setLastSynced(new Date());
		return diet;
	}

	/**
	 * Parses a single day using the given description of the day's menus.
	 * @param description The description of the day's menus
	 * @return
	 */
	private Day parseDay(String description) {

		//create day
		Day d = new Day();

		// Clear the CDATA and use delimiter 
		description = description.replace("<![CDATA[", "");
		description = description.replace("]]>", "");

		//Split description in seperate rows
		String parts[] = description.split("<br>");

		//Interprete description, cancel if no <u> tag is available
		for(int j=0; j<parts.length; j++) {

			//Part is start of a new category, e.g. Men� 1
			if(this.optimizeString(parts[j]).contains("<u>")) {

				//create menu
				Menu m = new Menu();

				//category title, e.g. "Men� 1"
				m.setTitle(this.optimizeString(parts[j]).replaceAll("<u>", "").replaceAll("</u>", ""));

				//if category title is the notes title -> save next part as notes and  continue;
				if(m.getTitle().equals("Kennzeichnung")) {
					d.setNotes(this.optimizeString(parts[++j]));
					continue;
				}

				//if the next two items do not contain a new category, use them as apetizer and mai course
				if(!parts[j+1].contains("<u>") && !parts[j+2].contains("<u>")) {
					//get apetizer
					m.setAppetizer(this.optimizeString(parts[++j]));
					//get apetizer
					m.setMainCourse(this.optimizeString(parts[++j]));
				
				} else {
					
					//if not the first item contains <u> (a new category) set it as mainCourse and add the menu
					if(!parts[j+1].contains("<u>")) {
						m.setMainCourse(this.optimizeString(parts[++j]));
						d.addMenu(m);
						continue;
					
					//Both contain a new category, skip it
					} else {
						continue;
					}
				}

				
		
				//get side dishes
				StringBuilder sideDishes = new StringBuilder();
				while(true) {

					//Check if next part is next title
					if(++j > parts.length || parts[j].contains("<u>")) {
						j--;
						break;
					}

					//Append part to sideDish
					String s = this.optimizeString(parts[j]);

					if(s.length() > 0)
						sideDishes.append(s + ", ");
				}

				//remove last comma and set
				if(sideDishes.toString().endsWith(", "))
					sideDishes.delete(sideDishes.length()-2, sideDishes.length()-1);
				m.setSideDish(sideDishes.toString());

				//add Menu to day
				d.addMenu(m);
			}
		}

		return d;
	}

	/**
	 * Optimizes the given String by deleting unrelevant characters or replacing them by better ones.
	 * @param s The String object that is going to be optimized
	 * @return
	 */
	private String optimizeString(String s) {
		s = s.replace("\n", "").replace("\r", "").replace("\t", "");
		s = s.replaceAll("<u></u>", "");
		s = s.trim().replaceAll(" +", " ");
		s = s.replaceAll(">\\s+<", "><").trim();
		s = s.replace("<br>", "");
		s = s.replaceAll("(;)\\1+", "");
		s = s.replaceAll("<b>", "");
		s = s.replaceAll("</b>", "");

		if(s.startsWith(" "))
			s = s.substring(1);

		return s;
	}

	private Date getDateOfNextSunday() {
		Calendar calendar = Calendar.getInstance();  
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);  

		if(weekday != Calendar.SUNDAY || (weekday == Calendar.SATURDAY && calendar.get(Calendar.HOUR_OF_DAY) > 20)) {

			int days = Calendar.SUNDAY - weekday;  
			if (days < 0)  {  
				// this will usually be the case since Calendar.SUNDAY is the smallest  
				days += 7;  
			}  
			calendar.add(Calendar.DAY_OF_YEAR, days); 
		}

		calendar.set(Calendar.AM_PM, Calendar.PM);
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date d = calendar.getTime();

		return calendar.getTime();
	}

	/**
	 * Calls the MainActivity instance to show a loading dialog.
	 * @see #endDietFetchNotification(boolean)
	 */
	private void startDietFetchNotification() {

		//Call UI Thead to show indeterminate ProgressDialog
		this.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				DietFetchTask.this.progressDialog = ProgressDialog.show(DietFetchTask.this.getActivity(), "", 
						DietFetchTask.this.getActivity().getResources().getString(R.string.text_diet_fetch), true);
			}
		});

	}

	/**
	 * Dismisses the dialog which is created in {@link #startDietFetchNotification()} and shows a 
	 * error Toast if the action was not successfull.
	 * @param success The flag indicating if the fetch action was successfull.
	 * @see #startDietFetchNotification()
	 */
	private void endDietFetchNotification(boolean success) {

		//Show error toast if an error has occured
		if(!success)
			this.getActivity().showToast(this.getActivity().getResources().getString(R.string.error_diet_fetch));

		//if the progress dialog is null, cancel operation
		if(this.progressDialog == null)
			return;

		//Call UI Thread to dismiss progressDialog
		this.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				DietFetchTask.this.progressDialog.dismiss();
			}
		});
	}

	/**
	 * Return the MainActivity instance which has invoked this DietFetchTask.
	 * @return The MainActivity instance
	 */
	public MainActivity getActivity() {
		return activity;
	}
}