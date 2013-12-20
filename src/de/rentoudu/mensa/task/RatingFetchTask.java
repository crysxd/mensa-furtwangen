package de.rentoudu.mensa.task;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.ratings.Ratings;
import com.google.api.services.ratings.model.Rating;
import com.google.api.services.ratings.model.TotalMenuRatingQuery;

import de.rentoudu.mensa.RatingBarController;

public class RatingFetchTask extends RatingTask<Void, Void, Rating>{


	public RatingFetchTask(RatingBarController controller) {
		super(controller);
	}

	@Override
	protected Rating doInBackground(Void... params) {
		Ratings ratingService = this.createRatingsService();
		
		TotalMenuRatingQuery query = new TotalMenuRatingQuery();
		query.setMenuId(getActiveController().getActiveMenu().getMainCourse());
		
		try {
			return ratingService.fetchTotalMenuRating(query).execute();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Rating result) {
		if(result != null) {
			Double ratingScore = result.getRatingScore();
			float roundedRatingScore = (float) Math.round(ratingScore);
			getActiveController().getActiveRatingBar().setRating(roundedRatingScore);
		}
	}
	
}
