package de.rentoudu.mensa.task;

import android.os.AsyncTask;

import com.appspot.mensa_furtwangen.thumbs.Thumbs;

import de.rentoudu.mensa.Utils;

public class ThumbRemoveTask extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... params) {
		Thumbs service = Utils.buildThumbsService();
		try {
			String deviceId = params[0];
			String menuId = params[1];
			service.remove(deviceId, menuId).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
