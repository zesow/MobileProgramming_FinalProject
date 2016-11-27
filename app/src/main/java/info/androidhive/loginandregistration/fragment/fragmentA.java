package info.androidhive.loginandregistration.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.activity.MainActivity;
import info.androidhive.loginandregistration.activity.RecordActivity;

/**
 * Created by Yoo on 2016-11-27.
 */

public class FragmentA extends Fragment {


    public FragmentA() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_a, container, false);

        v.findViewById(R.id.button3).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
        );

        return v;
    }

}


