package com.medotech.masrofaty01;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private CategoryTab categoryTab;
    private List<Category> categoryList;


    public RecyclerViewAdapter(Context mContext, CategoryTab categoryTab, List<Category> categoryList) {
        this.mContext = mContext;
        this.categoryTab = categoryTab;
        this.categoryList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.category_cardview, parent, false);
        return new MyViewHolder(categoryTab, view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_category_title.setText(categoryList.get(position).getName());
        holder.tv_category_details.setText("Money: " + categoryList.get(position).getMoney());
        holder.img_category_thumbnail.setImageResource(categoryList.get(position).getIcon());
        holder.more_option_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.more_option_image_view);
                popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update_menu_item:
                                categoryTab.updateCategory(position);
                                break;
                            case R.id.delete_menu_item:
                                categoryTab.deleteCategory(position);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, categoryList.get(position).getName() + " Clicked", Toast.LENGTH_LONG).show();
                categoryTab.viewItems(position);

                /*Intent intent = new Intent(mContext,Book_Activity.class);

                // passing data to the book activity
                intent.putExtra("Title", categoryList.get(position).getTitle());
                intent.putExtra("Description", categoryList.get(position).getDescription());
                intent.putExtra("Thumbnail", categoryList.get(position).getThumbnail());
                // start the activity
                mContext.startActivity(intent);
                */
            }
        });


    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView tv_category_title;
        TextView tv_category_details;
        ImageView img_category_thumbnail;
        ImageView more_option_image_view;
        CardView cardView;
        private CategoryTab categoryTab;


        public MyViewHolder(CategoryTab categoryTab, View itemView) {
            super(itemView);
            this.categoryTab = categoryTab;

            tv_category_title = itemView.findViewById(R.id.category_title);
            img_category_thumbnail = itemView.findViewById(R.id.category_image);
            more_option_image_view = itemView.findViewById(R.id.more_option_image_view);
            cardView = itemView.findViewById(R.id.category_cardview);
            itemView.setOnCreateContextMenuListener(this);
            tv_category_details = itemView.findViewById(R.id.category_details);


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), R.id.update_menu_item, Menu.NONE, R.string.update_menu_item)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            categoryTab.updateCategory(getAdapterPosition());
                            return false;
                        }
                    });
            menu.add(this.getAdapterPosition(), R.id.delete_menu_item, Menu.NONE, R.string.delete_menu_item)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            categoryTab.deleteCategory(getAdapterPosition());
                            return false;
                        }
                    });


        }
    }

}
