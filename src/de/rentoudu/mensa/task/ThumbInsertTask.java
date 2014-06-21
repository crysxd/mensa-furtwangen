package de.rentoudu.mensa.task;

import android.os.AsyncTask;

import com.appspot.mensa_furtwangen.thumbs.Thumbs;
import com.appspot.mensa_furtwangen.thumbs.model.Thumb;

import de.rentoudu.mensa.Utils;

public class ThumbInsertTask extends AsyncTask<Thumb, Void, Void> {

	@Override
	protected Void doInBackground(Thumb... params) {
		Thumbs service = Utils.buildThumbsService();
		try {
			Thumb thumb = params[0];
			service.insert(thumb).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
