package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class MainActivity extends BaseActivity implements SensorEventListener{

	private TextView txtName;
	private Button btnLogout;

	private SQLiteHandler db;
	private SessionManager session;

	private SensorManager mSensorManager;
	private Sensor mProximity;

	private TextView tv;
	private int count=0;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtName = (TextView) findViewById(R.id.name);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		tv=(TextView)findViewById(R.id.textView2);



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

		name = user.get("name");

		findViewById(R.id.button).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {//기록 보여주기
						//Intent intent = new Intent(MainActivity.this,
						//		RecordActivity.class);
						//startActivity(intent);
						Intent intent = new Intent(MainActivity.this,
								NoFragmentActivity.class);
						intent.putExtra("myName",name);
						startActivity(intent);
					}
				}
		);

		// Displaying the user details on the screen
		txtName.setText(name+"님 안녕하세요!");

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});

		findViewById(R.id.button2).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						//저장(insert)
						registerScore(name,count);
						count=0;
						tv.setText(String.valueOf(count));
					}
				}
		);
	}

	private void registerScore(String name,int count){
		class InsertData extends AsyncTask<String, Void, String> {
			ProgressDialog loading;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loading = ProgressDialog.show(MainActivity.this, "기록 새기는 중..", null, true, true);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				loading.dismiss();
				Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
			}

			@Override
			protected String doInBackground(String... params) {

				try{
					String name = (String)params[0];
					String count = (String)params[1];

					String link="http://54.164.7.137/android_login_api/insert.php";
					String data  = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
					data += "&" + URLEncoder.encode("count", "UTF-8") + "=" + URLEncoder.encode(count, "UTF-8");

					URL url = new URL(link);
					URLConnection conn = url.openConnection();

					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

					wr.write( data );
					wr.flush();

					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					StringBuilder sb = new StringBuilder();
					String line = null;

					// Read Server Response
					while((line = reader.readLine()) != null)
					{
						sb.append(line);
						break;
					}
					return sb.toString();
				}
				catch(Exception e){
					return new String("Exception: " + e.getMessage());
				}

			}
		}

		InsertData task = new InsertData();
		task.execute(name,String.valueOf(count));

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
