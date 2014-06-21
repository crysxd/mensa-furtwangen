package de.rentoudu.mensa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.rentoudu.mensa.model.Mensa;
import de.rentoudu.mensa.model.MensaDatabase;

public class CanteenInfoActivity extends Activity {

	/**
	 * The name of the setting in which the selected Mensa's ID is stored
	 */
	private final String SELECTED_MENSA_SETTING = "selectedMensa";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_canteen_info);

		//Load the selected Mensa's ID  from the SharedPreferences object or load Furtwangen (id=641) 
		//as the selected one ans put the selctedMensa object
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		int selectedMensaId = settings.getInt(this.SELECTED_MENSA_SETTING, 641);
		Mensa m = MensaDatabase.createMensaDatabase().getMensaForId(selectedMensaId);

		//Set infos in text views
		((TextView) this.findViewById(R.id.tv_info)).setText(Html.fromHtml(m.getDescription()));
		((TextView) this.findViewById(R.id.tv_opening_hours)).setText(Html.fromHtml(m.getOpeningHours()));
		((TextView) this.findViewById(R.id.tv_street)).setText(m.getStreet());
		((TextView) this.findViewById(R.id.tv_place_zip)).setText(m.getPlace() + " " + m.getZip());

		//Set title and icon
		this.getActionBar().setTitle(m.getName());
		this.getActionBar().setIcon(m.getIconResource());
		this.getActionBar().setDisplayHomeAsUpEnabled(true);

		//Prepare map
		GoogleMap map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setTiltGesturesEnabled(false);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);

		//Get canteen location
		LatLng location = new LatLng(m.getLatitude(), m.getLongitude());

		//create marker
		MarkerOptions marker = new MarkerOptions()
			.position(location)
			.title(m.getName());
		map.addMarker(marker);

		//center camera
		CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(location)
			.zoom(17)
			.build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1, null);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        this.finish();
	        return true;
	    }
	    
	    return super.onOptionsItemSelected(item);
	}

}
