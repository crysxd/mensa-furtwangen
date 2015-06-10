package de.rentoudu.mensa;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public abstract class DefaultActivity extends ActionBarActivity {

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
