package com.example.videocallingusingwebrtc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videocallingusingwebrtc.databinding.ActivityMainBinding;
import com.example.videocallingusingwebrtc.databinding.ActivityMatchingProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.xml.transform.sax.SAXSource;

public class MatchingProfile extends AppCompatActivity {

    ActivityMatchingProfileBinding binding ;
    FirebaseAuth mAuth;
    boolean isOkay=false;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

        binding = ActivityMatchingProfileBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        String userName = mAuth.getUid();


        database.getReference().child("User")
                .orderByChild("status")
                .equalTo(0).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.getChildrenCount()>0){
                            Log.d("MatchingProfile","room is created");
                            //Room Available
                            isOkay= true;
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                database.getReference()
                                        .child("User")
                                        .child(snapshot1.getKey())
                                        .child("incoming")
                                        .setValue(userName);
                                database.getReference()
                                        .child("User")
                                        .child(snapshot1.getKey())
                                        .child("status")
                                        .setValue(1);
                                Intent intent = new Intent(MatchingProfile.this,CallingActivity.class);
                                String incoming = snapshot1.child("incoming").getValue(String.class);
                                String createdBy = snapshot1.child("createdBy").getValue(String.class);
                                boolean isAvailable = snapshot1.child("isAvailable").getValue(Boolean.class);
                                intent.putExtra("username",userName);
                                intent.putExtra("incoming",incoming);
                                intent.putExtra("isAvailable",isAvailable);
                                intent.putExtra("createdBy",createdBy);
                                startActivity(intent);
                            }
                        }
                        else {
                            HashMap<String, Object> room = new HashMap<>();
                            room.put("incoming",userName);
                            room.put("createdBy",userName);
                            room.put("isAvailable",false);
                            room.put("status",0);
                            database.getReference().child("User")
                                    .child(userName)
                                    .setValue(room)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference()
                                                    .child("User")
                                                    .child(userName)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.child("status").exists()){
                                                                if(snapshot.child("status").getValue(Integer.class)==1){
                                                                    if(isOkay) return;
                                                                    isOkay = true;
                                                                    Intent intent = new Intent(MatchingProfile.this,CallingActivity.class);
                                                                    String incoming = snapshot.child("incoming").getValue(String.class);
                                                                    String createdBy = snapshot.child("createdBy").getValue(String.class);
                                                                    boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                                                                    intent.putExtra("username",userName);
                                                                    intent.putExtra("incoming",incoming);
                                                                    intent.putExtra("isAvailable",isAvailable);
                                                                    intent.putExtra("createdBy",createdBy);
                                                                    startActivity(intent);

                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }
}