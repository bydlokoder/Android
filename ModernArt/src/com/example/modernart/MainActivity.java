package com.example.modernart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	private static String link = "http://www.moma.org";
	private LinearLayout red;
	private LinearLayout yellow;
	private LinearLayout green;
	private LinearLayout lightBlue;
	private LinearLayout blue;
	private LinearLayout magenta;

	private int redColor;
	private int yellowColor;
	private int greenColor;
	private int lightBlueColor;
	private int blueColor;
	private int magentaColor;

	private int maxProgress = 500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		red = (LinearLayout) findViewById(R.id.redCube);
		yellow = (LinearLayout) findViewById(R.id.yellowCube);
		green = (LinearLayout) findViewById(R.id.greenCube);
		lightBlue = (LinearLayout) findViewById(R.id.lightBlueCube);
		blue = (LinearLayout) findViewById(R.id.blueCube);
		magenta = (LinearLayout) findViewById(R.id.magentaCube);

		redColor = ((ColorDrawable) red.getBackground()).getColor();
		yellowColor = ((ColorDrawable) yellow.getBackground()).getColor();
		greenColor = ((ColorDrawable) green.getBackground()).getColor();
		lightBlueColor = ((ColorDrawable) lightBlue.getBackground()).getColor();
		blueColor = ((ColorDrawable) blue.getBackground()).getColor();
		magentaColor = ((ColorDrawable) magenta.getBackground()).getColor();

		SeekBar seekBar = (SeekBar) findViewById(R.id.colorSeeker);
		seekBar.setMax(maxProgress);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				changeColor(red, redColor, progress, maxProgress);
				changeColor(yellow, yellowColor, progress, maxProgress);
				changeColor(blue, blueColor, progress, maxProgress);
				changeColor(green, greenColor, progress, maxProgress);
				changeColor(lightBlue, lightBlueColor, progress, maxProgress);
				changeColor(magenta, magentaColor, progress, maxProgress);

			}
		});
	}

	public void changeColor(LinearLayout coub, int standartColor, int progress,
			int maxProgress) {
		float[] hsvColor = new float[3];
		Color.colorToHSV(standartColor, hsvColor);
		hsvColor[0] = hsvColor[0] + progress;
		hsvColor[0] = hsvColor[0] % 360;
		coub.setBackgroundColor(Color.HSVToColor(Color.alpha(standartColor),
				hsvColor));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.more_information) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setMessage(getResources().getString(R.string.dialog));
			
			dialog.setNegativeButton("Not now", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			dialog.setPositiveButton("Visit MOMA", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(link));
					startActivity(intent);
					dialog.dismiss();

				}
			});
			dialog.create();
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}
}
