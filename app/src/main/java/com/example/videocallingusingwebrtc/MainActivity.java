package com.example.videocallingusingwebrtc;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.videocallingusingwebrtc.databinding.ActivityMainBinding;
import com.example.videocallingusingwebrtc.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient client;
    FirebaseAuth mauth ;
    FirebaseDatabase database;
    User user;
    private ActivityMainBinding binding;
    String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mauth.getCurrentUser();
        database.getReference().child("Profiles")
                        .child(currentUser.getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        user = snapshot.getValue(User.class);
                                        Log.d("MainActivity",user.getProfile());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


        binding.findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()){
//                    Intent intent = new Intent(MainActivity.this,MatchingProfile.class);
//                    intent.putExtra("profile",user.getProfile());
//                    startActivity(intent);
                    startActivity(new Intent(MainActivity.this,MatchingProfile.class));
                }
                else {
                    aksPermission();

                }

            }
        });
        gso =new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        client = GoogleSignIn.getClient(this,gso);

        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }
                });
            }
        });



    }
    private boolean isPermissionGranted(){
        for(String permission : permissions){
            if(ActivityCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }

        }
        return true;
    }

    private void aksPermission() {
        ActivityCompat.requestPermissions(this,permissions,100);
    }
}