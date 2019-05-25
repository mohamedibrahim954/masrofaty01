package com.medotech.masrofaty01;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FloatingActionButton fab;

    public static TextView userMoneyTextView;
    private static TextView profileSymbolTextView, fullNameTextView, emailTextView, currencyTextView, moneyTextView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    public static void displayUserInfo(String fullName, String email, String currency, String money) {
        System.out.println("display userInfo ");
        char c = fullName.charAt(0);
        profileSymbolTextView.setText("" + c);
        fullNameTextView.setText(fullName);
        emailTextView.setText(email);
        currencyTextView.setText(currency);
        moneyTextView.setText(money + " " + currency);
        userMoneyTextView.setText(money + " " + currency);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.home_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, android.R.string.ok, android.R.string.no);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        profileSymbolTextView = headerView.findViewById(R.id.first_big_letter_text_view);
        fullNameTextView = headerView.findViewById(R.id.user_full_name);
        emailTextView = headerView.findViewById(R.id.user_email);
        currencyTextView = headerView.findViewById(R.id.user_currency);
        moneyTextView = headerView.findViewById(R.id.user_money);


        userMoneyTextView = findViewById(R.id.user_money_text_view);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        UserInfo.getInstance(this).getAllUserInfo();
        fullNameTextView.setText("mohamed");

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id) {
//            case R.id.action_settings:
//                return true;
            case R.id.add_category:
                Intent i = new Intent(this, AddCategoryActivity.class);
                startActivity(i);
                finish();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.change_password_menu_item:
                displayChangePasswordDialog();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayChangePasswordDialog() {
        System.out.println("display Change Password Dialog");
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.change_pass_dialog, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setCancelable(true);

        final TextInputLayout oldPassInputLayout = mView.findViewById(R.id.old_pass_edit_text_dialog);
        final TextInputLayout newPassInputLayout = mView.findViewById(R.id.new_pass_edit_text_dialog);
        final TextInputLayout confirmPassInputLayout = mView.findViewById(R.id.confirm_pass_edit_text_dialog);
        Button sendButton = mView.findViewById(R.id.send_button_dialog);

        alertDialogBuilderUserInput.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attemptToChangePassword(oldPassInputLayout, newPassInputLayout, confirmPassInputLayout)) {
                    alertDialog.cancel();
                }
            }

            private boolean attemptToChangePassword(TextInputLayout oldPassInputLayout, TextInputLayout newPassInputLayout, TextInputLayout confirmPassInputLayout) {
                String oldPassword = oldPassInputLayout.getEditText().getText().toString().trim();
                String newPassword = newPassInputLayout.getEditText().getText().toString().trim();
                String confirmPassword = confirmPassInputLayout.getEditText().getText().toString().trim();

                boolean valid = true;
                if (TextUtils.isEmpty(oldPassword)) {
                    oldPassInputLayout.setErrorEnabled(true);
                    oldPassInputLayout.setError("This Field Can't Be Empty.");
                    valid = false;
                } else {
                    oldPassInputLayout.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(newPassword)) {
                    newPassInputLayout.setErrorEnabled(true);
                    newPassInputLayout.setError("This Field Can't Be Empty.");
                    valid = false;
                } else {
                    newPassInputLayout.setErrorEnabled(false);

                    if (TextUtils.isEmpty(oldPassword)) {
                        confirmPassInputLayout.setErrorEnabled(true);
                        confirmPassInputLayout.setError("This Field Can't Be Empty.");
                        valid = false;
                    } else {
                        confirmPassInputLayout.setErrorEnabled(false);
                        if (!TextUtils.equals(newPassword, confirmPassword)) {
                            confirmPassInputLayout.setErrorEnabled(true);
                            confirmPassInputLayout.setError("The PassWord Don't Match.");
                            valid = false;
                        }

                    }
                }

                if (valid) {
                    requestChangePassword(oldPassword, newPassword);
                }

                return valid;
            }
        });


    }

    public void requestChangePassword(String oldPass, String newPass) {
        Map<String, String> changePassData = new HashMap<>();
        changePassData.put("OldPassword", oldPass);
        changePassData.put("NewPassword", newPass);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int responseCode = jsonObject.getInt("Code");
                    String requestDetails = jsonObject.getString("RequstDetails");

                    new AlertDialog.Builder(HomeActivity.this).setTitle(R.string.change_password)
                            .setMessage(requestDetails)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    if (responseCode == 1) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        TransferData transferData = new TransferData(Request.Method.POST, ServerURL.RESET_PASSWORD_URL, responseListener, null) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                //headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", UserInfo.getInstance(getApplicationContext()).getAuthorization());
                return headerMap;
            }
        };
        transferData.setDataMap(changePassData);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    OutcomeTab outcomeTab = new OutcomeTab(HomeActivity.this);
                    return outcomeTab;
                case 1:
                    IncomeTab incomeTab = new IncomeTab(HomeActivity.this);
                    return incomeTab;
                case 2:
                    StatisticsTab statisticsTab = new StatisticsTab();
                    return statisticsTab;
                default:
                    return null;


            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
