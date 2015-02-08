package de.rentoudu.mensa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.rentoudu.mensa.fragment.DayFragment;
import de.rentoudu.mensa.model.Day;
import de.rentoudu.mensa.model.Diet;

/**
 * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * 
 * FragmentPagerAdapter caused problems with fragments inside our DayFragment,
 * so we're using the FragmentStatePagerAdapter. This means when pages are not
 * visible to the user, their entire fragment may be destroyed, only keeping the
 * saved state of that fragment.
 */
public class DayPagerAdapter extends FragmentStatePagerAdapter {

	private Diet diet;
	
    public DayPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    
    public void setDiet(Diet diet) {
		this.diet = diet;
	}

	@Override
    public Fragment getItem(int position) {
		if(diet == null) {
    		return null;
    	}
		
    	DayFragment fragment = new DayFragment();
    	Day day = diet.getDays().get(position);
    	Bundle args = new Bundle();
    	args.putSerializable("day", day);
    	fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
    	return diet == null ? 0 : diet.getDays().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	if(diet == null) {
    		return "";
    	}
    	
    	Day day = diet.getDays().get(position);
    	Date d = Utils.getDate(day.getGuid());
    	
    	String dateString = SimpleDateFormat.getDateInstance().format(d).toUpperCase(Locale.getDefault());
    	String dayString = new SimpleDateFormat("EEEE").format(d).toUpperCase(Locale.getDefault());
    	
    	return String.format("%s (%s)", dayString, dateString);

    }
}