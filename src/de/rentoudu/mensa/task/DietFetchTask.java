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

	private NotificationCompat.Builder notificationBuilder;
	private NotificationManager notificationManager;
	private MainActivity activity;
	
	public DietFetchTask(MainActivity mainActivity) {
		this.activity = mainActivity;
		this.notificationBuilder = new NotificationCompat.Builder(activity);
		this.notificationManager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);
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
			return null;
		} catch (ParserConfigurationException e) {
			return null;
		} catch (SAXException e) {
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
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(in);
		
		int currentWeek = Utils.getWeek();
		
		Diet diet = new Diet();
		
		NodeList itemElements = document.getElementsByTagName("item");
		for (int i = 0; i < 5; i++) { // NO WEEKEND DAYS (SATURDAY)
			Element itemElement = (Element) itemElements.item(i);
			String title = itemElement.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
			String guid = itemElement.getElementsByTagName("guid").item(0).getFirstChild().getNodeValue();
			String description = itemElement.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();

			// Clear the CDATA and use delimiter ;
			description = description.replace("\n", "").replace("\r", "").replace("\t", "");
	        description = description.replaceAll("<u></u>", "");
			description = description.trim().replaceAll(" +", " ");
			description = description.replaceAll(">\\s+<", "><").trim();
			description = description.replace("<br>", ";");
			description = description.replaceAll("(;)\\1+", "");
			description = description.replaceAll("<b>", "");
			description = description.replaceAll("</b>", "");
			
			// Fetch the menu strings
			int indexOfMenuTwo = description.indexOf("<u>Men", 1);
			int indexOfNotes = description.indexOf("<u>Kennzeichnung");
			
			String menuOneString = description.substring(0, indexOfMenuTwo);
			String menuTwoString = description.substring(indexOfMenuTwo, indexOfNotes);
			String notes = description.substring(indexOfNotes).replace("<u>Kennzeichnung</u>;", "");
			
			// Parse the menu strings
			Menu menuOne = parseMenu(menuOneString);
			Menu menuTwo = parseMenu(menuTwoString);
			
			menuOne.setTitle(getActivity().getString(R.string.text_header_menu_one));
			menuTwo.setTitle(getActivity().getString(R.string.text_header_menu_two));

			Day day = new Day();
			if(isSecondWeek) {
				day.setWeek(currentWeek + 1);
			} else {
				day.setWeek(currentWeek);
			}
			day.setDay(i + 2); // index equals Calender.DAYXX + 2
			
			// No main course.. no menu!
			if(menuOne.getMainCourse() != "") 
				day.addMenu(menuOne);
		
			if(menuTwo.getMainCourse() != "") 
				day.addMenu(menuTwo);
			
			day.setNotes(notes);
			day.setTitle(title);
			day.setGuid(guid);

			diet.addDay(day);
		}

		diet.setLastSynced(new Date());
		return diet;
	}
	
	protected Menu parseMenu(String menuString) {
		String[] menuItems = menuString.split(";");
		
		Menu menu = new Menu();
		menu.setAppetizer("");
		menu.setMainCourse("");
		menu.setSideDish("");
		
		if(menuItems.length > 2) { // Make sure, there is a parsable value.
		
			String appetizer = menuItems[1].trim();
			String mainCourse = menuItems[2].trim();
			String sideDish = "";
			for(int i = 3; i < menuItems.length; i++) {
				sideDish = sideDish.concat(", " + menuItems[i]);
			}
			sideDish = sideDish.substring(1).trim();
			
			menu.setAppetizer(appetizer);
			menu.setMainCourse(mainCourse);
			menu.setSideDish(sideDish);
		
		}
		
		return menu;
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
		Intent intent = new Intent(getActivity(), MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
		notificationBuilder.setContentIntent(pIntent);
		notificationBuilder.setContentTitle(getActivity().getString(R.string.notification_updating_menu_title))
				.setContentText(getActivity().getString(R.string.notification_updating_menu_text))
				.setSmallIcon(R.drawable.ic_action_refresh_dark)
				.setProgress(0, 0, true);
		notificationManager.notify(0, notificationBuilder.build());
	}
	
	private void endDietFetchNotification(boolean success) {
		notificationManager.cancel(0);
		if(success) {
			getActivity().showToast(getActivity().getString(R.string.text_diet_synced));
		} else {
			getActivity().showToast(getActivity().getString(R.string.error_diet_fetch));
		}
	}
	
	public MainActivity getActivity() {
		return activity;
	}
}