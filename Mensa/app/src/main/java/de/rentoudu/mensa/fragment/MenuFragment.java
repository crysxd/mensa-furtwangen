package de.rentoudu.mensa.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hfu.mensa.R;
import de.rentoudu.mensa.model.Menu;

/**
 * This fragments represents menus of a day.
 */
public class MenuFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		
		TextView menuTitle = (TextView) view.findViewById(R.id.menu_title);
		TextView menuData = (TextView) view.findViewById(R.id.menu_data);
    	
    	Menu menu = getMenu();
    	
    	//Set Typeface
		Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
		menuTitle.setTypeface(myTypeface);
    	
		// Menu value
		menuTitle.setText(menu.getTitle());
		menuData.setText(menu.getData());

    	// Thumbs rating
    	if(savedInstanceState == null && menu.getTitle() != null) {
    		ThumbsFragment ratingFragment = new ThumbsFragment();
    		Bundle bundle = new Bundle();
    		bundle.putString("menuId", menu.getId());
    		ratingFragment.setArguments(bundle);
    		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    		transaction.add(R.id.menu_header_container, ratingFragment).commit();
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
	
	public Menu getMenu() {
		return (Menu) getArguments().get("menu");
	}
}
