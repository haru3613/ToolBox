package com.cyut.toolbox.toolbox;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.service.QiscusFirebaseIdService;

import static com.android.volley.VolleyLog.TAG;

public class MyFirebaseIdService extends QiscusFirebaseIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh(); // Must call super

        // Below is your own apps specific code
        // e.g register the token to your backend
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: "+refreshedToken);
        Qiscus.setFcmToken(refreshedToken);


    }
}