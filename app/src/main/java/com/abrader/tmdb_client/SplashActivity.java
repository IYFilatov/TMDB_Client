package com.abrader.tmdb_client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        /*Intent intent = new Intent(this, RVActivity.class);
        startActivity(intent);
        finish();*/

        setContentView(R.layout.splash_layout);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, getSplashScreenDuration());


    }

    private long getSplashScreenDuration() {
        /*SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String prefKeyFirstLaunch = "pref_first_launch";
        long delay = 10;
        if (sp.getBoolean(prefKeyFirstLaunch, true)){
            delay = 3000;
        };*/
        long delay = 10;
        return delay;

    }
}
