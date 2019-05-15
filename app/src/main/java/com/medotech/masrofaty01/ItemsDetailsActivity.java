package com.medotech.masrofaty01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ItemsDetailsActivity extends AppCompatActivity {

    private String itemName;
    private String itemPrice;
    private String itemNotes;
    private String itemIncomeName;
    private String itemOutcomeName;

    private TextView nameTextView, priceTextView, notesTextView, outcomeTextView, incomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);

        Bundle bundle = getIntent().getExtras();
        itemName = bundle.getString("itemName");
        itemPrice = bundle.getString("itemPrice");
        itemNotes = bundle.getString("itemNotes");
        itemIncomeName = bundle.getString("itemIncomeName");
        itemOutcomeName = bundle.getString("itemOutcomeName");

        nameTextView = findViewById(R.id.title_text_view_item_details_activity);
        priceTextView = findViewById(R.id.price_item_text_view);
        notesTextView = findViewById(R.id.notes_item_text_view);
        outcomeTextView = findViewById(R.id.outcome_item_text_view);
        incomeTextView = findViewById(R.id.income_item_text_view);

        nameTextView.setText(itemName);
        priceTextView.setText(itemPrice);
        notesTextView.setText(itemNotes);
        outcomeTextView.setText(itemOutcomeName);
        incomeTextView.setText(itemIncomeName);

    }
}
