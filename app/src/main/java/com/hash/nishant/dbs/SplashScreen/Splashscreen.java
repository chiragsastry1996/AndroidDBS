package com.hash.nishant.dbs.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.hash.nishant.dbs.First_entry.Login;
import com.hash.nishant.dbs.R;
import com.hash.nishant.dbs.Standard_Entry.QuickAccess;

public class Splashscreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    final String PREFS_EVENTS = "MyIntroFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        SharedPreferences prefs = getSharedPreferences("Version", MODE_PRIVATE);
        final String login = prefs.getString("login", "0");

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences(PREFS_EVENTS, 0);
                if (settings.getBoolean("my_first_time", true)) {
                    settings.edit().putBoolean("my_first_time", false).apply();
                    Intent intent = new Intent(Splashscreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(login.equals("true")) {

                        Intent intent = new Intent(Splashscreen.this, QuickAccess.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(Splashscreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }
}

