package de.rentoudu.mensa;

import java.util.Calendar;
import java.util.Locale;

import de.rentoudu.mensa.fragment.DayFragment;
import de.rentoudu.mensa.model.Day;
import de.rentoudu.mensa.model.Diet;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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

	private Resources resources;
	private Diet diet;
	
    public DayPagerAdapter(Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
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
    	String guid = day.getGuid();
    	int weekDay =  day.getDay();
    	
    	String pageTitle = "EMPTY";
    	
    	switch (weekDay) {
            case Calendar.MONDAY:
            	pageTitle = getPageTitle(R.string.weekday_mo, guid);
            	break;
            case Calendar.TUESDAY:
            	pageTitle = getPageTitle(R.string.weekday_tu, guid);
            	break;
            case Calendar.WEDNESDAY:
            	pageTitle = getPageTitle(R.string.weekday_we, guid);
            	break;
            case Calendar.THURSDAY:
            	pageTitle = getPageTitle(R.string.weekday_th, guid);
            	break;
            case Calendar.FRIDAY:
            	pageTitle = getPageTitle(R.string.weekday_fr, guid);
            	break;
            case Calendar.SATURDAY:
            	pageTitle = getPageTitle(R.string.weekday_sa, guid);
            	break;
            case Calendar.SUNDAY:
            	pageTitle = getPageTitle(R.string.weekday_so, guid);
            	break;
            default:
            	pageTitle = "UNKOWN";
            	break;
    	}

        return pageTitle;
    }
    
    private String getPageTitle(int dayName, String guid) {
    	return resources.getString(dayName).toUpperCase(Locale.GERMANY) + " (" + Utils.getFormattedDate(guid) + ")";
    }
}