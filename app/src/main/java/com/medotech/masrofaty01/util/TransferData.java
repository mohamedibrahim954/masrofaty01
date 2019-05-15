package com.medotech.masrofaty01.util;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class TransferData extends StringRequest {

    Map<String, String> dataMap;

    public TransferData(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return dataMap;
    }


}
