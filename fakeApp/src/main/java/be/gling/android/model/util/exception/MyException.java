package be.gling.android.model.util.exception;

import android.util.Log;

/**
 * Created by florian on 11/11/14.
 * Used to display exception
 */
public class MyException extends Exception {

    public MyException(String detailMessage) {
        super(detailMessage);
        Log.e("MyException", detailMessage + " ");
    }
}
