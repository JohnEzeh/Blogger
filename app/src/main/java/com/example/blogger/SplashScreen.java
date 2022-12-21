package com.example.blogger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blogger.connectioncheck.ConnectionReceiver;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity implements ConnectionReceiver.ReceiverListener{

    private static final int SPLASH_TIME_OUT = 3000;
    Button btn_try_again;
    TextView connectiontxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        connectiontxt = findViewById(R.id.connectiontxt);
        btn_try_again = findViewById(R.id.btn_try_again);
        checkConnection();

        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, SplashScreen.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void checkConnection(){

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new ConnectionReceiver(), intentFilter);
        ConnectionReceiver.Listener = this;
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        showSnackbar(isConnected);
    }

    private void showSnackbar(boolean isConnected) {
        if (isConnected){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        } else {
            connectiontxt.setText(R.string.noInternet);
            btn_try_again.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onNetworkChange(boolean isConnected) {
        showSnackbar(isConnected);
    }
}