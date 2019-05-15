package com.medotech.masrofaty01.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnection {
    private Context context;

    public CheckInternetConnection(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
