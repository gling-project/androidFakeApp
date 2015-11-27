package be.gling.android.view;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by florian on 27/11/15.
 */
public class GlingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            //connect to parse
            Parse.initialize(this, "qUEDet4Q24JkiXhWTELqJFHeZ6bbvEcLw6WfXy5p", "4iB0Ztu3UnYXdvyjb2TxlFWlsadRi4qK9cooVc8N");
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e) {

        }


    }
}
