package bd.com.infobox.browser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutBrowser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_browser_activity);


        //Back Indicator About Activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

}
