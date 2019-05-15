package com.medotech.masrofaty01;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextInputLayout emailTextInputLayout, codeTextInputLayout, emailVerificationTextInputLayout, newPasswordTextInputLayout;
    LinearLayout emailLinearLayout, verificationLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailTextInputLayout = findViewById(R.id.email_text_input);
        codeTextInputLayout = findViewById(R.id.verification_code_text_input);
        emailVerificationTextInputLayout = findViewById(R.id.email_verification_text_input);
        newPasswordTextInputLayout = findViewById(R.id.password_verification_text_input);

        emailLinearLayout = findViewById(R.id.send_email_linear_layout);
        verificationLinearLayout = findViewById(R.id.verification_linear_layout);
        emailLinearLayout.setVisibility(View.VISIBLE);
        verificationLinearLayout.setVisibility(View.GONE);
    }

    public void haveCode(View view) {
        emailLinearLayout.setVisibility(View.GONE);
        verificationLinearLayout.setVisibility(View.VISIBLE);
    }

    public void sendForgetPasswordRequest(View view) {
        String email = emailTextInputLayout.getEditText().getText().toString().trim();
        if (!email.isEmpty()) {
            emailTextInputLayout.setErrorEnabled(false);
            String url = String.format(ServerURL.FORGET_PASSWORD_URL + "?Email=%1$s",
                    email);
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "recieve respose!!", Toast.LENGTH_SHORT).show();
                    System.out.println(response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int responseCode = jsonObject.getInt("Code");
                        String requestDetails = jsonObject.getString("RequstDetails");
                        Toast.makeText(getApplicationContext(), requestDetails, Toast.LENGTH_LONG).show();
                        if (responseCode == 1) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };
            TransferData transferData = new TransferData(Request.Method.GET, url, responseListener, null);
            transferData.setDataMap(null);
            RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
            MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);
        } else {
            emailTextInputLayout.setErrorEnabled(true);
            emailTextInputLayout.setError("Email can't be Empty!!");
        }
    }

    public void sendVerificationCode(View view) {
        boolean ready = true;
        String email = emailVerificationTextInputLayout.getEditText().getText().toString().trim();
        String password = newPasswordTextInputLayout.getEditText().getText().toString().trim();
        String code = codeTextInputLayout.getEditText().getText().toString().trim();
        Map<String, String> forgetPassData = new HashMap<>();

        forgetPassData.put("Email", email);
        forgetPassData.put("Password", password);
        forgetPassData.put("Code", code);
        if (email.isEmpty()) {
            emailVerificationTextInputLayout.setErrorEnabled(true);
            emailVerificationTextInputLayout.setError("Email can't be Empty!!");
            ready = false;
        } else {
            emailVerificationTextInputLayout.setErrorEnabled(false);
        }

        if (password.isEmpty()) {
            newPasswordTextInputLayout.setErrorEnabled(true);
            newPasswordTextInputLayout.setError("Password can't be Empty!!");
            ready = false;
        } else {
            newPasswordTextInputLayout.setErrorEnabled(false);
        }
        if (code.isEmpty()) {
            codeTextInputLayout.setErrorEnabled(true);
            codeTextInputLayout.setError("Verification Code can't be Empty!!");
            ready = false;
        } else {
            codeTextInputLayout.setErrorEnabled(false);
        }
        if (ready) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int responseCode = jsonObject.getInt("Code");
                        String requestDetails = jsonObject.getString("RequstDetails");
                        Toast.makeText(getApplicationContext(), requestDetails, Toast.LENGTH_LONG).show();
                        if (responseCode == 1) {
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };
            TransferData transferData = new TransferData(Request.Method.POST, ServerURL.FORGET_PASSWORD_URL, responseListener, null);
            transferData.setDataMap(forgetPassData);
            RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
            MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);
        }

    }
}
