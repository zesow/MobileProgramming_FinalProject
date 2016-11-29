package info.androidhive.loginandregistration.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class NoFragmentActivity extends BaseActivity {


    String myJSON;

    private static final String TAG_RESULTS="result";

    private static final String TAG_NAME = "name";
    private static final String TAG_ADD ="count";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;
    ArrayList<HashMap<String, String>> myList;
    HashMap<String,String> persons;

    ListView list;
    TextView maxView;
    TextView meanView;
    TextView myMeanView;
    TextView estimateView;

    int max=0;
    int sum=0;
    int cnt=0;
    int mySum=0;
    int myCnt=0;
    String bestMan;
    String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_fragment);

        list = (ListView) findViewById(R.id.listView);
        maxView= (TextView) findViewById(R.id.textViewScore);
        meanView= (TextView) findViewById(R.id.textViewMean);
        myMeanView= (TextView) findViewById(R.id.textViewMyMean);
        estimateView= (TextView) findViewById(R.id.textViewEstimate);

        //Intent intent=getIntent();
        //myName=intent.getStringExtra("myName");
        //Toast.makeText(this, myName, Toast.LENGTH_SHORT).show();

        personList = new ArrayList<HashMap<String,String>>();
        myList = new ArrayList<HashMap<String,String>>();
        getData("http://54.164.7.137/android_login_api/getdata.php");

    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);

                String name = c.getString(TAG_NAME);
                String count = c.getString(TAG_ADD);

                if(Integer.parseInt(count)>max){
                    max=Integer.parseInt(count);
                    bestMan=name;
                }

                sum+=Integer.parseInt(count);
                cnt++;

                persons = new HashMap<String,String>();


                persons.put(TAG_NAME,name);
                persons.put(TAG_ADD,count);

                personList.add(persons);
                //Toast.makeText(this, myName, Toast.LENGTH_SHORT).show();
                if(name.equals(myName)){
                    myList.add(persons);
                    mySum+=Integer.parseInt(count);
                    myCnt++;
                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    NoFragmentActivity.this, myList, R.layout.list_item,
                    new String[]{TAG_NAME,TAG_ADD},
                    new int[]{R.id.name, R.id.count}
            );

            list.setAdapter(adapter);

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
                Intent intent=getIntent();
                myName=intent.getStringExtra("myName");
                //Toast.makeText(this, myName, Toast.LENGTH_SHORT).show();
                showList();
                maxView.setText("최고 점수는 "+bestMan+" 님의 "+String.valueOf(max)+" 점!");
                meanView.setText("전체 평균은 "+String.valueOf(sum/cnt)+" 점!");
                myMeanView.setText("내 평균은 "+String.valueOf(mySum/myCnt)+" 점!");

                if((mySum/myCnt)>(sum/cnt)){
                    estimateView.setText("잘 하시고 계시네요!");
                }
                else{
                    estimateView.setText("좀 더 분발하세요!");
                }
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}
