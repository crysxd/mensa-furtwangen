package de.rentoudu.mensa.task;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

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

public class UrlCache {

	/*
	 * Singelton design pattern
	 */
	private static UrlCache instance;
	
	public synchronized static UrlCache getCache() {
		if(instance == null)
			instance = new UrlCache();
		
		return instance;
	}
	
	/*
	 * Class context
	 */
	private final File CACHE_DIRECTORY = new File(Environment.getExternalStorageDirectory(), "/Android/data/de.rentoudu.mensa");
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	
	private UrlCache() {
		
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
	
	public synchronized void clearCache() {
		File[] files = this.CACHE_DIRECTORY.listFiles();
		
		for(File f : files) {
			f.delete();
			
		}
	}
	
	private synchronized File getFileForURL(String url) {
		
		//Generate the beginning of the file that represents the cache for the url and list all files in cache dir
		String fileName = url.hashCode() + "_";
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
	
	public InputStream fetchUrl(String url, Date expireDate, boolean useCachedValues) throws IOException {
		
		//Get the cached file for the url, may not exist
		File f = useCachedValues ? this.getFileForURL(url) : null;
		
		try {

            //if f is null the cache file does not exist or is expired -> reload
			if(f == null) {
                Log.i(this.getClass().getSimpleName(), "Cache miss for " + url.hashCode());
                f = this.cacheUrlToFile(url, expireDate);
            }
			
			//create a FileInputStream and return it
            Log.i(this.getClass().getSimpleName(), "Cache hit for " + url.hashCode());
            return new FileInputStream(f);

		} catch(Exception e) {
            Log.e(this.getClass().getSimpleName(), "Error while reading cache file for " + url.hashCode());
            e.printStackTrace();
			//If an error occurs try to return the direct InputStream over HTTP
			return this.getUrlInputStream(url);
			
		}
	
		
	}
	
	private synchronized File cacheUrlToFile(String url, Date expireDate) throws Exception {
		
		File f = null;
		try {
			//create File
			f = new File(this.CACHE_DIRECTORY, url.hashCode() + "_" + this.DATE_FORMAT.format(expireDate));
			f.createNewFile();
			
			//get reader and writer
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(f));
			BufferedReader urlReader = new BufferedReader(new InputStreamReader(this.getUrlInputStream(url)));
			
			//write the contents of the url to the file
			String line;
			while((line = urlReader.readLine()) != null) {
				fileWriter.write(line + "\n");
			}
			
			//close both streams
			fileWriter.close();
			urlReader.close();
			
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
	 * @return The InpuStream from which the URL can be read from.
	 * @throws java.io.IOException
	 */
	private InputStream getUrlInputStream(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);

        //Hard code a Chrome user agent
        //This is necessary because the server returnes an error if no Chrome/IE/... user agent is set
        //Let the games begin...
        conn.setRequestProperty("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.3");

		// Starts the query
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}
	
}
