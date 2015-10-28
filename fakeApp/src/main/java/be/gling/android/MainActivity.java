package be.gling.android;

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
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends Activity {

    /* URL saved to be loaded after fb login */
    private static final String target_url        = "https://www.gling.be/";
    private static final String target_url_prefix = "gling.be";
    private Context      mContext;
    private LinearLayout loadingImage;
    private WebView      mWebview;
    private FrameLayout  mContainer;
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
//        mWebview.setWebChromeClient(new UriChromeClient());

        mWebview.loadUrl(target_url);
        mContext = this.getApplicationContext();

    }

    @Override
    protected void onStart() {
        start(false);
    }

    private void start(boolean reload) {
        if (!isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(be.gling.android.R.string.notConnectedDescr);
            // Add the buttons
            builder.setPositiveButton(be.gling.android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    start(true);
                    dialog.cancel();
                }
            });

            builder.create();
            builder.show();
        }
        else if(reload){
            mWebview.loadUrl(target_url);
        }
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
            } else if (url.contains(target_url_prefix)
                    || url.contains("/dialog/oauth")
                    || url.contains("m.facebook.com/login")) {
                view.loadUrl(url);
                return false;
            } else {
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

//    class UriChromeClient extends WebChromeClient {
//
//        @Override
//        public boolean onCreateWindow(WebView view, boolean isDialog,
//                                      boolean isUserGesture, Message resultMsg) {
////            mWebviewPop = new WebView(mContext);
////            mWebviewPop.setVerticalScrollBarEnabled(false);
////            mWebviewPop.setHorizontalScrollBarEnabled(false);
////            mWebviewPop.setWebViewClient(new UriWebViewClient());
////            mWebviewPop.getSettings().setJavaScriptEnabled(true);
////            mWebviewPop.getSettings().setSavePassword(false);
//
////            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
////                    ViewGroup.LayoutParams.MATCH_PARENT);
////            layoutParams.setMargins(20, 100, 20, 100);
//
////            mWebviewPop.setLayoutParams(layoutParams);
////            mContainer.addView(mWebviewPop);
////            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
////            transport.setWebView(mWebviewPop);
//            resultMsg.sendToTarget();
//
//            return true;
//        }
//
//    }

//    class UriChromeClient extends WebChromeClient {
//
//        @Override
//        public boolean onCreateWindow(WebView view, boolean isDialog,
//                                      boolean isUserGesture, Message resultMsg) {
////            mWebviewPop = new WebView(mContext);
////            mWebviewPop.setVerticalScrollBarEnabled(false);
////            mWebviewPop.setHorizontalScrollBarEnabled(false);
////            mWebviewPop.setWebViewClient(new UriWebViewClient());
////            mWebviewPop.getSettings().setJavaScriptEnabled(true);
////            mWebviewPop.getSettings().setSavePassword(false);
//
////            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
////                    ViewGroup.LayoutParams.MATCH_PARENT);
////            layoutParams.setMargins(20, 100, 20, 100);
//
////            mWebviewPop.setLayoutParams(layoutParams);
////            mContainer.addView(mWebviewPop);
////            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
////            transport.setWebView(mWebviewPop);
//            resultMsg.sendToTarget();
//
//            return true;
//        }
//
//    }

}
