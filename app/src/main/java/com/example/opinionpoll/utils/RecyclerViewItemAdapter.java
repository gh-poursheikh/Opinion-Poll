package com.example.opinionpoll.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opinionpoll.R;
import com.example.opinionpoll.model.Product;
import com.example.opinionpoll.model.Rating;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewItemAdapter extends RecyclerView.Adapter<RecyclerViewItemAdapter.ItemViewHolder> {

    public static final String TAG = RecyclerViewItemAdapter.class.getSimpleName();

    private ArrayList<Product> productList;
    private ArrayList<Rating> ratingList;
    private Context context;
    private int personId;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemText;
        private ImageView itemImage;
        private RatingBar itemRating;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.itemTextView);
            itemImage = itemView.findViewById(R.id.itemImageView);
            itemRating = itemView.findViewById(R.id.itemRatingBar);
        }
    }

    public RecyclerViewItemAdapter(ArrayList<Product> productList, ArrayList<Rating> ratingList, int personId, Context context) {
        this.productList = productList;
        this.ratingList = ratingList;
        this.context = context;
        this.personId = personId;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        final Product currentProductItem = productList.get(position);
        final Rating currentRatingItem = new Rating(0, 0, 0);

        if (context == null) {
            return;
        }

        holder.itemText.setText(currentProductItem.getProductName());
        holder.itemRating.setRating(currentRatingItem.getRatingNumber());

        String imageFeed = currentProductItem.getProductImagePath();
        Picasso.get().load(MyWebService.BASE_URL + imageFeed)
                .resize(80, 80)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_error)
                .into(holder.itemImage);

        holder.itemRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    currentRatingItem.setRatingNumber((int) rating);

                    try {
                        currentRatingItem.setPersonId(personId);
                        currentRatingItem.setProductId(currentProductItem.getProductId());
                        currentRatingItem.setRatingNumber((int) rating);

                        // check if the newly rated item is already rated.
                        // If so the newly rated item must replace the item previously rated
                        // so that items always have unique ids
                        boolean firstTime = true;
                        for (Rating ratingItemCounter : ratingList) {
                            if (currentRatingItem.getProductId() == (ratingItemCounter.getProductId())) {
                                int index = ratingList.indexOf(ratingItemCounter);
                                ratingList.set(index, currentRatingItem);
                                firstTime = false;
                            }
                        }

                        if (firstTime) {
                            ratingList.add(currentRatingItem);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = productList.size();
        Log.d(TAG, "getItemCount: productList.size() = " + size);
        return productList.size();
    }
}
