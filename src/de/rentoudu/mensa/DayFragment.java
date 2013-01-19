package de.rentoudu.mensa;

import de.rentoudu.mensa.model.Day;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class DayFragment extends Fragment {

	public DayFragment() {
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_day, container, false);
    	
    	TextView menuOneAppetizer = (TextView) view.findViewById(R.id.menuOne_appetizer);
    	TextView menuOnemainCourse = (TextView) view.findViewById(R.id.menuOne_maincourse);
    	TextView menuOneSideDish = (TextView) view.findViewById(R.id.menuOne_sidedish);
    	
    	TextView menuTwoAppetizer = (TextView) view.findViewById(R.id.menuTwo_appetizer);
    	TextView menuTwomainCourse = (TextView) view.findViewById(R.id.menuTwo_maincourse);
    	TextView menuTwoSideDish = (TextView) view.findViewById(R.id.menuTwo_sidedish);
    	
    	TextView notes = (TextView) view.findViewById(R.id.notes);
    	
    	Day day = (Day) getArguments().get("day");
    	
    	menuOneAppetizer.setText(day.getMenuOne().getAppetizer());
    	menuOnemainCourse.setText(day.getMenuOne().getMainCourse());
    	menuOneSideDish.setText(day.getMenuOne().getSideDish());
    	
    	menuTwoAppetizer.setText(day.getMenuTwo().getAppetizer());
    	menuTwomainCourse.setText(day.getMenuTwo().getMainCourse());
    	menuTwoSideDish.setText(day.getMenuTwo().getSideDish());
    	
    	notes.setText(day.getNotes());
    	
        return view;
    }
}