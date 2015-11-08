package be.gling.android.view.activity.technical;

import android.app.Activity;
import android.view.View;

import be.gling.android.view.activity.MainActivity;
import be.gling.android.R;

/**
 * Created by florian on 29/11/14.
 */
public abstract class AbstractActivity extends Activity {

    public static final Class MAIN_ACTIVITY = MainActivity.class;


    public void errorMessageClose(View v) {
        if (findViewById(R.id.error_message_container) != null) {
            findViewById(R.id.error_message_container).setVisibility(View.GONE);
        }
    }
}
