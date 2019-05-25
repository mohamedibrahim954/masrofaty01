package com.medotech.masrofaty01;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Currency {
    private Context context;
    private ArrayList<HashMap<String, Object>> concuranceysArrayList;
    private List<String> currencies;

    public Currency(Context context) {
        this.context = context;
    }

    public List<String> getCurrencies() {
        concuranceysArrayList = new ArrayList<>();
        currencies = new ArrayList<>();
        currencies.add(0, "Select Currency");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray concuranceys = null;
                try {
                    concuranceys = new JSONArray(response);
                    for (int i = 0; i < concuranceys.length(); i++) {
                        JSONObject c = concuranceys.getJSONObject(i);
                        int id = c.getInt("Id");
                        String country = c.getString("Country");
                        String name = c.getString("Name");
                        HashMap<String, Object> concurancey = new HashMap<>();
                        concurancey.put("Id", id);
                        concurancey.put("Country", country);
                        concurancey.put("Name", name);
                        currencies.add(name);
                        System.out.println(country);
                        concuranceysArrayList.add(concurancey);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        TransferData transferData = new TransferData(Request.Method.GET, ServerURL.GET_ALL_CURRENCY_URL, responseListener, null);
        transferData.setDataMap(null);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(context).getRequestQueue();
        MainRequestQueue.getInstance(context).addToRequestQueue(transferData);
        return currencies;
    }

}
