package de.rentoudu.mensa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class Diet implements Serializable {

	private Date lastSynced;

    private String urlNextWeek;
	
	private List<Day> days;
	
	public Diet() {
		this.days = new ArrayList<Day>();
	}
	
	public Date getLastSynced() {
		return lastSynced;
	}
	
	public void setLastSynced(Date lastSynced) {
		this.lastSynced = lastSynced;
	}
	
	public void addDay(Day day) {
		this.days.add(day);
	}
	
	public void addDays(List<Day> days) {
		this.days.addAll(days);
	}
	
	public List<Day> getDays() {
		return days;
	}
	
	public void setDays(List<Day> days) {
		this.days = days;
	}

    public void setUrlQueryNextWeek(String query) { this.urlNextWeek = "?" + query; }

    public String getUrlQueryNextWeek() { return this.urlNextWeek; }
	
}
