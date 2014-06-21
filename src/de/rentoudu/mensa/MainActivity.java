package de.rentoudu.mensa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

	private static final String FEED_URL = "http://www.swfr.de/essen-trinken/speiseplaene/speiseplan-rss/?no_cache=1&Tag={day}&Ort_ID={id}";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections.
	 */
	private DayPagerAdapter dayPagerAdapter; 

	/**
	 * The name of the SharedPreferences instance used by this activity
	 */
	private final String SHARED_PREFS_NAME = "MainActivitySettings";

	/**
	 * The name of the setting in which the selected Mensa's ID is stored
	 */
	private final String SELECTED_MENSA_SETTING = "selectedMensa";

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

		viewPager = (ViewPager) findViewById(R.id.pager);
		dayPagerAdapter = new DayPagerAdapter(getResources(), getSupportFragmentManager());


		//Load the selected Mensa's ID  from the SharedPreferences object or load Furtwangen (id=641) 
		//as the selected one ans put the selctedMensa object
		SharedPreferences settings = getSharedPreferences(this.SHARED_PREFS_NAME, 0);
		int selectedMensaId = settings.getInt(this.SELECTED_MENSA_SETTING, 641);
		this.selectedMensa = MensaDatabase.createMensaDatabase().getMensaForId(selectedMensaId);

		//Update the ActionBar's title to correspont with the selected Mensa
		this.getActionBar().setTitle(this.selectedMensa.getName());
		this.getActionBar().setIcon(this.getResources().getDrawable(this.selectedMensa.getIconResource()));

		if(savedInstanceState != null && savedInstanceState.containsKey("diet")) {
			// Reuse already fetched diet.
			Diet savedDiet = (Diet) savedInstanceState.getSerializable("diet");
			updateDietAndView(savedDiet);
		} else {
			// Starts async fetching task.
			refreshDiet();
		}

		//create the left drawer and it's list
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list);

		//create the drawer toggle which will be put in the left side of the ActionBar
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.app_name  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				MainActivity.this.getActionBar().setTitle(MainActivity.this.selectedMensa.getName());
				MainActivity.this.getActionBar().setIcon(MainActivity.this.getResources().getDrawable(
						MainActivity.this.selectedMensa.getIconResource()));

			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(R.string.drawer_open);
			}
		};

		// Set the adapter for the list view
		MensaDatabase db = MensaDatabase.createMensaDatabase();
		Mensa[] mensen = db.getMensaListArray();
		MensaArrayAdapter a = new MensaArrayAdapter(this, mensen);
		mDrawerList.setAdapter(a);

		// Set the list's click listener
		mDrawerList.setOnItemClickListener(this);

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		//Setup the ActionBar for the DrawerToggle
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

		//Generate RSS feed URLs
		String firstWeekFeedUrl = buildFeedUrl(-getCurrentCanteenDayIndex());
		String secondWeekFeedUrl = buildFeedUrl(-getCurrentCanteenDayIndex() + 7);

		//create Executor and AsyncTask and execute it. Shutdown s properly.
		//Hint: use new ExecutorService instead of the default one to ensure that the task is started immediately
		ExecutorService s = Executors.newSingleThreadExecutor();
		new DietFetchTask(this).executeOnExecutor(s, this.selectedMensa.getId() + "", firstWeekFeedUrl, secondWeekFeedUrl);
		s.shutdown();
	}

	public void updateDietAndView(Diet diet) {
		this.currentDiet = diet;

		viewPager.setAdapter((this.dayPagerAdapter = 
				new DayPagerAdapter(getResources(), getSupportFragmentManager())));
		dayPagerAdapter.setDiet(diet);
		dayPagerAdapter.notifyDataSetChanged();

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
			showAboutActivity();
			break;
		}
		return true;
	}

	private void goToToday() {
		viewPager.setCurrentItem(getCurrentDayIndex());
	}

	private void showAboutActivity() {
		Intent i = new Intent(this, AboutActivity.class);
		this.startActivity(i);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		//Get the Mensa object selceted by the user
		Mensa m = MensaDatabase.createMensaDatabase().getMensaAtPosition(position);

		//If the selected object is not the current selected one
		if(m != this.selectedMensa) {

			//set the selected one
			this.selectedMensa = m;

			//Save the is to the SharedPreferences Object
			SharedPreferences settings = getSharedPreferences(this.SHARED_PREFS_NAME, 0);
			settings.edit().putInt(this.SELECTED_MENSA_SETTING, this.selectedMensa.getId()).apply();

			//Refresh the diet to dispaly the diet for the new canteen
			this.refreshDiet();
		}

		//Close the drawer
		this.mDrawerLayout.closeDrawers();

	}

}
