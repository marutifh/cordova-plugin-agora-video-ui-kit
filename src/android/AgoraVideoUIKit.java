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
    private Integer uid = 555;

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
            this.uid = args.getInt(3);
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
                try {
                    // TODO: Check permissions then request join channel.
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
    }

    public void joinChannel() {
        agView.join(channelName, token, Constants.CLIENT_ROLE_BROADCASTER, uid);
    }
}
