package com.example.videocallingusingwebrtc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videocallingusingwebrtc.databinding.ActivitySignUpBinding;
import com.example.videocallingusingwebrtc.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding  binding;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    FirebaseDatabase database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authWithIdPass(binding.emailIdEt.getText().toString(),binding.password.getText().toString());

            }
        });

    }
    void authWithIdPass(String email,String pass){

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            User user1 = new User(user.getUid(),"deepak ","mathura","");

                            database.getReference()
                                    .child("Profiles")
                                    .child(user.getUid()).setValue(user1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("Login",true);
                                                editor.apply();

                                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
//                                                p.setVisibility(View.GONE);
                                                finishAffinity();
                                            }
                                            else {
                                                Toast.makeText(SignUpActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                        else{
                            Toast.makeText(SignUpActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();

                            Log.d("LoginActivity","here is a error"+task.getException());
                        }

                    }
                });
//        AuthCredential credential = GoogleAuthProvider.getCredential(tokenId,null);
//        Log.d("LoginActivty","the token id is "+tokenId);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            Log.d("LoginActivity",user.toString());
//                        }
//                    }
//                });
    }
}