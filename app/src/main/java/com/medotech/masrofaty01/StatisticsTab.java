package com.medotech.masrofaty01;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsTab extends Fragment {

    static List<MonthInfo> monthInfoList = new ArrayList<>();
    TextView monthTextView, yearTextView, moneyTextView, budgetTextView;

    public StatisticsTab() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                monthInfoList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray categories_jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < categories_jsonArray.length(); i++) {
                        JSONObject c = categories_jsonArray.getJSONObject(i);
                        String month = c.getString("Month");
                        String year = c.getString("Year");
                        String money = c.getString("Money");
                        String budget = c.getString("Budget");
                        MonthInfo monthInfo = new MonthInfo(month, year, money, budget);
                        monthInfoList.add(monthInfo);
                    }

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    //displayMonthInfo(monthInfoList.get(month));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        TransferData transferData = new TransferData(Request.Method.GET, ServerURL.ALL_MONTHS_STATISTICS_URL, responseListener, null) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                //headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", UserInfo.getInstance(getActivity().getApplicationContext()).getAuthorization());
                return headerMap;
            }
        };
        transferData.setDataMap(null);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getActivity().getApplicationContext()).addToRequestQueue(transferData);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistics_tab, container, false);
        monthTextView = rootView.findViewById(R.id.month_text_view);
        yearTextView = rootView.findViewById(R.id.year_text_view);
        moneyTextView = rootView.findViewById(R.id.money_text_view);
        budgetTextView = rootView.findViewById(R.id.budget_text_view);

        return rootView;
    }

    private void displayMonthInfo(MonthInfo monthInfo) {
        monthTextView.setText(monthInfo.money);
        yearTextView.setText(monthInfo.year);
        moneyTextView.setText(monthInfo.money);
        budgetTextView.setText(monthInfo.budget);
    }

    private class MonthInfo {
        public String month;
        public String year;
        public String money;
        public String budget;

        public MonthInfo(String month, String year, String money, String budget) {
            this.month = month;
            this.year = year;
            this.money = money;
            this.budget = budget;
        }
    }
}
