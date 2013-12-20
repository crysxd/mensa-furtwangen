package de.rentoudu.mensa;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RatingBar;

import com.google.api.services.ratings.model.Rating;

import de.rentoudu.mensa.fragment.RatingBarDialogFragment;
import de.rentoudu.mensa.fragment.RatingBarDialogFragment.RatingBarDialogListener;
import de.rentoudu.mensa.model.Menu;
import de.rentoudu.mensa.task.RatingFetchTask;
import de.rentoudu.mensa.task.RatingInsertTask;

public class RatingBarController implements OnTouchListener, RatingBarDialogListener {

	private Fragment activeFragment;
	private RatingBar activeRatingBar;
	private Menu activeMenu;

	public RatingBarController(Fragment fragment, RatingBar ratingBar, Menu menu) {
		this.activeFragment = fragment;
		this.activeRatingBar = ratingBar;
		this.activeMenu = menu;
	}

	/**
	 * Updates the view asynchrony.
	 */
	public void refresh() {
		new RatingFetchTask(this).execute();
	}

	@Override
	public void onDialogPositiveClick(int rating) {
		if(rating > 0 && rating < 6 && getActiveMenu().getMainCourse() != null) {

			// Build rating.
			Rating userRating = new Rating();
			userRating.setDeviceId(Utils.getDeviceId());
			userRating.setMenuId(getActiveMenu().getMainCourse().toString());
			userRating.setRatingScore((double) rating);
			// Persist rating.
			new RatingInsertTask(this).execute(userRating);

		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(getFragmentManager().findFragmentByTag("RatingDialogFragment") == null) {
			RatingBarDialogFragment ratingDialog = new RatingBarDialogFragment();
			ratingDialog.show(getFragmentManager(), "RatingDialogFragment");
			ratingDialog.setRatingBarDialogListener(this);
		}
		return true;
	}

	public Menu getActiveMenu() {
		return activeMenu;
	}

	public RatingBar getActiveRatingBar() {
		return activeRatingBar;
	}

	public FragmentActivity getActivity() {
		return activeFragment.getActivity();
	}

	public FragmentManager getFragmentManager() {
		return activeFragment.getFragmentManager();
	}

}