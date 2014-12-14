package com.example.dailyselfie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	private static final int ACTION_TAKE_A_SELFIE = 0;
	private static final String SELFIE_KEY = "GET_SELFIES";
	private static final String SELFIE_FILES = "SELFIES";
	private static final String SELFIE_PATH = "PATHS";
	public static final String SHOW_SELFIE = "SHOW_SELFIE";
	private Uri pathToSaveSelfie;
	private static ArrayList<String> selfiesURI = new ArrayList<String>();
	private ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// selfieList = (ListView) findViewById(R.);
		if (savedInstanceState == null) {
			deSerialize();
			AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, AlarmNotificationReceiver.class);
			PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0,
					intent, 0);

			// Set alarm with interval 2 min
			long interval = SystemClock.elapsedRealtime() + 60 * 2 * 1000;
			manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					interval, interval, alarmIntent);
		}
		adapter = new ListAdapter(this, R.layout.list_view_item, selfiesURI);
		checkFiles();
		setListAdapter(adapter);
		ListView listView = this.getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this, ShowSelfie.class);
				intent.putExtra(SHOW_SELFIE, selfiesURI.get((int) id));
				startActivity(intent);
			}
		});

	}

	private void checkFiles() {
		for (int i = 0; i < selfiesURI.size(); i++) {
			String path = selfiesURI.get(i);
			File file = new File(path);
			if (!file.exists()) {
				selfiesURI.remove(i);
			}
		}
		adapter.notifyDataSetChanged();
		serialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkFiles();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.camera) {
			File folder = new File(Environment.getExternalStorageDirectory(),
					"DailySelfie/"); // Folder where we will save our images
			if (!folder.exists()) {
				folder.mkdirs();
			}
			String fileName = new SimpleDateFormat("yyyyMMddhhmmSSS'.jpg'")
					.format(new Date()); // timestamp
			File selfie = new File(folder.getPath() + "/" + fileName);
			pathToSaveSelfie = Uri.fromFile(selfie); // file with name - current
														// date
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pathToSaveSelfie);
			startActivityForResult(cameraIntent, ACTION_TAKE_A_SELFIE);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACTION_TAKE_A_SELFIE && resultCode == RESULT_OK) {
			// if result of taking picture is good - add the path in the
			// ArrayList
			// and serialize it
			Uri uri = Uri.parse(pathToSaveSelfie.toString());
			String path = uri.getPath();
			selfiesURI.add(path);
			serialize();
			// update ListView
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putStringArrayList(SELFIE_KEY, selfiesURI);
		if (pathToSaveSelfie != null) {
			outState.putString(SELFIE_PATH, pathToSaveSelfie.toString());
		}	
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		selfiesURI = savedInstanceState.getStringArrayList(SELFIE_KEY);
		pathToSaveSelfie = Uri.parse(savedInstanceState.getString(SELFIE_PATH));
	}

	private void serialize() {
		try {
			FileOutputStream fos = openFileOutput(SELFIE_FILES,
					Context.MODE_PRIVATE);
			ObjectOutputStream os;

			os = new ObjectOutputStream(fos);
			os.writeObject(selfiesURI);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void deSerialize() {
		try {
			FileInputStream fis = openFileInput(SELFIE_FILES);
			ObjectInputStream is = new ObjectInputStream(fis);
			selfiesURI = (ArrayList<String>) is.readObject();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
