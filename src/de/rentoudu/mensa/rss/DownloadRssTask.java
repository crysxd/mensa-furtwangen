package de.rentoudu.mensa.rss;

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

import android.os.AsyncTask;
import de.rentoudu.mensa.Utils;
import de.rentoudu.mensa.model.Day;
import de.rentoudu.mensa.model.Diet;
import de.rentoudu.mensa.model.Menu;

public class DownloadRssTask extends AsyncTask<String, Void, Diet> {

	@Override
	protected Diet doInBackground(String... urls) {
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

	protected Diet parseRss(InputStream in, boolean isSecondWeek) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(in);
		
		int currentWeek = Utils.getWeek();
		
		Diet diet = new Diet();
		
		NodeList itemElements = document.getElementsByTagName("item");
		for (int i = 0; i < 5; i++) { // NO WEEKEND DAYS (SATURDAY)
			Element itemElement = (Element) itemElements.item(i);
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

			Day day = new Day();
			if(isSecondWeek) {
				day.setWeek(currentWeek + 1);
			} else {
				day.setWeek(currentWeek);
			}
			day.setDay(i + 2); // index equals Calender.DAYXX + 2
			day.setMenuOne(menuOne);
			day.setMenuTwo(menuTwo);
			day.setNotes(notes);

			diet.addDay(day);
		}

		diet.setLastSynced(new Date());
		return diet;
	}
	
	protected Menu parseMenu(String menuString) {
		String[] menuItems = menuString.split(";");
		
		String appetizer = menuItems[1].trim();
		String mainCourse = menuItems[2].trim();
		String sideDish = "";
		for(int i = 3; i < menuItems.length; i++) {
			sideDish = sideDish.concat(", " + menuItems[i]);
		}
		sideDish = sideDish.substring(1).trim();
		
		Menu menu = new Menu();
		menu.setAppetizer(appetizer);
		menu.setMainCourse(mainCourse);
		menu.setSideDish(sideDish);
		
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
}