package de.rentoudu.mensa.fragment;

import de.rentoudu.mensa.R;
import de.rentoudu.mensa.RatingBarController;
import de.rentoudu.mensa.model.Menu;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * This fragments represents menus of a day.
 */
public class MenuFragment extends Fragment {

	private RatingBarController menuRatingBarController;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		
		TextView menuTitle = (TextView) view.findViewById(R.id.menu_title);
		TextView menuAppetizer = (TextView) view.findViewById(R.id.menu_appetizer);
    	TextView menumainCourse = (TextView) view.findViewById(R.id.menu_maincourse);
    	TextView menuSideDish = (TextView) view.findViewById(R.id.menu_sidedish);
    	
    	// Ratings bar.
    	RatingBar menuRatingBar = (RatingBar) view.findViewById(R.id.menu_ratingBar);
    	
    	Menu menu = (Menu) getArguments().get("menu");
    	
		// Menu value
		menuTitle.setText(menu.getTitle());
		menuAppetizer.setText(menu.getAppetizer());
		menumainCourse.setText(menu.getMainCourse());
    	menuSideDish.setText(menu.getSideDish());
		// Rating bar
    	menuRatingBarController = new RatingBarController(this, menuRatingBar, menu);
    	menuRatingBar.setOnTouchListener(menuRatingBarController);
    	menuRatingBarController.refresh();
		
    	
    	//hide unused textfields
    	if(menuAppetizer.getText().length() == 0) {
    		menuAppetizer.setVisibility(View.GONE);
    	}
    	if(menumainCourse.getText().length() == 0) {
    		menumainCourse.setVisibility(View.GONE);
    	}
    	if(menuSideDish.getText().length() == 0) {
    		menuSideDish.setVisibility(View.GONE);
    	}
    	
		return view;
	}
	
	public static MenuFragment fromMenu(Menu menu) {
		MenuFragment fragment = new MenuFragment();
    	Bundle args = new Bundle();
    	args.putSerializable("menu", menu);
    	fragment.setArguments(args);
    	return fragment;
	}
	
}
