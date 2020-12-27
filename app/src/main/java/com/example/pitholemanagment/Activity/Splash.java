package com.example.pitholemanagment.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.pitholemanagment.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread(){
            public void  run()
            {
                try {
                    sleep(3*1000);
                    Intent i=new Intent(Splash.this,Login.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }
}