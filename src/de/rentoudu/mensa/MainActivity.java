package de.rentoudu.mensa;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.*;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.rentoudu.mensa.model.Diet;
import de.rentoudu.mensa.model.Mensa;
import de.rentoudu.mensa.model.MensaDatabase;
import de.rentoudu.mensa.task.DietFetchTask;

/**
 * The main activity.
 * 
 * @author Florian Sauter
 */
public class MainActivity extends FragmentActivity implements OnItemClickListener {

	private final String FEED_URL = "http://www.swfr.de/essen-trinken/speiseplaene/speiseplan-rss/?no_cache=1&Tag={day}&Ort_ID={id}";

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
	 * The selected mensa's id
	 */
	private Mensa selectedMensa = null;

	/**
	 * Opening time in german format
	 */
	public static int[] openingTime = {11, 25};

	/**
	 * Closing time in german format
	 */
	public static int[] closingTime = {13, 40};

	/**
	 * DrawerLayout stuff
	 */
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//create pagers
		viewPager = (ViewPager) findViewById(R.id.pager);
		dayPagerAdapter = new DayPagerAdapter(getResources(), getSupportFragmentManager());

		//set selected canteen. TODO: Save in Bundle and reuse
		this.selectedMensa = MensaDatabase.createMensaDatabase().getMensaForId(641);
		this.getActionBar().setTitle(MainActivity.this.selectedMensa.getName());

		//load saved instances or reload from feed
		if(savedInstanceState != null && savedInstanceState.containsKey("diet")) {
			// Reuse already fetched diet.
			Diet savedDiet = (Diet) savedInstanceState.getSerializable("diet");
			updateDietAndView(savedDiet);
		} else {
			// Starts async fetching task.
			refreshDiet();
		}

		//Create drawer amnd its list
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		//create toggle switch to replace the back button in the activity's actionbar
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.app_name  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(MainActivity.this.selectedMensa.getName());
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(R.string.drawer_open);
			}
		};

		// Set the adapter for the list view
		mDrawerList.setAdapter(new NavigationDrawerListAdapter(this, 
				MensaDatabase.createMensaDatabase().getMensaListArray()));

		// Set the list's click listener
		mDrawerList.setOnItemClickListener(this);

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//Set the correct settings for the actionbar to corespond with the ActionBarDrawerToggle
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Remember the fetched diet for restoring in #onCreate.
		outState.putSerializable("diet", currentDiet);
	}

	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		//this.mDrawerLayout.openDrawer(Gravity.LEFT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		//hide the drawer
		this.mDrawerLayout.closeDrawers();

		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle your other action bar items...
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		//A new canteen was selected in the drawer's list. Get the selected one
		Mensa m = ((NavigationDrawerListAdapter)this.mDrawerList.getAdapter()).getItem(position);//MensaDatabase.createMensaDatabase().getMensaAtPosition(position);

		//If the new is not the old, set the new and reload the diet
		if(m != null && m!= this.selectedMensa) {
			this.selectedMensa = m;
			this.refreshDiet();
		} 
		
		//close drawer
		this.mDrawerLayout.closeDrawers();

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
		return FEED_URL
				.replace("{day}", String.valueOf(startDay))
				.replace("{id}", String.valueOf(this.selectedMensa.getId()));
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
