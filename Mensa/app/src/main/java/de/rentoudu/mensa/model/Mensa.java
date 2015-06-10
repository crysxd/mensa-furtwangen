package de.rentoudu.mensa.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.lang.reflect.Field;

import de.hfu.mensa.R;

public class Mensa {

	@Attribute(name="id")
	private int id;
	@Element
	private String name = "Default";
	@Element
	private String street = "";
	@Element
	private String zip = "";
	@Element
	private String place = "";
    @Element(name="menuurl")
    private String menuUrl = "";
	@Element
	private String description = "";
	@Element(name="openinghours")
	private String openingHours = "";
	@Element(required=false, name="iconResourcename")
	private String iconResourceName = "ic_default_canteen_icon";
	@Element
	private double latitude = 0;
	@Element
	private double longitude = 0;
	
	public Mensa() {
        //Empty construcotr needed for XML parsing
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getStreet() {
		return street;
	}
	public String getPlace() {
		return place;
	}
    public String getMenuUrl() {
        return menuUrl;
    }
    public String getDescription() {
		return description;
	}
	public String getZip() {
		return zip;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public String getOpeningHours() {
		return openingHours;
	}
	
	public int getIconResource() {
		try {
			Class<?> c = R.drawable.class;

			Field f = c.getDeclaredField(this.iconResourceName);
			f.setAccessible(true);

			return (Integer) f.get(null);	
		} catch(Exception e) {
			e.printStackTrace();
			
			return R.drawable.ic_default_canteen_icon;
		}


	}
	
	

}
