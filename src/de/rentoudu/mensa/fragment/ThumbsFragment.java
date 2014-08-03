package de.rentoudu.mensa.fragment;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appspot.mensa_furtwangen.thumbs.model.Thumb;

import de.hfu.mensa.R;
import de.rentoudu.mensa.Utils;
import de.rentoudu.mensa.task.ThumbInsertTask;
import de.rentoudu.mensa.task.ThumbRemoveTask;
import de.rentoudu.mensa.task.ThumbsFetchTask;

/**
 * This fragments represents the rating of a menu.
 * 
 * <p>The thumb ratings will be fetched after the view has been created ({@link #onViewCreated(View, Bundle)}.</p>
 */
public class ThumbsFragment extends Fragment implements OnClickListener {

	private ThumbState currentState;
	private String menuId;
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rating, container, false);
		TextView thumbUpView = (TextView) view.findViewById(R.id.rating_thumb_up);
		TextView thumbDownView = (TextView) view.findViewById(R.id.rating_thumb_down);
		thumbUpView.setOnClickListener(this);
		thumbDownView.setOnClickListener(this);
		
		menuId = getArguments().getString("menuId");

		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// Start asynchronous rating fetch task.
		new ThumbsFetchTask(this).execute(Utils.getDeviceId(), menuId);
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(getView().findViewById(R.id.rating_thumb_up))) {
			updateThumb(ThumbState.UP);
		} else if (v.equals(getView().findViewById(R.id.rating_thumb_down))) {
			updateThumb(ThumbState.DOWN);
		} else {
			// Nothing to do.
		}
	}
	
	public boolean isThumbsViewInitialized() {
		return getView() != null; 
	}
	
	private TextView getThumbUpView() {
		View view = getView();
		View thumbView = view.findViewById(R.id.rating_thumb_up);
		return (TextView) thumbView;
	}
	
	private TextView getThumbDownView() {
		View view = getView();
		View thumbView = view.findViewById(R.id.rating_thumb_down);
		return (TextView) thumbView;
	}
	
	/**
	 * Sets the count for a given thumb.
	 * 
	 * @param thumbState
	 * @param count
	 */
	public void setThumbCount(ThumbState thumbState, int count) {
		if(isThumbsViewInitialized() == false) {
			// Small quick fix for some crazy errors (R.id.rating_thumb_up is null..)
			return;
		}

		if(ThumbState.UP.equals(thumbState)) {
			TextView thumbUpView = getThumbUpView();
			thumbUpView.setText(String.valueOf(count));
		} else if (ThumbState.DOWN.equals(thumbState)) {
			TextView thumbDownView = getThumbDownView();
			thumbDownView.setText(String.valueOf(count));
		} else {
			// Nothing to do.
		}
	}
	
	/**
	 * Returns the thumb count for a given state.
	 * 
	 * @param thumbState
	 * 
	 * @return the determined count
	 */
	private int getThumbCount(ThumbState thumbState) {
		if(ThumbState.UP.equals(thumbState)) {
			return Integer.parseInt(getThumbUpView().getText().toString());
		} else if (ThumbState.DOWN.equals(thumbState)) {
			return Integer.parseInt(getThumbDownView().getText().toString());
		} else {
			return 0;
		}
	}
	
	/**
	 * Sets a new state and ignores counting (useful for initializing).
	 * 
	 * @param newState or null for no state.
	 */
	public void setThumb(ThumbState newState) {
		if(isThumbsViewInitialized() == false) {
			// Small quick fix for some crazy errors (R.id.rating_thumb_up is null..)
			return;
		}
		
		if(ThumbState.UP.equals(newState)) {
			getThumbUpView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_good_green, 0, 0, 0);
		} else if(ThumbState.DOWN.equals(newState)) {
			getThumbDownView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bad_red, 0, 0, 0);
		}
		currentState = newState;
	}
	
	/**
	 * Sets the correct thumb icon for a given state.
	 * 
	 * @param thumb pass null to reset thumbs
	 */
	private void updateThumb(ThumbState newState) {
		if(isThumbsViewInitialized() == false) {
			// Small quick fix for some crazy errors (R.id.rating_thumb_up is null..)
			return;
		}
		
		TextView thumbUpView = getThumbUpView();
		TextView thumbDownView = getThumbDownView();
		
		// Disable interaction while rating
		thumbUpView.setOnClickListener(null);
		thumbDownView.setOnClickListener(null);
		
		String deviceId = Utils.getDeviceId();
		
		// Creating thumb model for interaction with service.
		// Also note:
		// There is no need to remove old thumb states on server side
		// (getting done automatically after insert)
		Thumb thumb = new Thumb();
		thumb.setDeviceId(deviceId);
		thumb.setMenuId(menuId);
		thumb.setState(newState.toString());
		
		// Remove thumbs (from up)
		if(ThumbState.UP.equals(newState) && ThumbState.UP.equals(currentState)) {
			thumbUpView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_good, 0, 0, 0);
			setThumbCount(ThumbState.UP, getThumbCount(ThumbState.UP) - 1);
			currentState = null;
			new ThumbRemoveTask().execute(deviceId, menuId);
			
		// Thumb up and may remove thumb down
		} else if (ThumbState.UP.equals(newState)) {
			thumbUpView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_good_green, 0, 0, 0);
			thumbDownView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bad, 0, 0, 0);
			setThumbCount(ThumbState.UP, getThumbCount(ThumbState.UP) + 1);
			if(ThumbState.DOWN.equals(currentState)) {
				setThumbCount(ThumbState.DOWN, getThumbCount(ThumbState.DOWN) - 1);
			}
			currentState = ThumbState.UP;
			new ThumbInsertTask().execute(thumb);
		
		// Remove thumbs (from down)
		} else if (ThumbState.DOWN.equals(newState)  && ThumbState.DOWN.equals(currentState)) {
			thumbDownView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bad, 0, 0, 0);
			setThumbCount(ThumbState.DOWN, getThumbCount(ThumbState.DOWN) - 1);
			currentState = null;
			new ThumbRemoveTask().execute(deviceId, menuId);
			
		// Thumb down and may remove thumb up
		} else if (ThumbState.DOWN.equals(newState)) {
			thumbUpView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_good, 0, 0, 0);
			thumbDownView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bad_red, 0, 0, 0);
			setThumbCount(ThumbState.DOWN, getThumbCount(ThumbState.DOWN) + 1);
			if(ThumbState.UP.equals(currentState)) {
				setThumbCount(ThumbState.UP, getThumbCount(ThumbState.UP) - 1);
			}
			currentState = ThumbState.DOWN;
			new ThumbInsertTask().execute(thumb);
		}
		
		// Enable interaction after rating
		thumbUpView.setOnClickListener(this);
		thumbDownView.setOnClickListener(this);
	}
	
	public enum ThumbState implements Serializable {
		UP, DOWN
	}

}
