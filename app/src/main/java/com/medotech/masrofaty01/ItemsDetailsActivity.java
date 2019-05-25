package com.medotech.masrofaty01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ItemsDetailsActivity extends AppCompatActivity {

    private String itemName;
    private String itemPrice;
    private String itemNotes;
    private String categoryName;

    private TextView nameTextView, priceTextView, notesTextView, categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        Bundle bundle = getIntent().getExtras();
        itemName = bundle.getString("itemName");
        itemPrice = bundle.getString("itemPrice");
        itemNotes = bundle.getString("itemNotes");
        categoryName = bundle.getString("categoryName");

        nameTextView = findViewById(R.id.title_text_view_item_details_activity);
        priceTextView = findViewById(R.id.price_item_text_view);
        notesTextView = findViewById(R.id.notes_item_text_view);
        categoryTextView = findViewById(R.id.outcome_item_text_view);


        nameTextView.setText(itemName);
        priceTextView.setText(itemPrice + " " + UserInfo.getInstance(getApplicationContext()).getCurrency());
        notesTextView.setText(itemNotes);
        categoryTextView.setText(categoryName);

    }
}
