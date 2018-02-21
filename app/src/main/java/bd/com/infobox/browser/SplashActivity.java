package bd.com.infobox.browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        //Splash Time and 1st activity
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);

                    Intent intend = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(intend);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}

