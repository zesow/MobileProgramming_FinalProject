package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class MainActivity extends Activity implements SensorEventListener{

	private TextView txtName;
	private Button btnLogout;

	private SQLiteHandler db;
	private SessionManager session;

	private SensorManager mSensorManager;
	private Sensor mProximity;

	private TextView tv;
	private int count=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtName = (TextView) findViewById(R.id.name);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		tv=(TextView)findViewById(R.id.textView2);

		findViewById(R.id.button).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								RecordActivity.class);
						startActivity(intent);
					}
				}
		);

		findViewById(R.id.button2).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						//저장(insert)
					}
				}
		);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");

		// Displaying the user details on the screen
		txtName.setText(name+"님 안녕하세요!");

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
	}


	public final void onSensorChanged(SensorEvent event) {
		float distance = event.values[0];
		// Do something with this sensor data.
		String s = ""+distance;
		//Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
		if(distance==0.0){
			count++;
			tv.setText(String.valueOf(count));
		}

	}

	@Override
	protected void onResume() {
		// Register a listener for the sensor.
		super.onResume();
		mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
}
