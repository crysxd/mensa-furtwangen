package de.rentoudu.mensa.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hfu.mensa.R;
import de.rentoudu.mensa.MainActivity;
import de.rentoudu.mensa.Utils;

/**
 * This fragments represents the countdown and opening hours.
 */
public class CountdownFragment extends Fragment {

	//TODO: Dirty dirtier dirtiest....
	private int openningHour = MainActivity.openingTime[0];
	private int openingMinute = MainActivity.openingTime[1];
	
	private int closingHour = MainActivity.openingTime[0];
	private int closingMinute = MainActivity.openingTime[1];
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_countdown, container, false);
		
		if( Utils.isAfter(openningHour, openingMinute) && Utils.isBefore(closingHour, closingMinute) ) {
			initializeCountDownTimer(view);
		}
		
		return view;
	}

	private void initializeCountDownTimer(View view) {
		final TextView countdown = (TextView) view.findViewById(R.id.opening_countdown);
		
		new CountDownTimer(Utils.getMilliseconds(closingHour, closingMinute) - Utils.getNow(), 1000) {
			
			public void onTick(long millisUntilFinished) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
				String time = sdf.format(new Date(millisUntilFinished - TimeZone.getDefault().getRawOffset()));
				countdown.setText(time);
			}
			
			public void onFinish() {
				countdown.setText(R.string.text_closed);
			}
			
		}.start();
	}

}
