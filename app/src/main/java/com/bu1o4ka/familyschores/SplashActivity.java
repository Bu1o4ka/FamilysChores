package com.bu1o4ka.familyschores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);

        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            else {
                if((mSettings.getString("familyCode", ""))==""){
                    startActivity( new Intent(SplashActivity.this, FamilyActivity.class));
                } else {
                    startActivity( new Intent(SplashActivity.this, MainActivity.class));
                }

            }
            finish();
        }, 2000); // Задержка на 2 секунды
    }
}
