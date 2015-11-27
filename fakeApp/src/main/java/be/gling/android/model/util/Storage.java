package be.gling.android.model.util;

import android.content.Context;

import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.util.Date;

import be.gling.android.model.dto.AccountDTO;
import be.gling.android.model.dto.MyselfDTO;
import be.gling.android.model.service.AccountService;

/**
 * Created by florian on 11/11/14.
 * Store data
 */
public class Storage {

    private final static long MAX_DELAY = 30 * 60 * 1000;

    private static Long userId;
    private static Date lastLoading;
    private static AccountDTO account;
    private static String authenticationKey;


    public static void store(Context context, MyselfDTO connection) {

        account = connection;

        authenticationKey = connection.getAuthenticationKey();

        AccountService.storeService(context, connection);
        userId = account.getId();
        lastLoading = new Date();


        //add userId to parse
        ParseInstallation currentInstallation = ParseInstallation.getCurrentInstallation();
        ParsePush.subscribeInBackground("user"+account.getId());
//        currentInstallation.add("channels", account.getId());
//        currentInstallation.saveInBackground();
//
//        String installationId = currentInstallation.getInstallationId();
//
//        Object userId2 = ParseInstallation.getCurrentInstallation().get("channels");
//        Number channels = ParseInstallation.getCurrentInstallation().getNumber("channels");
//
//
//
//        int i = 0;

    }

    public static boolean isConnected() {
        return account != null;
    }

    public static void clean(Context context) {
        account = null;
        authenticationKey = null;
        AccountService.clearKey(context);
    }

    public static boolean testStorage() {
        if (account == null) {
            return false;
        }
        return true;
    }


    /*
     * authentication key
     */
    public static String getAuthenticationKey() {
        return authenticationKey;
    }


    public static void setAuthenticationKey(String authenticationKey) {
        Storage.authenticationKey = authenticationKey;
    }


    public static AccountDTO getAccount() {
        return account;
    }

    public static Long getUserId() {
        return userId;
    }
}

