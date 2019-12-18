package com.example.splashapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splashapplication.R;
import com.example.splashapplication.Halper.SessionManager;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    SessionManager sessionManager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context=this;
        sessionManager = new SessionManager(getApplicationContext());
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!((Activity) context).isFinishing())
                {
                    sessionManager.checkLogin();
                }
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
            }
        },1500);
    }
}
