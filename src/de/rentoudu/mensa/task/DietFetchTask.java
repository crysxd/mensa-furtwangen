package de.rentoudu.mensa.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import de.rentoudu.mensa.MainActivity;
import de.rentoudu.mensa.R;
import de.rentoudu.mensa.Utils;
import de.rentoudu.mensa.model.Day;
import de.rentoudu.mensa.model.Diet;
import de.rentoudu.mensa.model.Menu;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 */
public class DietFetchTask extends AsyncTask<String, Void, Diet> {

	private MainActivity activity;
	private ProgressDialog progressDialog;
	
	public DietFetchTask(MainActivity mainActivity) {
		this.activity = mainActivity;
	}

	@Override
	protected Diet doInBackground(String... urls) {

		startDietFetchNotification(); // Ends in onPostExecute(..)

		try {
			InputStream firstWeekStream = fetchRss(urls[0]);
			InputStream secondWeekStream = fetchRss(urls[1]);
			Diet firstWeek = parseRss(firstWeekStream, false);
			Diet secondWeek = parseRss(secondWeekStream, true);

			Diet mergedDiet = new Diet();
			mergedDiet.setLastSynced(new Date());
			mergedDiet.addDays(firstWeek.getDays());
			mergedDiet.addDays(secondWeek.getDays());

			return mergedDiet;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

			return null;
		} catch (SAXException e) {
			e.printStackTrace();

			return null;
			
		}
	}

	@Override
	protected void onPostExecute(Diet result) {
		if(result == null) {
			endDietFetchNotification(false);
		} else {
			getActivity().updateDietAndView(result);
			endDietFetchNotification(true);
		}
	}

	protected Diet parseRss(InputStream in, boolean isSecondWeek) throws ParserConfigurationException, SAXException, IOException {
		//Set up Parser
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(in);

		//Get week and create new Diet
		int currentWeek = Utils.getWeek();
		Diet diet = new Diet();

		//create note list for <item>
		NodeList itemElements = document.getElementsByTagName("item");
		for(int i=0; i<itemElements.getLength(); i++) {
			//Get current Element
			Element itemElement = (Element) itemElements.item(i);

			//Get info
			String title = itemElement.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
			String guid = itemElement.getElementsByTagName("guid").item(0).getFirstChild().getNodeValue();
			String description = itemElement.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();

			//create day
			Day d = this.parseDay(title, guid, description);
			
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

		diet.setLastSynced(new Date());
		return diet;
	}

	private Day parseDay(String title, String guid, String description) {

		//create day
		Day d = new Day();
		
		// Clear the CDATA and use delimiter 
		description = description.replace("<![CDATA[", "");
		description = description.replace("]]>", "");

		//Split description in seperate rows
		String parts[] = description.split("<br>");

		//Interprete description, cancel if no <u> tag is available
		for(int j=0; j<parts.length; j++) {

			//Part is start of a new category, e.g. Menü 1
			if(this.optimizeString(parts[j]).contains("<u>")) {

				//create menu
				Menu m = new Menu();

				//category title, e.g. "Menü 1"
				m.setTitle(this.optimizeString(parts[j]).replaceAll("<u>", "").replaceAll("</u>", ""));
				
				//if category title is the notes title -> save next part as notes and  continue;
				if(m.getTitle().equals("Kennzeichnung")) {
					d.setNotes(this.optimizeString(parts[++j]));
					continue;
				}
				
				//get apetizer
				m.setAppetizer(this.optimizeString(parts[++j]));
				//get apetizer
				m.setMainCourse(this.optimizeString(parts[++j]));
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


	// Given a string representation of a URL, sets up a connection and gets
	// an input stream.
	protected InputStream fetchRss(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}

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

	public MainActivity getActivity() {
		return activity;
	}
}