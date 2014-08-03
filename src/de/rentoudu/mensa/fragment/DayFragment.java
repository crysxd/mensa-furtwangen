package de.rentoudu.mensa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import de.hfu.mensa.R;
import de.rentoudu.mensa.model.Day;
import de.rentoudu.mensa.model.Menu;

/**
 * The fragment representing both menus, a rating bar and the notes.
 */
public class DayFragment extends Fragment {
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_day, container, false);
    	
    	if(savedInstanceState == null) {
    		processDay(view);
    	}
    	
        return view;
    }

    /**
     * Checks wheter we've to add some menus or add a placeholder view (in case of holidays etc.).
     * 
     * @param view
     */
	private void processDay(View view) {
		LinearLayout dayContainer = (LinearLayout) view.findViewById(R.id.day_container);
		LinearLayout menuContainer = (LinearLayout) view.findViewById(R.id.day_menu_container);
    	Day day = (Day) getArguments().get("day");
    	if(day.hasMenus())
    		processMenus(dayContainer, menuContainer, day);
    	else
    		processNoMenuView(menuContainer);
	}

	/**
	 * Adds all menus programmatically to the day fragment.
	 * 
	 * @param dayContainer
	 * @param menuContainer
	 * @param day
	 */
	private void processMenus(LinearLayout dayContainer, LinearLayout menuContainer, Day day) {
		NoteFragment notesFragment = NoteFragment.fromText(day.getNotes());
    	FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
    	// Add menus
    	for(Menu menu : day.getMenus()) {
    		MenuFragment menuFragment = MenuFragment.fromMenu(menu);
    		int menuIndex = day.getMenus().indexOf(menu);
    		fragmentTransaction.add(menuContainer.getId(), menuFragment, "menu-" + menuIndex);
    	}
    	// Add notes
    	fragmentTransaction.add(dayContainer.getId(), notesFragment, "notes");
    	// Commit fragments to view.
    	fragmentTransaction.commit();
	}
	
	/**
	 * Adds an empty view (like a placeholder).
	 * 
	 * @param container
	 */
	private void processNoMenuView(LinearLayout container) {
		EmptyMenuFragment fragment = new EmptyMenuFragment();
		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
    	fragmentTransaction.add(container.getId(), fragment, "empty");
    	fragmentTransaction.commit();
	}

}