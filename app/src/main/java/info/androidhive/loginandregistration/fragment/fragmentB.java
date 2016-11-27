package info.androidhive.loginandregistration.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.loginandregistration.R;

public class FragmentB extends Fragment {

    public FragmentB() {
        // Required empty public constructor
    }

    String myJSON;
    TextView text;
    JSONArray peoples = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_b, container, false);

        text = (TextView) v.findViewById(R.id.textView5);
        getData("http://54.164.7.137/android_login_api/getbest.php");

        return v;
    }

    protected void showMax(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);

            peoples = jsonObj.getJSONArray("result");
            JSONObject c = peoples.getJSONObject(0);
            String max=c.getString("max");
            text.setText(String.valueOf(max));
            //Toast.makeText(getActivity(),max,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                //showMax();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }
}