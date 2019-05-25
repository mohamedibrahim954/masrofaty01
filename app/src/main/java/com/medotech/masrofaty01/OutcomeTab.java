package com.medotech.masrofaty01;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.medotech.masrofaty01.util.TransferData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class OutcomeTab extends CategoryTab {

    public static List<Category> categoryList = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private Context context;

    @SuppressLint("ValidFragment")
    public OutcomeTab(Context context1) {
        this.context = context1;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                categoryList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray categories_jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < categories_jsonArray.length(); i++) {
                        JSONObject c = categories_jsonArray.getJSONObject(i);
                        String id = c.getString("Id");
                        String name = c.getString("Name");
                        String icon = c.getString("Icon");
                        String money = c.getString("Money");
                        String budget = c.getString("Budget");
                        System.out.println("Add new category" + i + ": " + name);
                        int iconId = getIconId(icon);
                        categoryList.add(i, new Category(id, name, iconId, money, budget));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerViewAdapter.notifyDataSetChanged();

            }
        };
        TransferData transferData = new TransferData(Request.Method.GET, ServerURL.GET_ALL_OUTCOME_CATEGORIES_URL, responseListener, null) {
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

    private int getIconId(String icon) {
        int resourseId = getResources().getIdentifier(icon.toLowerCase(), "drawable", getActivity().getPackageName());
        return resourseId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.outcome_tab, container, false);

        System.out.println("fragmment onCreateView method ");
        recyclerView = rootView.findViewById(R.id.recycler_view_outcome);
        recyclerViewAdapter = new RecyclerViewAdapter(context, this, categoryList);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        int viewWidth = recyclerView.getMeasuredWidth();
                        float cardViewWidth = getActivity().getResources().getDimension(R.dimen.category_card_view);
                        int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                        gridLayoutManager.setSpanCount(newSpanCount);
                        gridLayoutManager.requestLayout();
                    }
                }
        );
        recyclerView.setAdapter(recyclerViewAdapter);

        registerForContextMenu(recyclerView);

        return rootView;
    }

    @Override
    public void updateCategory(int position) {
        super.updateCategory(position);
        Intent intent = new Intent(getActivity(), UpdateCategoryActivity.class);
        intent.putExtra("id", categoryList.get(position).getId());
        intent.putExtra("type", "OUTCOME");
        intent.putExtra("name", categoryList.get(position).getName());
        intent.putExtra("icon", categoryList.get(position).getIcon());
        System.out.println(categoryList.get(position).getName() + ": " + categoryList.get(position).getIcon());
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void deleteCategory(final int position) {
        super.deleteCategory(position);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int responseCode = jsonObject.getInt("Code");
                    String requestDetails = jsonObject.getString("RequstDetails");
                    if (responseCode == 1) {
                        categoryList.remove(position);
                        recyclerView.removeViewAt(position);
                        recyclerViewAdapter.notifyItemRemoved(position);
                    }

                    Toast.makeText(getActivity().getApplicationContext(), requestDetails, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerViewAdapter.notifyDataSetChanged();

            }
        };

        String url = String.format(ServerURL.DELETE_CATEGORY_URL + "?id=%1$s",
                categoryList.get(position).getId());

        TransferData transferData = new TransferData(Request.Method.GET, url, responseListener, null) {
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
    public void viewItems(int position) {
        super.viewItems(position);
        Intent intent = new Intent(getActivity().getApplicationContext(), ItemsActivity.class);
        intent.putExtra("categoryId", categoryList.get(position).getId());
        intent.putExtra("categoryName", categoryList.get(position).getName());
        intent.putExtra("categoryType", "OUTCOME");
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    /* @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_menu_item:
                Toast.makeText(getActivity().getApplicationContext(),"update: item position " + item.getGroupId() ,Toast.LENGTH_LONG).show();
                break;
            case R.id.delete_menu_item:
                Toast.makeText(getActivity().getApplicationContext(),"delete: item position " + item.getGroupId() ,Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }*/
}
