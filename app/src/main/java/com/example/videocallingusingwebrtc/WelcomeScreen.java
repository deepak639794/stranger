package com.example.videocallingusingwebrtc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videocallingusingwebrtc.databinding.ActivityWelcomeScreenBinding;

public class WelcomeScreen extends AppCompatActivity {

    private ActivityWelcomeScreenBinding binding ;
    SharedPreferences sharedPreferences ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        binding = ActivityWelcomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
                boolean isLogin = sharedPreferences.getBoolean("Login",false);
                if(isLogin)
                    startActivity(new Intent(WelcomeScreen.this,MainActivity.class));
                else
                    startActivity(new Intent(WelcomeScreen.this,LoginActivity.class));

                finish();

            }
        });

    }
}