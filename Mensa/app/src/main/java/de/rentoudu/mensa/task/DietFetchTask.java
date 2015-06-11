package de.rentoudu.mensa.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import de.hfu.mensa.R;
import de.rentoudu.mensa.MainActivity;
import de.rentoudu.mensa.model.Day;
import de.rentoudu.mensa.model.Diet;
import de.rentoudu.mensa.model.Mensa;
import de.rentoudu.mensa.model.Menu;

public class DietFetchTask extends AsyncTask<Mensa, Void, Diet> {

	//The MainActivity instance invoking this DietFetchTask
	private MainActivity activity;

	//The ProgressDialog shown when loading the RSS feed
	private ProgressDialog progressDialog;

	//A flag indicating if cached values should be used
	private boolean useCachedValues = true;

	//A attribute to store (the last) occured exception
	private Exception occuredException = null;

	/**
	 * Creates a new DieFetchTask instance.
	 * @param mainActivity The MainActivity instance invoking this contructor.
	 */
	public DietFetchTask(MainActivity mainActivity, boolean useCachedValues) {
		this.activity = mainActivity;
		this.useCachedValues = useCachedValues;

	}

	@Override
	protected Diet doInBackground(Mensa... args) {
		//Wrap everything into try-catch to perfectly handle exceptions
		try {

            //Fetch menu URL
            String url = args[0].getMenuUrl();

            //Pause 250ms to let the animations finish
            Thread.sleep(250);

			//Show the LoadingDialog
			startDietFetchNotification(); // Ends in onPostExecute(..)

			//Create Rss Cache instance
			UrlCache rssCach = UrlCache.getCache();

			//Load Diet for first week  (in seperate try-catch to load as much data as possible)
            Diet mergedDiet = new Diet();
            String urlQuery = "";
			try {
                while(urlQuery != null) {
                    InputStream in = rssCach.fetchUrl(url + urlQuery, this.getDateOfNextSunday(),  this.useCachedValues);
                    Diet d = parseMenu(in);
                    mergedDiet.addDays(d.getDays());
                    urlQuery = d.getUrlQueryNextWeek();

                }
			} catch(Exception e) {
				e.printStackTrace();
				this.occuredException = e;

			}

			//Set sync date
			mergedDiet.setLastSynced(new Date());

			//return the merged one
			return mergedDiet;

		} catch(Exception e) {
			//Something went wrong...save exception and return null
			e.printStackTrace();
			this.occuredException = e;
			return null;

		}
	}

	@Override
	protected void onPostExecute(Diet result) {

		//Check if the resut if valid
		if(result == null || this.occuredException != null) {
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
	 * @return The parsed Diet object
	 * @throws javax.xml.parsers.ParserConfigurationException
	 * @throws org.xml.sax.SAXException
	 * @throws java.io.IOException
	 */
	protected Diet parseMenu(InputStream in) throws Exception {
        //Read input stream
        Log.i(this.getClass().getSimpleName(), "Start reading...");
        StringBuilder html = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream((in))));
        String line;
        while((line = br.readLine()) != null) {
            html.append(line);
        }
        Log.i(this.getClass().getSimpleName(), "Reading done.");


        in.close();

        //Create Jsoup document
        Log.i(this.getClass().getSimpleName(), "Start parsing...");
        Document doc = Jsoup.parse(html.toString());
        Log.i(this.getClass().getSimpleName(), "Parsing done");

        // Create empty Diet
        Diet diet = new Diet();

        //Get diet
        Element dietElement = doc.getElementById("speiseplan-tabs");

        //Get notes
        String notes = doc.getElementsByClass("menu-zusatzstoffe").get(0).html();

        //Parse every day
        diet.addDay(this.parseDay(dietElement.getElementById("tab-mon"), notes));
        diet.addDay(this.parseDay(dietElement.getElementById("tab-tue"), notes));
        diet.addDay(this.parseDay(dietElement.getElementById("tab-wed"), notes));
        diet.addDay(this.parseDay(dietElement.getElementById("tab-thu"), notes));
        diet.addDay(this.parseDay(dietElement.getElementById("tab-fri"), notes));
        diet.addDay(this.parseDay(dietElement.getElementById("tab-sat"), notes));

        //Get link to next week (if there is one)
        Elements linkNextWeek = doc.getElementsByClass("next-week");
        if(linkNextWeek.size() > 0) {
            diet.setUrlQueryNextWeek(URI.create(linkNextWeek.get(0).attr("href")).getQuery());
        }

        return diet;
    }

	/**
	 * Parses a single day using the given description of the day's menus.
	 * @param tab The day's tab element
	 * @return
	 */
	private Day parseDay(Element tab, String notes) {
        //Create empty day
        Day day = new Day();
        day.setNotes(notes);
        day.setGuid(tab.getElementsByTag("h3").get(0).text());

        //Get menu-header and menu-info divs
        Elements header = tab.getElementsByClass("menu-header");
        Elements info = tab.getElementsByClass("menu-info");

        //Iterate over all available menus
        for(int i=0; i<header.size(); i++) {
            //Get html code
            String infoText = info.get(i).html();

            //Get title
            String titleText = header.get(i).getElementsByTag("h4").get(0).text();

            //Get type
            String type = info.attr("class").substring("menu-info ".length());

            //Create menu
            Menu menu = new Menu(titleText, infoText, type);

            //Add to day
            day.addMenu(menu);

        }

        return day;

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

		//		Date d = calendar.getTime();

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