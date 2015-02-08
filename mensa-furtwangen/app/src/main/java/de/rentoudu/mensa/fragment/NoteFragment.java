package de.rentoudu.mensa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hfu.mensa.R;

/**
 * This fragments represents the notes of a day.
 */
public class NoteFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note, container, false);
		TextView notes = (TextView) view.findViewById(R.id.notes);
		String text = (String) getArguments().get("text");
		notes.setText(text);
		return view;
	}
	
	public static NoteFragment fromText(String text) {
		NoteFragment fragment = new NoteFragment();
    	Bundle args = new Bundle();
    	args.putSerializable("text", text);
    	fragment.setArguments(args);
    	return fragment;
	}
	
}
