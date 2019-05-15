package com.medotech.masrofaty01;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsActivity extends AppCompatActivity {

    private static List<Item> itemsList = new ArrayList<>();
    private String categoryId, categoryType, categoryName;
    private ListView itemsListView;
    private FloatingActionButton addItemFAB;
    private TextView categoryNameTextView;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Bundle bundle = getIntent().getExtras();
        categoryId = bundle.getString("categoryId");
        categoryName = bundle.getString("categoryName");
        categoryType = bundle.getString("categoryType");


        addItemFAB = (FloatingActionButton) findViewById(R.id.add_item_fab);
        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("categoryType", categoryType);
                intent.putExtra("isNew", true);
                startActivity(intent);
                finish();
            }
        });

        categoryNameTextView = findViewById(R.id.category_name_items_activity);
        categoryNameTextView.setText(categoryName);

        itemsListView = findViewById(R.id.items_list_view);

        itemsList = getItems(categoryId, ServerURL.ITEMS__URL);

        arrayAdapter = new ArrayAdapter(this, R.layout.items_list, itemsList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_list, parent, false);
                }
                TextView nameTextView = convertView.findViewById(R.id.item_name_text_view);
                nameTextView.setText(itemsList.get(position).getName());
                TextView priceTextView = convertView.findViewById(R.id.item_price_text_view);
                priceTextView.setText("price: " + itemsList.get(position).getPrice() + " " + UserInfo.getInstance().getCurrency());
                TextView categoryNameTextView = convertView.findViewById(R.id.item_category_name_text_view);
                String s = "";
                if (categoryType.equals("OUTCOME")) {
                    s = getString(R.string.item_in_category) + itemsList.get(position).getIncomeCategoryName();
                } else {
                    if (categoryType.equals("INCOME")) {
                        s = getString(R.string.item_out_category) + itemsList.get(position).getOutcomeCategoryName();
                    }
                }
                categoryNameTextView.setText(s);
                TextView createDateTextView = convertView.findViewById(R.id.item_create_date_text_view);
                createDateTextView.setText(itemsList.get(position).getCreateDate());
                return convertView;
            }
        };

        itemsListView.setAdapter(arrayAdapter);
        registerForContextMenu(itemsListView);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ItemsDetailsActivity.class);
                intent.putExtra("itemName", itemsList.get(position).getName());
                intent.putExtra("itemPrice", itemsList.get(position).getPrice());
                intent.putExtra("itemNotes", itemsList.get(position).getNotes());
                intent.putExtra("itemIncomeName", itemsList.get(position).getIncomeCategoryName());
                intent.putExtra("itemOutcomeName", itemsList.get(position).getOutcomeCategoryName());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.update_menu_item:
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("categoryType", categoryType);
                intent.putExtra("isNew", false);
                intent.putExtra("itemId", itemsList.get(contextMenuInfo.position).getId());
                intent.putExtra("itemName", itemsList.get(contextMenuInfo.position).getName());
                intent.putExtra("itemPrice", itemsList.get(contextMenuInfo.position).getPrice());
                intent.putExtra("itemNotes", itemsList.get(contextMenuInfo.position).getNotes());
                intent.putExtra("itemIncomeName", itemsList.get(contextMenuInfo.position).getIncomeCategoryName());
                intent.putExtra("itemOutcomeName", itemsList.get(contextMenuInfo.position).getOutcomeCategoryName());
                startActivity(intent);
                finish();
                break;
            case R.id.delete_menu_item:
                int itemPosition = contextMenuInfo.position;
                String itemId = itemsList.get(itemPosition).getId();
                deleteItem(itemId, itemPosition);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem(String itemId, final int itemPosition) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String requestDetails = jsonResponse.getString("RequstDetails");
                    Toast.makeText(getApplicationContext(), requestDetails, Toast.LENGTH_LONG).show();
                    int responseCode = jsonResponse.getInt("Code");
                    if (responseCode == 1) {
                        itemsList.remove(itemPosition);
                        arrayAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    System.out.println(responseBody);
                } catch (JSONException e) {
                    //Handle a malformed json response
                } catch (UnsupportedEncodingException error) {

                }
            }
        };

        String url = String.format(ServerURL.DELETE_ITEM__URL + "?id=%1$s",
                itemId);

        TransferData transferData = new TransferData(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Content-Type", "application/json; charset=utf-8");
                headerMap.put("Authorization", UserInfo.getInstance().getAuthorization());
                return headerMap;
            }
        };

        transferData.setDataMap(null);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);
    }

    public List<Item> getItems(String id, String url) {
        final List<Item> list = new ArrayList<>();
        System.out.println("cat Id: " + id);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray categories_jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < categories_jsonArray.length(); i++) {
                        JSONObject c = categories_jsonArray.getJSONObject(i);
                        String id = c.getString("Id");
                        String name = c.getString("Name");
                        String price = c.getString("Price");
                        String notes = c.getString("Notes");
                        String incomeCategoryId = c.getString("IncomeCategoryId");
                        String outComeCategoryId = c.getString("OutComeCategoryId");
                        String incomeCategoryName = c.getString("IncomeCategoryName");
                        String outcomeCategoryName = c.getString("OutComeCategoryName");
                        String createDate = c.getString("CreateDate");

                        String cat = "";
                        if (categoryType.equals("OUTCOME")) {
                            cat = outcomeCategoryName;
                        } else {
                            if (categoryType.equals("INCOME")) {
                                cat = incomeCategoryName;
                            }
                        }
                        if (categoryName.equals(cat)) {
                            Item item = new Item(id, name, notes, price, incomeCategoryId, outComeCategoryId, incomeCategoryName, outcomeCategoryName, createDate);
                            list.add(item);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapter.notifyDataSetChanged();

            }
        };
        TransferData transferData = new TransferData(Request.Method.GET, url, responseListener, null) {
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
        transferData.setDataMap(null);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);

        return list;
    }

    /*public void addItem(String catId){
        // "Id":0,   "Name":" ",اﻻﯾﺠﺎر  "Notes":"nothings",   "Price":300,   "IncomeCategoryId":5,   "OutComeCategoryId":8
        System.out.println("cat Id :" + catId);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject item_object = jsonObject.getJSONObject("data");
                        String id = item_object.getString("Id");
                        String name = item_object.getString("Name");
                        String price = item_object.getString("Price");
                        String notes = item_object.getString("Notes");

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        };
        TransferData transferData = new TransferData(Request.Method.POST,ServerURL.ITEMS__URL , responseListener, null) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<>();
                //headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", UserInfo.getInstance().getAuthorization());
                return headerMap;
            }
        };
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("Id", "0");
        dataMap.put("Name", "charge");
        dataMap.put("Notes","Mobile internet bundle");
        dataMap.put("Price", "50");
        dataMap.put("OutComeCategoryId", catId);
        dataMap.put("InComeCategoryId", "48");
        transferData.setDataMap(dataMap);
        RequestQueue mainRequestQueue = MainRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        MainRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(transferData);

    }*/
}
