package de.rentoudu.mensa.model;

import java.lang.reflect.Field;
import java.sql.Time;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;

import de.rentoudu.mensa.R;

import android.content.res.Resources;

public class Mensa {

	@Attribute(name="id")
	private int id;
	@Element
	private String name;
	@Element
	private String street;
	@Element
	private String zip;
	@Element
	private String place;
	@Element
	private String description;
	@Element(required=false)
	private String iconResourceName = "ic_default_canteen_icon";
	
	public Mensa() {
		// TODO Auto-generated constructor stub
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
//	public String getDescription() {
//		return description;
//	}
	public String getZip() {
		return zip;
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
