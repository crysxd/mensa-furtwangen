package de.rentoudu.mensa.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.os.Environment;

public class RssCache {

	private final File CACHE_DIRECTORY = new File(Environment.getExternalStorageDirectory(), "/Android/data/de.rentoudu.mensa");
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	
	public RssCache() {
		
		this.CACHE_DIRECTORY.mkdir();
		
		//List all files and delete all expires ones
		File files[] = this.CACHE_DIRECTORY.listFiles();
		
		if(files == null)
			return;
		
		for(File f: files) {
			if(this.isFileExpired(f) || f.length() == 0) {
				f.delete();
			}
		}
		
	}
	
	private File getFileForId(int id) {
		
		//Generate the beginning of the file that represents the cache for the url and list all files in cache dir
		String fileName = id + "_";
		File[] files = this.CACHE_DIRECTORY.listFiles();

		if(files == null)
			return null;
		
		//Go throug all files
		for(File f:files) {
			
			//If the filename is correct for the url
			if(f.getName().startsWith(fileName)) {
				
				//check if the file is expired. If true return null to indicate that no File was found and delete the expired one
				if(this.isFileExpired(f)) {
					f.delete();
					return null;
					
				//Everything is fine, cached file found and is valid. Return it.
				} else {
					return f;
				}
			}
		}
		
		//Nothing found. Return null
		return null;
	}
	
	private boolean isFileExpired(File f) {
		
		//Get the start index of the date, file name has the pattern *HASH*_YYYY-MM-dd-hh-mm
		int start = f.getName().indexOf("_") + 1;
		
		//get date String
		String dateString = f.getName().substring(start);
		
		//format
		Date expireDate;
		try {
			expireDate = this.DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
			
			//Something went wrong...return false
			return true;
		}
		
		//calculate if the expiration date is in the past
		return new Date().after(expireDate);
		
	}
	
	public InputStream fetchRssFeed(int id, String url, Date expireDate) throws IOException {
		
		//Get the cached file for the url, may not exist
		File f = this.getFileForId(id);
		
		try {
			//if f is null the cache file does not exist or is expired -> reload
			if(f == null)
				f = this.cacheRssFeedToFile(id, url, expireDate);
			
			//create a FileInputStream and return it
			return new FileInputStream(f);
		} catch(Exception e) {
			e.printStackTrace();
			
			//If an error occurs try to return the direct InputStream over HTTP
			return this.getRssInputStream(url);
			
		}
	
		
	}
	
	private File cacheRssFeedToFile(int id, String url, Date expireDate) throws Exception {
		
		File f = null;
		try {
			//create File
			f = new File(this.CACHE_DIRECTORY, id + "_" + this.DATE_FORMAT.format(expireDate));
			f.createNewFile();
			
			//get reader and writer
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(f));
			BufferedReader rssReader = new BufferedReader(new InputStreamReader(this.getRssInputStream(url)));
			
			//write the contents of the rss feed to the file
			String line;
			while((line = rssReader.readLine()) != null) {
				fileWriter.write(line + "\n");
			}
			
			//close both streams
			fileWriter.close();
			rssReader.close();
			
			//return the written file
			return f;
			
		} catch(Exception e) {
			//Delete f to ensure that no corrupt data is stored
			f.delete();
			
			throw e;
		}
		

	}
	
	/**
	 * Given a string representation of a URL, sets up a connection and gets an input stream.
	 * @param urlString The String object representing the URL
	 * @return The InpuStream from which the RSS feed can be read.
	 * @throws IOException
	 */
	private InputStream getRssInputStream(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// Starts the query
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}
	
}
