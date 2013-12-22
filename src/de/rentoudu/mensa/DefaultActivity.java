package de.rentoudu.mensa;

import android.app.Activity;
import android.view.MenuItem;

public abstract class DefaultActivity extends Activity {

	
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
