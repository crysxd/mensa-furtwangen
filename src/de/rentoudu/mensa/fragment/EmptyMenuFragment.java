package de.rentoudu.mensa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.rentoudu.mensa.R;

/**
 * This fragments represents an empty day (no menus).
 */
public class EmptyMenuFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_empty, container, false);
	}

}
