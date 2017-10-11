package com.example.albader.wirelesscontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Albader on 4/28/17.
 */

public class SplashScreen extends Activity {

    //Timer 10 seconds
    public static int SPLASH_TIMER = 3000;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, DeviceList.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIMER);
    }
}
