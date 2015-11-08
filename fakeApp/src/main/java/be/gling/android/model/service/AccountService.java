package be.gling.android.model.service;

import android.content.Context;
import android.content.SharedPreferences;

import be.gling.android.model.dto.MyselfDTO;

/**
 * Created by florian on 23/11/14.
 * used to store / load the authenticationKey
 */
public class AccountService {

    private final static String TYPE = "user";
    private final static String KEY = "key";

    public static void storeService(Context context, MyselfDTO loginSuccessDTO) {

        SharedPreferences sharedPref = context.getSharedPreferences(TYPE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY, loginSuccessDTO.getAuthenticationKey());
        editor.apply();
    }

    public static String getAuthenticationKey(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(TYPE, Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
        return sharedPref.getString(KEY, null);
    }

    public static void clearKey(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(TYPE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY);
        editor.apply();
    }
}
