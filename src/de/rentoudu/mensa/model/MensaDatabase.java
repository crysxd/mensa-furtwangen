package de.rentoudu.mensa.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

@Root
public class MensaDatabase {

	/*
	 * static content
	 */ 
	private static MensaDatabase defaultMensaDatabase = null;
	
	/**
	 * Creates a new MensaDatabase instance or returns a used instance.
	 * @return A MensaDatabase instance
	 */
	public static MensaDatabase createMensaDatabase() {
		
		//If the defaultInstance is not available create new one and save as defaulr
		if(MensaDatabase.defaultMensaDatabase == null ) {
			
			
			//Create Serializer and Reader for resource
			Serializer serializer = new Persister();
			Reader r = new InputStreamReader(MensaDatabase.class.getResourceAsStream("mensa_database.xml"));
			
			//desirialize XML
			MensaDatabase db = null;
			try {
				db = serializer.read(MensaDatabase.class, r);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//set as default and return
			MensaDatabase.defaultMensaDatabase = db;
			return db;
			
		//Instance available -> return this one
		} else {
			return MensaDatabase.defaultMensaDatabase;
		}
	
	}
	
	/*
	 * non-static content
	 */
	@ElementList(name="mensa", inline=true)
	private ArrayList<Mensa> mensaList;

	/**
	 * Returns a list of all known canteens.
	 * @return a list of all known canteens
	 */
	@SuppressWarnings("unchecked")
	public List<Mensa> getMensaList() {
		return (List<Mensa>) this.mensaList.clone();
	}

	/**
	 * Returns a list of all known canteens.
	 * @return a list of all known canteens
	 */
	public Mensa[] getMensaListArray() {
		return this.mensaList.toArray(new Mensa[this.mensaList.size()]);
	}
	
	/**
	 * Returns the Mensa instance with the given id.
	 * @param id The id of the wanted Mensa instance
	 * @return The wanted Mensa instance
	 * @throws IllegalArgumentException
	 */
	public Mensa getMensaForId(int id) throws IllegalArgumentException {
		for(int i=0; i<this.mensaList.size(); i++)
			if(this.mensaList.get(i).getId() == id)
				return this.mensaList.get(i);
		
		throw new IllegalArgumentException("No canteen found for id '" + id + "'.");
	}
}
