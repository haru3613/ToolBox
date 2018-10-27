package com.cyut.toolbox.toolbox;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.qiscus.sdk.service.QiscusFirebaseService;

public class MyFirebaseMessagingService extends QiscusFirebaseService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (QiscusFirebaseService.handleMessageReceived(remoteMessage)) { // For qiscus
            Log.e("message", "onMessageReceived");
            return;
        }

        //Your FCM PN here
    }
}