package de.rentoudu.mensa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.rentoudu.mensa.model.Diet;
import de.rentoudu.mensa.task.DietFetchTask;

/**
 * The main activity.
 * 
 * @author Florian Sauter
 */
public class MainActivity extends FragmentActivity {

	private static final String FEED_URL = "http://www.swfr.de/essen-trinken/speiseplaene/speiseplan-rss/?no_cache=1&Tag={day}&Ort_ID=641";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections.
	 */
	private DayPagerAdapter dayPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager viewPager;

	/**
	 * The current activity diet.
	 */
	private Diet currentDiet;
	
	/**
	 * Opening time in german format
	 */
	public static int[] openingTime = {11, 25};
	
	/**
	 * Closing time in german format
	 */
	public static int[] closingTime = {13, 40};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		dayPagerAdapter = new DayPagerAdapter(getResources(), getSupportFragmentManager());
		
		if(savedInstanceState != null && savedInstanceState.containsKey("diet")) {
			// Reuse already fetched diet.
			Diet savedDiet = (Diet) savedInstanceState.getSerializable("diet");
			updateDietAndView(savedDiet);
		} else {
			// Starts async fetching task.
			refreshDiet();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Remember the fetched diet for restoring in #onCreate.
		outState.putSerializable("diet", currentDiet);
	}

	/**
	 * Returns the current day index (using closing hours).
	 */
	protected int getCurrentCanteenDayIndex() {
		// by default -2 because an we want to start counting with 0
		int dateBalancer = -2;
		// Have a look at the closing hours
		if (Utils.isAfter(closingTime[0], closingTime[1])) {
			dateBalancer = -1;
		}
		return Utils.getDay() + dateBalancer;
	}
	
	/**
	 * Returns the current day index.
	 */
	protected int getCurrentDayIndex() {
		// by default -2 because an we want to start counting with 0
		int dateBalancer = -2;
		return Utils.getDay() + dateBalancer;
	}
	
	public void refreshDiet() {
		String firstWeekFeedUrl = buildFeedUrl(-getCurrentCanteenDayIndex());
		String secondWeekFeedUrl = buildFeedUrl(-getCurrentCanteenDayIndex() + 7);
		new DietFetchTask(this).execute(firstWeekFeedUrl, secondWeekFeedUrl);
	}
	
	public void updateDietAndView(Diet diet) {
		this.currentDiet = diet;
		dayPagerAdapter.setDiet(diet);
		viewPager.setAdapter(dayPagerAdapter);
		viewPager.setCurrentItem(getCurrentCanteenDayIndex());
	}

	/**
	 * Builds the feed URL to fetch based on the current weekday.
	 */
	protected String buildFeedUrl(int startDay) {
		return FEED_URL.replace("{day}", String.valueOf(startDay));
	}

	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_today:
				goToToday();
				break;
			case R.id.menu_sync:
				refreshDiet();
				break;
			case R.id.menu_about:
				showAboutMenu();
				break;
		}
		return true;
	}

	private void goToToday() {
		viewPager.setCurrentItem(getCurrentDayIndex());
	}

	private void showAboutMenu() {
		String version = "-";
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// Nothing to do.
		}
		String message = String.format(getString(R.string.text_about), version);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle(R.string.menu_about)
			.setNeutralButton(android.R.string.ok,
	            new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}
			).create().show();
	}
	
	public DayPagerAdapter getDayPagerAdapter() {
		return dayPagerAdapter;
	}
	
	public ViewPager getViewPager() {
		return viewPager;
	}
	
	/**
	 * Shows a toast notification.
	 */
	public void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * Prints a log message (mainly for LogCat)
	 */
	protected void log(String message) {
		// Log.v(getClass().getSimpleName(), message);
	}
}
