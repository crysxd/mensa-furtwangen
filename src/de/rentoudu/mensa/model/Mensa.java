package de.rentoudu.mensa.model;

import java.sql.Time;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;

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
	
	

}
