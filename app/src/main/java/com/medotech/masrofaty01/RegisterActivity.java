package com.medotech.masrofaty01;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.medotech.masrofaty01.util.SessionManager;
import com.medotech.masrofaty01.util.SqliteHandler;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passEditText, budgetEditText;
    private Spinner beginDaySpinner, currenciesSpinner;
    private Switch budgetSwitch;
    private Button registerButton;
    private List<String> currenciesList = new ArrayList<String>();
    private int currencySelectedIndex;

    private FrameLayout progressBarHolder;

    private SessionManager session;
    private SqliteHandler db;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new SqliteHandler(getApplicationContext());

        session = new SessionManager(getApplicationContext());

        nameEditText = findViewById(R.id.fullName);
        emailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.password);

        budgetSwitch = findViewById(R.id.budgetSwitch);
        budgetEditText = findViewById(R.id.budgetValue);

        budgetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    budgetEditText.setVisibility(View.VISIBLE);
                } else {
                    budgetEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        beginDaySpinner = findViewById(R.id.beginDaySpinner);
        currenciesSpinner = findViewById(R.id.currencySpinner);

        registerButton = findViewById(R.id.register_button);

        progressBarHolder = findViewById(R.id.progress_circular_holder);

        Currency currency = new Currency(getApplicationContext());
        currenciesList = currency.getCurrencies();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, currenciesList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the spinner collapsed item (non-popup item) as a text view
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.BLUE);

                // Return the view
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Cast the drop down items (popup items) as text view
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);

                // Set the text color of drop down items
                tv.setTextColor(Color.BLACK);

                // If this item is selected item
                if (position == currencySelectedIndex) {
                    // Set spinner selected popup item's text color
                    tv.setTextColor(Color.BLUE);
                }

                // Return the modified view
                return tv;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currenciesSpinner.setAdapter(arrayAdapter);
        currenciesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencySelectedIndex = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void AttemptRegister(View view) {
        String nameString = nameEditText.getText().toString();
        String emailString = emailEditText.getText().toString();
        String passwordString = passEditText.getText().toString();
        int currencyID = currencySelectedIndex;
        int beginDayNumber = beginDaySpinner.getSelectedItemPosition();
        String budgetString = null;


        if (nameString.isEmpty()) {
            Toast.makeText(getApplicationContext(), "enter FullName", Toast.LENGTH_LONG).show();
            return;
        }
        if (emailString.isEmpty() || !emailString.contains("@")) {
            Toast.makeText(getApplicationContext(), "enter Correct Email", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordString.isEmpty() || passwordString.length() < 8) {
            Toast.makeText(getApplicationContext(), "enter Password at least 8 Characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (currencyID == 0) {
            Toast.makeText(getApplicationContext(), "choose your Currency", Toast.LENGTH_LONG).show();
            return;
        }
        if (budgetSwitch.isChecked()) {
            budgetString = budgetEditText.getText().toString();
            if (budgetString.isEmpty()) {
                Toast.makeText(getApplicationContext(), "enter budget value or disable it.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        registerButton.setEnabled(false);
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);

        requestRegister(nameString, emailString, passwordString, currencyID, beginDayNumber, budgetSwitch.isChecked(), budgetString);
    }

    private void requestRegister(String nameString, String emailString, String passwordString, int currencyID, int beginDayNum, boolean checked, String budgetString) {
        final Map<String, String> registerData = new HashMap<>();
        registerData.put("Email", emailString);
        registerData.put("FullName", nameString);
        registerData.put("ConcuranceyId", String.valueOf(currencyID));
        registerData.put("Password", passwordString);
        registerData.put("BegainDayOfWeek", String.valueOf(beginDayNum));
        if (checked) {
            registerData.put("BadgetSelected", "true");
            registerData.put("BadgetValue", budgetString);
        } else {
            registerData.put("BadgetSelected", "false");
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        registerData.put("DeviceId", token);
                        System.out.println("InstanceId: " + token);
                    }
                });


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
                registerButton.setEnabled(true);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int responseCode = jsonObject.getInt("Code");
                    String requestDetails = jsonObject.getString("RequstDetails");
                    Toast.makeText(getApplicationContext(), requestDetails, Toast.LENGTH_LONG).show();
                    if (responseCode == 1) {
                        session.setLogin(true);

                        String fullName = jsonObject.getString("FullName");
                        String email = jsonObject.getString("Email");
                        String token_type = jsonObject.getString("token_type");
                        String access_token = jsonObject.getString("access_token");
                        String Authorization = token_type + ' ' + access_token;
                        db.addUser(fullName, email, Authorization);
                        UserInfo.getInstance(getApplicationContext()).addInfo(fullName, email, Authorization);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        TransferData transferData = new TransferData(Request.Method.POST, ServerURL.REGISTER_URL, responseListener, null);
        transferData.setDataMap(registerData);
        System.out.println(registerData.toString());
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);

    }

    public void backToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(intent);
        finish();
    }
}
