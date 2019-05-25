package com.medotech.masrofaty01;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.medotech.masrofaty01.util.CheckInternetConnection;
import com.medotech.masrofaty01.util.SessionManager;
import com.medotech.masrofaty01.util.SqliteHandler;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    private EditText emailEditText, passEditText;
    private Button loginButton;
    private SessionManager session;
    private SqliteHandler db;

    private FrameLayout progressBarHolder;
    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);
        progressBarHolder = findViewById(R.id.progress_bar_holder);

        passEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                AttemptLogin();
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogin();
            }
        });

        db = new SqliteHandler(getApplicationContext());


        // get Firebase Token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("InstanceId: " + "error");
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        System.out.println("InstanceId: " + token);
                    }
                });


        Currency currency = new Currency(this);
        session = new SessionManager(getApplicationContext());

        CheckInternetConnection checkInternetConnection = new CheckInternetConnection(this);
        boolean networkState = checkInternetConnection.isConnectingToInternet();
        if (networkState == false) {
            new AlertDialog.Builder(this).setTitle(R.string.no_internet_alert_title)
                    .setMessage(R.string.no_internet_alert_message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        } else {
            if (session.isLoggedIn()) {
                // User is already logged in. Take him to main activity
                Map<String, String> userInfo = db.getUserDetails();
                System.out.println(userInfo.toString());
                UserInfo.getInstance(getApplicationContext()).addInfoMap(userInfo);

                // get All User Info and set it in UserInfo
                UserInfo.getInstance(getApplicationContext()).getAllUserInfo();

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void AttemptLogin() {
        String emailString = emailEditText.getText().toString();
        String passwordString = passEditText.getText().toString();
        if (!emailString.isEmpty() && !passwordString.isEmpty()) {
            checkLogin(emailString, passwordString);

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getApplicationContext(), "please enter email and password!", Toast.LENGTH_LONG).show();
        }

    }

    private void checkLogin(String email, String password) {
        final Map<String, String> loginData = new HashMap<>();
        loginData.put("Email", email);
        loginData.put("Password", password);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        loginData.put("DeviceId", token);
                        System.out.println("InstanceId: " + token);
                    }
                });
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);

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
                        UserInfo.getInstance(getApplicationContext()).getAllUserInfo();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        TransferData transferData = new TransferData(Request.Method.POST, ServerURL.LOGIN_URL, responseListener, null);
        transferData.setDataMap(loginData);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);

    }

    public void startRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void forgetPassword(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
 /*       LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.forget_pass_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setCancelable(true);

        final EditText mailEditText = (EditText) mView.findViewById(R.id.mailInput);
        alertDialogBuilderUserInput.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> forgetPassData = new HashMap<>();
                forgetPassData.put("Email", mailEditText.getText().toString());
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

                                //Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                //startActivity(intent);
                                //finish();
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
        });

        AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();*/
    }
}
