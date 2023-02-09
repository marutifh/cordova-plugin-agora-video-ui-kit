package com.marutifh.agoravideo;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.util.Log;

import io.agora.rtc2.Constants;
import io.agora.agorauikit_android.*;

/**
 * This class echoes a string called from JavaScript.
 */
public class AgoraVideoUIKit extends CordovaPlugin {

    private Activity activity;
    private Context context;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = cordova.getActivity();
        context = webView.getContext();
    }

    // Object of AgoraVideoViewer class
    private AgoraVideoViewer agView = null;

    // Fill the App ID of your project generated on Agora Console.
    private String appId = "";

    // Fill the channel name.
    private String channelName = "";

    // Fill the temp token generated on Agora Console.
    private String token = "";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }

        if (action.equals("initializeAndJoinChannel")) {
            this.appId = args.getString(0);
            this.token = args.getString(1);
            this.channelName = args.getString(2);
            this.initializeAndJoinChannel(callbackContext);
            return true;
        }

        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void initializeAndJoinChannel(CallbackContext callbackContext) {
        Runnable runnable = new Runnable() {
            public void run() {
                // Create AgoraVideoViewer instance
                try {
                    agView = new AgoraVideoViewer(context, new AgoraConnectionData(appId, token),
                            AgoraVideoViewer.Style.FLOATING, new AgoraSettings(), null);
                } catch (Exception e) {
                    Log.e("AgoraVideoViewer",
                            "Could not initialize AgoraVideoViewer. Check that your app Id is valid.");
                    Log.e("Exception", e.toString());
                    return;
                }

                // Add the AgoraVideoViewer to the Activity layout
                activity.addContentView(agView, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
                try{
                    joinChannel();
                    String str = "appId::" +  appId +  " token::" + token;
                    callbackContext.success(str);
                } catch (Exception e) {
                    Log.e("AgoraVideoViewer",
                            "Could not initialize joinChannel method. Check that your app Id is valid.");
                    Log.e("Exception", e.toString());
                    callbackContext.error(e.getMessage());
                    return;
                }
            };
        };
        this.cordova.getActivity().runOnUiThread(runnable);
        // Check permission and join a channel
        // if (DevicePermissionsKt.requestPermissions(AgoraVideoViewer.Companion, this))
        // {
        // joinChannel();
        // callbackContext.success(message);
        // } else {
        // Button joinButton = new Button(this);
        // joinButton.setText("Allow camera and microphone access, then click here");
        // joinButton.setOnClickListener(new View.OnClickListener() {
        // // When the button is clicked, check permissions again and join channel
        // @Override
        // public void onClick(View view) {
        // if (DevicePermissionsKt.requestPermissions(AgoraVideoViewer.Companion,
        // getApplicationContext())) {
        // ((ViewGroup) joinButton.getParent()).removeView(joinButton);
        // joinChannel();
        // callbackContext.success(message);
        // }
        // }
        // });
        // this.addContentView(joinButton, new
        // FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 200));
        // }
    }

    public void joinChannel() {
        agView.join(channelName, token, Constants.CLIENT_ROLE_BROADCASTER, 0);
    }
}
