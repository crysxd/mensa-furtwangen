package de.rentoudu.mensa.task;

import java.io.IOException;

import com.appspot.mensa_furtwangen.thumbs.Thumbs;
import com.appspot.mensa_furtwangen.thumbs.model.ThumbsResult;

import android.os.AsyncTask;
import de.rentoudu.mensa.Utils;
import de.rentoudu.mensa.fragment.ThumbsFragment;
import de.rentoudu.mensa.fragment.ThumbsFragment.ThumbState;

/**
 * This task fetches the total rating count for a specific menu.
 * 
 * <p>Call example:</p>
 * <code>
 *     ...	
 *     new ThumbsFetchTask(this).execute(Utils.getDeviceId(), menuId);
 *     ...
 * </code>
 */
public class ThumbsFetchTask extends AsyncTask<String, Void, ThumbsResult> {

	private ThumbsFragment thumbsFragment;
	
	public ThumbsFetchTask(ThumbsFragment thumbsFragment) {
		this.thumbsFragment = thumbsFragment;
	}

	@Override
	protected ThumbsResult doInBackground(String... params) {
		Thumbs service = Utils.buildThumbsService();
		try {
			String deviceId = params[0];
			String menuId = params[1];
			return service.fetch(deviceId, menuId).execute();
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(ThumbsResult result) {
		// No result handling.
		if(result == null) {
			return;
		}
		if(ThumbState.UP.toString().equals(result.getDeviceState())) {
			thumbsFragment.setThumb(ThumbState.UP);
		} else if (ThumbState.DOWN.toString().equals(result.getDeviceState())) {
			thumbsFragment.setThumb(ThumbState.DOWN);
		} else {
			// Nothing to do.
		}
		thumbsFragment.setThumbCount(ThumbState.UP, result.getThumbsUp());
		thumbsFragment.setThumbCount(ThumbState.DOWN, result.getThumbsDown());
	}

}
