package com.medotech.masrofaty01;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.SqliteHandler;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {

    private static UserInfo userInfo;
    private Context context;
    Map<String, String> infoMap;
    private String fullName, email, Authorization, currency, userMoney;

    public UserInfo(Context context) {
        this.context = context;
    }

    public static UserInfo getInstance(Context context) {
        if (userInfo == null) {
            userInfo = new UserInfo(context);
        }
        return userInfo;
    }

    public void addInfoMap(Map<String, String> userMap) {
        this.infoMap = userMap;
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public void addInfo(String fullName, String email, String authorization) {
        this.fullName = fullName;
        this.email = email;
        this.Authorization = authorization;
    }

    public String getFullName() {
        if (fullName == null) {
            if (infoMap != null) {
                fullName = infoMap.get(SqliteHandler.KEY_NAME);
            }
        }
        return fullName;
    }

    public String getEmail() {
        if (email == null) {
            if (infoMap != null) {
                email = infoMap.get(SqliteHandler.KEY_EMAIL);
            }
        }
        return email;
    }

    public String getAuthorization() {
        if (Authorization == null) {
            if (infoMap != null) {
                Authorization = infoMap.get(SqliteHandler.KEY_AUTHORIZATION);
            }
        }
        return Authorization;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        System.out.println("user Money: " + userMoney + " " + currency);
        this.userMoney = userMoney;
    }

    public void getAllUserInfo() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int responseCode = jsonObject.getInt("Code");
                    String requestDetails = jsonObject.getString("RequstDetails");
                    if (responseCode == 1) {
                        email = jsonObject.getString("Email");
                        fullName = jsonObject.getString("FullName");
                        String currency = jsonObject.getString("Concurancey");
                        setCurrency(currency);
                        String userMoney = jsonObject.getString("UserMoney");
                        setUserMoney(userMoney);
                        HomeActivity.displayUserInfo(fullName, email, currency, userMoney);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        TransferData transferData = new TransferData(Request.Method.GET, ServerURL.GET_USER_INFO_URL, responseListener, null) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                //headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", getAuthorization());
                return headerMap;
            }
        };

        transferData.setDataMap(null);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(context).getRequestQueue();
        MainRequestQueue.getInstance(context).addToRequestQueue(transferData);

    }
}
