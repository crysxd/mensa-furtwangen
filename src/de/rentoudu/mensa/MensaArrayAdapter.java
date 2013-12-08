package de.rentoudu.mensa;

import de.rentoudu.mensa.model.Mensa;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class MensaArrayAdapter extends ArrayAdapter<Mensa> {

	public MensaArrayAdapter(Context context, Mensa[] objects) {
		super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get view from super
		View view = super.getView(position, convertView, parent);
		TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		TextView text2 = (TextView) view.findViewById(android.R.id.text2);
		
		text1.setText(this.getItem(position).getName());
		text2.setText(this.getItem(position).getPlace());
		
		return view;
	}

	
}
