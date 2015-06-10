package de.rentoudu.mensa;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.hfu.mensa.R;
import de.rentoudu.mensa.model.Mensa;
import de.rentoudu.mensa.model.MensaDatabase;

public class CanteenInfoActivity extends DefaultActivity {

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
		Mensa m = MensaDatabase.createMensaDatabase(this).getMensaForId(selectedMensaId);

		//Set infos in text views
		((TextView) this.findViewById(R.id.tv_info)).setText(Html.fromHtml(m.getDescription()));
		((TextView) this.findViewById(R.id.tv_opening_hours)).setText(Html.fromHtml(m.getOpeningHours()));
		((TextView) this.findViewById(R.id.tv_street)).setText(m.getStreet());
		((TextView) this.findViewById(R.id.tv_place_zip)).setText(m.getPlace() + " " + m.getZip());
		
    	//Set Typeface
		Typeface myTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
		((TextView) this.findViewById(R.id.tv_title_address)).setTypeface(myTypeface);
		((TextView) this.findViewById(R.id.tv_title_info)).setTypeface(myTypeface);
		((TextView) this.findViewById(R.id.tv_title_opening_hours)).setTypeface(myTypeface);

        //Set up Toolbar
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(bar);
		this.getSupportActionBar().setTitle(m.getName());
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}
