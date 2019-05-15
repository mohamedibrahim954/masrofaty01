package com.medotech.masrofaty01.util;

import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class TransmitData extends JsonObjectRequest {
    public TransmitData(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }
}
