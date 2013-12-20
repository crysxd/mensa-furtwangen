package de.rentoudu.mensa;

import de.rentoudu.mensa.model.Mensa;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

public class MensaArrayAdapter extends ArrayAdapter<Mensa> {

	public MensaArrayAdapter(Context context, Mensa[] objects) {
		super(context, R.layout.tablerow_drawer, R.id.text1, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get view from super
		View view = super.getView(position, convertView, parent);
		TextView text1 = (TextView) view.findViewById(R.id.text1);
		TextView text2 = (TextView) view.findViewById(R.id.text2);
		ImageView image1 = (ImageView) view.findViewById(R.id.image1);
		
		text1.setText(this.getItem(position).getName());
		text2.setText(this.getItem(position).getPlace());
		image1.setImageResource(this.getItem(position).getIconResource());
		
		return view;
	}

	
}
