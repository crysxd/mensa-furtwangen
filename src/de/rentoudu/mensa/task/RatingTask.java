package de.rentoudu.mensa.task;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.ratings.Ratings;

import android.os.AsyncTask;
import de.rentoudu.mensa.RatingBarController;

public abstract class RatingTask<P, C ,T> extends AsyncTask<P, C, T>{

	private RatingBarController activeController;

	public RatingTask(RatingBarController controller) {
		this.activeController = controller;
	}

	public RatingBarController getActiveController() {
		return activeController;
	}

	protected Ratings createRatingsService() {
		Ratings.Builder ratingsBuilder = new Ratings.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		ratingsBuilder.setApplicationName("Mensa Furtwangen");//Quick fix, TODO: Load from Resources

		return ratingsBuilder.build();
	}

}
