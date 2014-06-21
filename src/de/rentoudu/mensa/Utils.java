package de.rentoudu.mensa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.appspot.mensa_furtwangen.thumbs.Thumbs;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import android.os.Build;

public class Utils {

	public static Thumbs buildThumbsService() {
		Thumbs.Builder thumbsBuilder = new Thumbs.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		thumbsBuilder.setApplicationName("mensa-furtwangen");
		Thumbs thumbsService = thumbsBuilder.build();
		return thumbsService;
	}
	
	public static int getDay() {
		return getCalendar().get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getWeek() {
		return getCalendar().get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getYear() {
		return getCalendar().get(Calendar.YEAR);
	}
	
	public static String getFormattedDate(String unformattedDate) {
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
		SimpleDateFormat simpleDateFormat = (SimpleDateFormat) dateFormat;
		Date date;
		try {
			date = new SimpleDateFormat("E, d.M.y", Locale.GERMANY).parse(unformattedDate);
		} catch (Exception e) {
			date = new Date();
		}
		String formattedDate = simpleDateFormat.format(date);
		return formattedDate;
	}
	
	public static long getNow() {
		return getCalendar().getTimeInMillis();
	}
	
	public static long getMilliseconds(int hour, int minute) {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	public static boolean isAfter(int hour, int minute) {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date closingDate = calendar.getTime();
		Date currentDate = new Date();
		return currentDate.after(closingDate);
	}
	
	public static boolean isBefore(int hour, int minute) {
		Calendar calendar = getCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date openingDate = calendar.getTime();
		Date currentDate = new Date();
		return currentDate.before(openingDate);
	}
	
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
    	calendar.setFirstDayOfWeek(Calendar.MONDAY);
    	return calendar;
	}
	
	public static String getDeviceId() {
		return "35"
				+ // we make this look like a valid IMEI
				Build.BOARD.length() % 10 + Build.BRAND.length() % 10
				+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
				+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
				+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
				+ Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
				+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10
				+ Build.USER.length() % 10; // 13 digits
	}
	
}
