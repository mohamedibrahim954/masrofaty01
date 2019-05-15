package com.medotech.masrofaty01;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateCategoryActivity extends AppCompatActivity {

    List<Integer> imageList = new ArrayList<>();
    private TextInputLayout categoryTextInputLayout, categoryMoneyInputLayout;
    private Spinner categoryImageSpinner;
    private String imageNamePrefix = "cash";
    private String categoryId, categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        Bundle bundle = getIntent().getExtras();
        categoryId = bundle.getString("id");
        categoryType = bundle.getString("type");
        String categoryName = bundle.getString("name");
        int categoryIcon = bundle.getInt("icon");
        String categoryMoney = bundle.getString("categoryMoney");

        System.out.println(categoryIcon);

        categoryTextInputLayout = findViewById(R.id.update_category_name_text_input);
        categoryMoneyInputLayout = findViewById(R.id.update_category_money_text_input);
        categoryImageSpinner = findViewById(R.id.update_category_pic_spinner);
        categoryTextInputLayout.getEditText().setText(categoryName);

        for (int i = 1; i <= 20; i++) {
            int resourceId = getResources().getIdentifier(imageNamePrefix + i, "drawable", getPackageName());
            imageList.add(i - 1, resourceId);
            System.out.println(getResources().getResourceEntryName(resourceId));
        }

        imageList.addAll(Arrays.asList(R.drawable.food, R.drawable.shopping, R.drawable.personal,
                R.drawable.car, R.drawable.kast, R.drawable.home, R.drawable.child,
                R.drawable.salary, R.drawable.saving, R.drawable.credit));


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_items, imageList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_items, parent, false);
                }
                ImageView imageView = convertView.findViewById(R.id.category_select_image);
                imageView.setImageResource(imageList.get(position));
                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_items, parent, false);
                }
                ImageView imageView = convertView.findViewById(R.id.category_select_image);
                imageView.setImageResource((Integer) getItem(position));
                return convertView;
            }
        };

        categoryImageSpinner.setAdapter(arrayAdapter);

        categoryImageSpinner.setSelection(imageList.indexOf(categoryIcon));

        LinearLayout categoryMoneyLinearLayout = findViewById(R.id.category_money_linear_layout);
        if (categoryType.equals("INCOME")) {
            categoryMoneyLinearLayout.setVisibility(View.VISIBLE);
            categoryMoneyInputLayout.getEditText().setText(categoryMoney);
            System.out.println(categoryMoney);
        } else {
            if (categoryType.equals("OUTCOME")) {
                categoryMoneyLinearLayout.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    public void updateCategory(View view) {
        String URL = "";
        String categoryImage;
        String categoryMoney = null;


        int imagePosition = categoryImageSpinner.getSelectedItemPosition();

        categoryImage = getResources().getResourceEntryName(imageList.get(imagePosition));

        String categoryName = categoryTextInputLayout.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(categoryName)) {
            categoryTextInputLayout.setErrorEnabled(true);
            categoryTextInputLayout.setError("Name Can't Be Empty !!");
            return;
        } else {
            categoryTextInputLayout.setErrorEnabled(false);
        }

        if (categoryType.equals("INCOME")) {
            URL = ServerURL.GET_ALL_INCOME_CATEGORIES_URL;
            categoryMoney = categoryMoneyInputLayout.getEditText().getText().toString().trim();
            if (TextUtils.isEmpty(categoryMoney)) {
                categoryMoneyInputLayout.setErrorEnabled(true);
                categoryMoneyInputLayout.setError("Money Can't Be Empty !!");
                return;
            } else {
                categoryMoneyInputLayout.setErrorEnabled(false);
            }

        } else {
            if (categoryType.equals("OUTCOME")) {
                URL = ServerURL.GET_ALL_OUTCOME_CATEGORIES_URL;
            }
        }

        requestUpdateCategory(URL, categoryName, categoryImage, categoryMoney);

    }

    private void requestUpdateCategory(String url, String categoryName, String categoryImage, String categoryMoney) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int resposeCode = jsonObject.getInt("Code");
                    String requestDetails = jsonObject.getString("RequstDetails");
                    if (resposeCode == 1) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();


                    }

                    Toast.makeText(getApplicationContext(), requestDetails, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        TransferData transferData = new TransferData(Request.Method.POST, url, responseListener, null) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<>();
                //headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", UserInfo.getInstance().getAuthorization());
                return headerMap;
            }
        };
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("id", categoryId);
        dataMap.put("Name", categoryName);
        dataMap.put("Icon", categoryImage);
        if (categoryType.equals("INCOME")) {
            dataMap.put("Price", categoryMoney);
        }
        transferData.setDataMap(dataMap);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);

    }
}
