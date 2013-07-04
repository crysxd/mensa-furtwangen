package de.rentoudu.mensa.task;

import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.ratings.Ratings;
import com.google.api.services.ratings.model.Rating;

import de.rentoudu.mensa.RatingBarController;

public class RatingInsertTask extends RatingTask<Rating, Void, Boolean>{

	public RatingInsertTask(RatingBarController controller) {
		super(controller);
	}

	@Override
	protected Boolean doInBackground(Rating... params) {
		Ratings.Builder ratingsBuilder = new Ratings.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		Ratings ratingService = ratingsBuilder.build();
		
		try {
			ratingService.insertRating(params[0]).execute();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(result) {
			getActiveController().refresh();
		} else {
			Toast.makeText(getActiveController().getActivity().getApplicationContext(), "Could not insert rating, sry.", Toast.LENGTH_SHORT).show();
		}
	}
	
}
