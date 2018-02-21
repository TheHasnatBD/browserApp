package bd.com.infobox.browser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private static final int TIME_LIMIT = 1500;
    private static long backPressed;

    WebView takeURL;
    ProgressBar progressBar;
    EditText inputUrl;
    Button urlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        setTitle("");

        //Definition ... Common task
        inputUrl = (EditText) findViewById(R.id.enterURL);
        urlButton = (Button) findViewById(R.id.enterButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.GONE);

        takeURL = (WebView) findViewById(R.id.webView);



        if (savedInstanceState != null) {
            takeURL.restoreState(savedInstanceState);

        } else {
            //Web View Mechanism
            //takeURL.setWebViewClient(new ourViewClient());

            takeURL.getSettings().setJavaScriptEnabled(true);
            takeURL.getSettings().setSupportZoom(true);
            takeURL.getSettings().setBuiltInZoomControls(false);
            takeURL.getSettings().setUseWideViewPort(true);
            takeURL.getSettings().setLoadWithOverviewMode(true);
            takeURL.getSettings().setLoadsImagesAutomatically(true);

            takeURL.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            takeURL.setBackgroundColor(Color.WHITE);

            //Progressbar System
            takeURL.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    progressBar.setProgress(newProgress);
                    if (newProgress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                    }
                    if (newProgress == 100) {
                        progressBar.setVisibility(ProgressBar.GONE);

                    }
                }
            });


            //WebViewClient + Check Internet
            takeURL.setWebViewClient(new ourViewClient() {
                public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                    try {
                        webView.stopLoading();
                    } catch (Exception e) {
                    }

                    if (webView.canGoBack()) {
                        webView.goBack();
                    }

                    webView.loadUrl("about:blank");
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Opps !!");
                    alertDialog.setMessage("Check your Wi-Fi or, Data Connection and try again.\n\nOtherwise Check your web URL");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    });

                    alertDialog.show();
                    super.onReceivedError(webView, errorCode, description, failingUrl);
                }
            });


        }

            // Take User URL input
            urlButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Firebase Analytics
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "btn_click");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Url_Click");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    //Firebase Analytics End

                    //take URL input
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    takeURL.loadUrl("http://"+inputUrl.getText().toString());
                    inputUrl.setText("");
                }
            });
    }



    public class ourViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            CookieManager.getInstance().setAcceptCookie(true);
            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //Show Visited URL when Browsing
            inputUrl.setText(url);

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        takeURL.saveState(outState);
    }

    //Menu Option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    //Menu option item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //URL Back
            case R.id.back: {
                if(takeURL.canGoBack()){
                    takeURL.goBack();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                    toast.setText("No previous web page");
                    toast.show();
                }
                return true;
            }
            //URL Forward
            case R.id.forward: {
                if(takeURL.canGoForward()){
                    takeURL.goForward();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                    toast.setText("No forward web page");
                    toast.show();
                }
                return true;
            }
            //Browser Home
            case R.id.home: {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                takeURL.loadUrl("https://www.google.com");
                inputUrl.setText("");

                return true;
            }

            //For Browser Optional Settings
            case R.id.about_dev: {
                    startActivity(new Intent(MainActivity.this, AboutBrowser.class));
                return true;
            }
            case R.id.settings: {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("Settings functionality will be added in next feature\n"+
                        "\nThanks for your patient :)");
                //alert.setCancelable(true);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

                return true;
            }
            case R.id.exit: {
                Toast toast = Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT);
                toast.setText("Exited Browser");
                toast.show();
                finish();
                return true;
            }
            //End Browser Optional Settings

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // This method is used to detect back button
    @Override
    public void onBackPressed() {
        if(takeURL.canGoBack()) {
            takeURL.goBack();
        }else {
            if(TIME_LIMIT + backPressed > System.currentTimeMillis()){
                super.onBackPressed();
                Toast.makeText(getApplicationContext(), "Exited", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Press back again to Exit", Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();
        }
    }//End Back button press for exit...


}
