package de.rentoudu.mensa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Day implements Serializable {


	private String title;
	private String guid;
	
	private List<Menu> menus;
	
	private String notes;
	
	public Day() {
		this.menus = new ArrayList<Menu>();
	}
	
	public void addMenu(Menu menu) {
		menus.add(menu);
	}
	
	public List<Menu> getMenus() {
		return menus;
	}
	
	public boolean hasMenus() {
		return menus.isEmpty() == false;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;

        //Parse the HTML definition list to a more convinient layour
        this.notes = this.notes.replace("\n", "");
        this.notes = this.notes.replace("\r", "");
        this.notes = this.notes.replaceAll("( )*</dd>( )*<dt>( )*", ", (");
        this.notes = this.notes.replaceAll("( )*</dt>( )*<dd>( )*", ") ");
        this.notes = this.notes.replaceAll("( )*<dt>( )*", "(");
        this.notes = this.notes.replaceAll("( )*</dd>( )*", "");

    }
	
	public String getNotes() {
		return notes;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}
