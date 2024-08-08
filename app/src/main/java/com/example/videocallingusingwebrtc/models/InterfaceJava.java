package com.example.videocallingusingwebrtc.models;

import android.webkit.JavascriptInterface;

import com.example.videocallingusingwebrtc.CallingActivity;

public class InterfaceJava {
    CallingActivity callingActivity;

    public InterfaceJava(CallingActivity callingActivity){
        this.callingActivity = callingActivity;

    }
    @JavascriptInterface
    public void onPeerConnected(){
        callingActivity.onPeerConnected();
    }
}
