package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.content.Context;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Yoo on 2016-11-28.
 */

public class BaseActivity extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
