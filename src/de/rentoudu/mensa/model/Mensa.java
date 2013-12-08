package de.rentoudu.mensa.model;

import java.sql.Time;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;

public class Mensa {

	@Attribute(name="id")
	private int id = -1;
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
	
	/*
	 * Empty constructor for desirialisation
	 */
	public Mensa() {
	}

	/**
	 * Returns the id of this Mensa object.
	 * @return The id of this Mensa object
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the name of this Mensa object.
	 * @return The name of this Mensa object
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the street and number of this Mensa object.
	 * @return The street and number of this Mensa object
	 */
	public String getStreet() {
		return street;
	}
	
	/**
	 * Returns the place name of this Mensa object.
	 * @return The place name of this Mensa object
	 */
	public String getPlace() {
		return place;
	}
	
	/**
	 * Returns the description of this Mensa object.
	 * @return The description of this Mensa object
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the zip code of this Mensa object.
	 * @return The zip code of this Mensa object
	 */
	public String getZip() {
		return zip;
	}
}
