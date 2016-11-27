package info.androidhive.loginandregistration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Button;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.fragment.FragmentA;
import info.androidhive.loginandregistration.fragment.FragmentB;
import info.androidhive.loginandregistration.fragment.FragmentC;

public class RecordActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        Fragment fragmentA = new FragmentA();
        Fragment fragmentB = new FragmentB();
        Fragment fragmentC = new FragmentC();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add( R.id.fragment, fragmentA );
        fragmentTransaction.add( R.id.fragment2, fragmentB );
        fragmentTransaction.add( R.id.fragment3, fragmentC );

        fragmentTransaction.commit();

        findViewById(R.id.button5).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(RecordActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
