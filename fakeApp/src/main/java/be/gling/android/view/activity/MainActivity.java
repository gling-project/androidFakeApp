package be.gling.android.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.*;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.HashMap;
import java.util.Map;

import be.gling.android.model.dto.MyselfDTO;
import be.gling.android.model.util.Storage;
import be.gling.android.model.util.externalRequest.WebClient;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends Activity {

    /* URL saved to be loaded after fb login */
    private static final String TARGET_URL = WebClient.TARGET_URL;
    private static final String LOGOUT_PATH = "rest/logout";
    private static final String TARGET_URL_PREFIX = WebClient.TARGET_URL_BASE;
    private static final String URL_PARAM = "url";
    private Context mContext;
    private LinearLayout loadingImage;
    private WebView mWebview;
    private FrameLayout mContainer;
    private long mLastBackPressTime = 0;
    private Toast mToast;
    private boolean onLoadingMode = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(be.gling.android.R.layout.activity_urimalo);
        // final View controlsView =
        // findViewById(R.id.fullscreen_content_controls);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        mWebview = (WebView) findViewById(be.gling.android.R.id.webview);
        //mWebviewPop = (WebView) findViewById(R.id.webviewPop);
        mContainer = (FrameLayout) findViewById(be.gling.android.R.id.webview_frame);
        loadingImage = (LinearLayout) findViewById(be.gling.android.R.id.loading_image);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setSupportMultipleWindows(false);
//        mWebview.setWebViewClient(new WebViewClient());
        mWebview.setWebViewClient(new UriWebViewClient());
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        String authenticationKey = Storage.getAuthenticationKey();

        Map<String, String> additionalHttpHeaders = new HashMap<>();
        additionalHttpHeaders.put("authenticationKey", authenticationKey);

        mWebview.loadUrl(TARGET_URL, additionalHttpHeaders);
        mContext = this.getApplicationContext();

    }

    @Override
    protected void onStart() {
        start(false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString(URL_PARAM, mWebview.getUrl());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        mWebview.loadUrl(savedInstanceState.getString(URL_PARAM));
    }

    private void start(boolean reload) {
        if (!isOnline()) {
            mWebview.setVisibility(View.GONE);
            loadingImage.setVisibility(View.VISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(be.gling.android.R.string.notConnectedDescr);
            // Add the buttons
            builder.setPositiveButton(be.gling.android.R.string.g_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    start(true);
                    dialog.cancel();
                }
            });

            builder.create();
            builder.show();
        }
        else if(mWebview.getVisibility() == View.GONE){
            mWebview.loadUrl(TARGET_URL);
        }
//        else if (reload) {
//            mWebview.loadUrl(TARGET_URL);
//        }
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private class UriWebViewClient extends WebViewClient {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest webResourceRequest)
        {

            String s = webResourceRequest.getUrl().toString();

            if (s.contains(TARGET_URL + LOGOUT_PATH)) {
                //logout facebook
                if(AccessToken.getCurrentAccessToken()!=null) {
                    LoginManager.getInstance().logOut();
                }
                //go to login activity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Storage.clean(MainActivity.this);
                finish();
            }

            return null;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("mailto:")) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(i);
            } else if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse(url));
                startActivity(intent);
            } else if (url.startsWith("maps:")) {
                Intent intent = new Intent(Intent.CATEGORY_APP_MAPS,
                        Uri.parse(url));
                startActivity(intent);
            } else if (url.contains(TARGET_URL_PREFIX)
                    || url.contains("/dialog/oauth")
                    || url.contains("m.facebook.com/login")) {
                view.loadUrl(url);
                return false;
            }  else {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("tttt", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (onLoadingMode) {
                mWebview.setVisibility(View.VISIBLE);
                loadingImage.setVisibility(View.GONE);
            }


            super.onPageFinished(view, url);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
