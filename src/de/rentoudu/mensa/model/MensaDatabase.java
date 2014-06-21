package de.rentoudu.mensa.model;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root
public class MensaDatabase {

	/*
	 * static content
	 */ 
	private static MensaDatabase defaultMensaDatabase = null;
	
	public static MensaDatabase createMensaDatabase() {
		
		if(MensaDatabase.defaultMensaDatabase == null ) {
			Serializer serializer = new Persister();
			Reader r = new InputStreamReader(MensaDatabase.class.getResourceAsStream("mensa_database.xml"));
			MensaDatabase db = null;
			try {
				db = serializer.read(MensaDatabase.class, r);
			} catch (Exception e) {
				e.printStackTrace();
			}
			MensaDatabase.defaultMensaDatabase = db;
			return db;
			
		} else {
			return MensaDatabase.defaultMensaDatabase;
		}
	
	}
	
	/*
	 * non-static content
	 */
	@ElementList(name="mensa", inline=true)
	private ArrayList<Mensa> mensaList;

	@SuppressWarnings("unchecked")
	public List<Mensa> getMensaList() {
		return (List<Mensa>) this.mensaList.clone();
	}

	public Mensa[] getMensaListArray() {
		return this.mensaList.toArray(new Mensa[this.mensaList.size()]);
	}
	
	public Mensa getMensaAtPosition(int position) {
		return this.mensaList.get(position);
	}
	
	public Mensa getMensaForId(int id) throws IllegalArgumentException {
		for(int i=0; i<this.mensaList.size(); i++)
			if(this.mensaList.get(i).getId() == id)
				return this.mensaList.get(i);
		
		throw new IllegalArgumentException("No canteen found for id '" + id + "'.");
	}
}
