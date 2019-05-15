package com.medotech.masrofaty01;

import com.medotech.masrofaty01.util.SqliteHandler;

import java.util.Map;

public class UserInfo {

    private static UserInfo userInfo;
    Map<String, String> infoMap;
    private String fullName, email, Authorization, currency;

    public UserInfo() {
    }

    public static UserInfo getInstance() {
        if (userInfo == null) {
            userInfo = new UserInfo();
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
}
