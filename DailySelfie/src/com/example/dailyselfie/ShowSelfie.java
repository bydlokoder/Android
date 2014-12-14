package com.example.dailyselfie;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowSelfie extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_selfie);
		String photoPath = (String) getIntent().getExtras().get(
				MainActivity.SHOW_SELFIE);
		ImageView imageView = (ImageView) findViewById(R.id.showSelfie);
		if (photoPath != null) {
			Drawable d = Drawable.createFromPath(photoPath);
			imageView.setImageDrawable(d);
		}
	}
}
