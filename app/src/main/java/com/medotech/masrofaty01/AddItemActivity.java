package com.medotech.masrofaty01;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    List<Category> outcomeCategoryList = new ArrayList<>();
    List<Category> incomeCategoryList = new ArrayList<>();
    List<String> outCategoryNameList = new ArrayList<>();
    List<String> inCategoryNameList = new ArrayList<>();
    private TextInputLayout nameInputLayout, priceInputLayout, notesInputLayout;
    private Spinner outComeSpinner, inComeSpinner;
    private LinearLayout outcomeLinearLayout, incomeLinearLayout;
    private String itemName, itemPrice, itemNotes;

    private String categoryId, categoryType, categoryName;
    private boolean isNew;
    private String itemUpdateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Bundle bundle = getIntent().getExtras();
        categoryId = bundle.getString("categoryId");
        categoryName = bundle.getString("categoryName");
        categoryType = bundle.getString("categoryType");
        isNew = bundle.getBoolean("isNew");

        nameInputLayout = findViewById(R.id.item_name_text_input);
        priceInputLayout = findViewById(R.id.item_price_text_input);
        notesInputLayout = findViewById(R.id.item_notes_text_input);

        outComeSpinner = findViewById(R.id.outcome_spinner);
        inComeSpinner = findViewById(R.id.income_spinner);

        outcomeLinearLayout = findViewById(R.id.outcome_linear_layout);
        incomeLinearLayout = findViewById(R.id.income_linear_layout);

        TextView titleTextView = findViewById(R.id.title_text_view_add_item_activity);
        titleTextView.setText(categoryName);

        Button addItemButton = findViewById(R.id.add_item_button);


        outcomeCategoryList = OutcomeTab.categoryList;

        for (int i = 0; i < outcomeCategoryList.size(); i++) {
            outCategoryNameList.add(i, outcomeCategoryList.get(i).getName());
        }

        incomeCategoryList = IncomeTab.categoryList;

        for (int i = 0; i < incomeCategoryList.size(); i++) {
            inCategoryNameList.add(i, incomeCategoryList.get(i).getName());
        }

        ArrayAdapter outArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, outCategoryNameList);
        outComeSpinner.setAdapter(outArrayAdapter);

        ArrayAdapter inArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, inCategoryNameList);
        inComeSpinner.setAdapter(inArrayAdapter);

        if (categoryType.equals("OUTCOME")) {
            outComeSpinner.setSelection(outCategoryNameList.indexOf(categoryName));
            outcomeLinearLayout.setVisibility(View.GONE);
        } else {
            if (categoryType.equals("INCOME")) {
                inComeSpinner.setSelection(inCategoryNameList.indexOf(categoryName));
                incomeLinearLayout.setVisibility(View.GONE);
            }
        }

        if (!isNew) {
            itemUpdateId = bundle.getString("itemId");
            String itemUpdateName = bundle.getString("itemName");
            String itemUpdatePrice = bundle.getString("itemPrice");
            String itemUpdateNotes = bundle.getString("itemNotes");
            String itemUpdateIncomeName = bundle.getString("itemIncomeName");
            String itemUpdateOutcomeName = bundle.getString("itemOutcomeName");

            addItemButton.setText(R.string.update_item);
            outcomeLinearLayout.setVisibility(View.VISIBLE);
            incomeLinearLayout.setVisibility(View.VISIBLE);
            nameInputLayout.getEditText().setText(itemUpdateName);
            priceInputLayout.getEditText().setText(itemUpdatePrice);
            notesInputLayout.getEditText().setText(itemUpdateNotes);
            inComeSpinner.setSelection(inCategoryNameList.indexOf(itemUpdateIncomeName));
            outComeSpinner.setSelection(outCategoryNameList.indexOf(itemUpdateOutcomeName));

        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        gotoItemsActivity();

    }

    public void gotoItemsActivity() {
        Intent intent = new Intent(getApplicationContext(), ItemsActivity.class);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("categoryType", categoryType);
        startActivity(intent);
        finish();
    }

    public void addItem(View view) {

        if (checkTextFields()) {
            String outcomeCategoryId = outcomeCategoryList.get(outComeSpinner.getSelectedItemPosition()).getId();
            String incomeCategoryId = incomeCategoryList.get(inComeSpinner.getSelectedItemPosition()).getId();
            String id;
            if (!isNew) {
                id = itemUpdateId;
            } else {
                id = "0";
            }
            addItem(id, itemName, itemNotes, itemPrice, incomeCategoryId, outcomeCategoryId);
        }
    }

    private boolean checkTextFields() {
        itemName = nameInputLayout.getEditText().getText().toString().trim();
        itemPrice = priceInputLayout.getEditText().getText().toString().trim();
        itemNotes = notesInputLayout.getEditText().getText().toString().trim();

        if (itemName.isEmpty()) {
            nameInputLayout.setErrorEnabled(true);
            nameInputLayout.setError("Name Can't Be Empty !!");
            return false;
        } else {
            nameInputLayout.setErrorEnabled(false);
        }

        if (itemPrice.isEmpty()) {
            priceInputLayout.setErrorEnabled(true);
            priceInputLayout.setError("Name Can't Be Empty !!");
            return false;
        } else {
            priceInputLayout.setErrorEnabled(false);
        }

        if (itemNotes.isEmpty()) {
            notesInputLayout.setErrorEnabled(true);
            notesInputLayout.setError("Name Can't Be Empty !!");
            return false;
        } else {
            notesInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public void addItem(String id, String name, String notes, String price,
                        String incomeCategoryId, String outcomeCategoryId) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int responseCode = jsonObject.getInt("Code");
                    if (responseCode == 1) {
                        JSONObject item_object = jsonObject.getJSONObject("data");
                        //String id = item_object.getString("Id");
                        //String name = item_object.getString("Name");
                        //String price = item_object.getString("Price");
                        //String notes = item_object.getString("Notes");

                        gotoItemsActivity();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        TransferData transferData = new TransferData(Request.Method.POST, ServerURL.ITEMS__URL, responseListener, null) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<>();
                //headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", UserInfo.getInstance().getAuthorization());
                return headerMap;
            }
        };
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("Id", id);
        dataMap.put("Name", name);
        dataMap.put("Notes", notes);
        dataMap.put("Price", price);
        dataMap.put("InComeCategoryId", incomeCategoryId);
        dataMap.put("OutComeCategoryId", outcomeCategoryId);
        transferData.setDataMap(dataMap);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);

    }
}
