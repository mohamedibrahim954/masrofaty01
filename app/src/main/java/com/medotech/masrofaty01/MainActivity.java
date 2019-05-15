package com.medotech.masrofaty01;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView response_view;
    Button request_button;
    Spinner countrySpinner;
    ArrayList<HashMap<String, Object>> concuranceysArrayList = new ArrayList<>();
    List<String> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        response_view = findViewById(R.id.textView2);
        countrySpinner = findViewById(R.id.spinner2);
        request_button = findViewById(R.id.button);
        request_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HttpHandler httpHandler = new HttpHandler();
                try {
                    String response = httpHandler.request_get_data();
                    response_view.setText(response);
                    readConcuranceysJson(response);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countryList);
                    countrySpinner.setAdapter(arrayAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void readConcuranceysJson(String jsonStr) {
        if (jsonStr != null) {
            try {
                if (!concuranceysArrayList.isEmpty()) {
                    concuranceysArrayList.clear();
                }
                countryList = new ArrayList<>();

                // Getting JSON Array
                JSONArray concuranceys = new JSONArray(jsonStr);
                for (int i = 0; i < concuranceys.length(); i++) {
                    JSONObject c = concuranceys.getJSONObject(i);
                    int id = c.getInt("Id");
                    String country = c.getString("Country");
                    String name = c.getString("Name");
                    HashMap<String, Object> concurancey = new HashMap<>();
                    concurancey.put("Id", id);
                    concurancey.put("Country", country);
                    concurancey.put("Name", name);
                    countryList.add(country);
                    System.out.println(country);
                    concuranceysArrayList.add(concurancey);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}