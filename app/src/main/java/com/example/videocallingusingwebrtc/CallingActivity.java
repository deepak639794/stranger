package com.example.videocallingusingwebrtc;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.videocallingusingwebrtc.databinding.ActivityCallingBinding;
import com.example.videocallingusingwebrtc.databinding.ActivityMatchingProfileBinding;
import com.example.videocallingusingwebrtc.models.InterfaceJava;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CallingActivity extends AppCompatActivity {

    ActivityCallingBinding binding ;
    String uniqueId = "";
    FirebaseAuth  auth ;
    String userName;
    String friendUserName ;
    boolean isPeerConnected = false;
    DatabaseReference firebaseRef;
    boolean isAudio = true;
    boolean isVideo = true;
    String createdBy ;
    String incoming ;
    boolean pageExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallingBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseRef = FirebaseDatabase.getInstance().getReference().child("User");

        userName = getIntent().getStringExtra("username");
        incoming = getIntent().getStringExtra("incoming");
        createdBy = getIntent().getStringExtra("createdBy");


        friendUserName = "";
        if(incoming.equalsIgnoreCase(friendUserName)){
            friendUserName = incoming;
        }
        setUpWebView();
        binding.micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAudio = !isAudio;
                callJavaScriptFunction("javascript:toggleAudio(\""+isAudio+"\")");

                if(isAudio)
                binding.micButton.setImageResource(R.drawable.btn_unmute_normal);
                else binding.micButton.setImageResource(R.drawable.btn_mute_normal);
            }

        });
        binding.videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVideo =!isVideo;
                callJavaScriptFunction("javascript:toggleVideo(\""+isVideo+"\")");

                if(isVideo)
                    binding.videoButton.setImageResource(R.drawable.btn_video_normal);
                else binding.videoButton.setImageResource(R.drawable.btn_video_muted);
            }
        });

        uniqueId = auth.getUid();


    }
    void setUpWebView(){
        binding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
        binding.webView.getSettings().setJavaScriptEnabled(true);
//        binding.webView.getSettings().setAllowFileAccess(true);
//        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webView.addJavascriptInterface(new InterfaceJava(this),"Android");
        loadVideoCall();

    }
    public void loadVideoCall(){
        String filepath = "file:///android_asset/call.html";
        binding.webView.loadUrl(filepath);
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
               public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                initializePeer();

            }

              @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.d("CallingActivity",error.getDescription().toString());
            }
        });
    }
    void initializePeer(){
        Log.d("CallingActivity","initialising peer");
        callJavaScriptFunction("javascript:init(\""+uniqueId+"\")");
        if(createdBy.equalsIgnoreCase(userName)){
            firebaseRef.child(userName).child("connId").setValue(uniqueId);
            firebaseRef.child(userName).child("isAvailable").setValue(true);
//            binding.controls.setVisibility(View.VISIBLE);

        }
        else {
            Log.d("CallingActivity","in the else part");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    friendUserName = createdBy;


                    FirebaseDatabase.getInstance().getReference()
                            .child("User")
                            .child(friendUserName)
                            .child("connId")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){
                                        sendCallRequest();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            },2000);
        }

    }
    public void onPeerConnected(){
        isPeerConnected = true;
    }
    void sendCallRequest(){
        Log.d("CallingActivity","request connection has been sent");
        if(!isPeerConnected){
            Toast.makeText(this,"you are not connected. please connect to internet",Toast.LENGTH_SHORT).show();
            return;
        }

            listenConnId();

    }
    void listenConnId(){
        Log.d("CallingActivity","inside the listenconnId");
        firebaseRef.child(friendUserName).child("connId")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()==null)
                            return;
//                        binding.controls.setVisibility(View.VISIBLE);
                        String connId = snapshot.getValue(String.class);
                        callJavaScriptFunction("javascript:startCall(\""+connId+"\")");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    void callJavaScriptFunction(String function){
        Log.d("CallingActivity","java script function   "+function+"  is called");
        binding.webView.post(new Runnable() {
            @Override
            public void run() {
                binding.webView.evaluateJavascript(function,null);

            }
        });
    }
}