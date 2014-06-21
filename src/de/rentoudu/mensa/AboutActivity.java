package de.rentoudu.mensa;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends DefaultActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_about);

		String version = this.getResources().getString(R.string.text_empty);
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// Nothing to do.
		}
		
		String message = String.format(getString(R.string.text_about), version);
		((TextView) this.findViewById(R.id.text1)).setText(message);

	}
}
