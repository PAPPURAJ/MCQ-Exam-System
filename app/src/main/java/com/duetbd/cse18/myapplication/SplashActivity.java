package com.duetbd.cse18.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {


    private ProgressBar loadingProgress;
    private Handler handler;
    private Runnable runnable;
    private int progress=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingProgress=findViewById(R.id.splashProgBar);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                loadingProgress.setProgress(progress++);

                if(progress>100){
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }else{
                    handler.postDelayed(runnable,10);
                }
            }
        };
        handler.post(runnable);
    }
}