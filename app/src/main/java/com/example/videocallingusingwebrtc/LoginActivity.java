package com.example.videocallingusingwebrtc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.credentials.CredentialManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videocallingusingwebrtc.databinding.ActivityLoginBinding;
import com.example.videocallingusingwebrtc.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding ;
    GoogleSignInClient client ;
    int RC_SIGN_IN = 11;
    FirebaseAuth mAuth;
    ProgressBar p;
    SharedPreferences sharedPreferences;

    FirebaseDatabase database;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        EdgeToEdge.enable(this);

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });
        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("AIzaSyA0cKGNGzkwM9I1d9ZWR4RN1lyBfkwZrXY")
                        .requestEmail().build();

        client = GoogleSignIn.getClient(this,gso);


//        binding.signUpWithgoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                p = new ProgressBar(LoginActivity.this);
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.WRAP_CONTENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                );
//                params.gravity = Gravity.CENTER;
//                p.setLayoutParams(params);
//                p.setVisibility(View.VISIBLE);
//                Intent intent = client.getSignInIntent();
//                startActivityForResult(intent,RC_SIGN_IN);
//
//            }
//        });
        setContentView(binding.getRoot());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==RC_SIGN_IN){
            Log.d("LoginAcivity","Successfully login");
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            authWithIdPass();
//            GoogleSignInAccount account = task.getResult();
//            authWithgoogle(account.getIdToken());
//            try {
//                if(task.isSuccessful()){
//                    task.getResult(ApiException.class);
//                    FirebaseUser user =   mAuth.getCurrentUser();
//                    Log.d("LoginAcitiviy",user.getUid());
////                    User user1   = new User(user.getUid().toString(),user.getDisplayName(),user.getPhotoUrl().toString(),"-");
//                    navigationToSecondActivity();
//
//                }
//
//            }
//            catch (ApiException e){
//                Log.d("LoginActivity",e.toString());
//            }

        }
    }

    private void navigationToSecondActivity() {
//        finish();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    void authWithIdPass(){

        mAuth.createUserWithEmailAndPassword("defsfdfdedfpakdfgoydanka91@gmail.com","32232233")
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
                                                        p.setVisibility(View.GONE);
                                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                        finishAffinity();
                                                    }
                                                    else {
                                                        Toast.makeText(LoginActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                        }
                        else{
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