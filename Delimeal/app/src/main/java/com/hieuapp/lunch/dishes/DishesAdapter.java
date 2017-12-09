package com.hieuapp.lunch.dishes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hieuapp.lunch.R;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.map.MapActivity;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.ImageLoader;
import com.hieuapp.lunch.util.FormatUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hieuapp on 09/03/2017.
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishViewHolder> {

    private List<Dish> dishList;
    private ImageLoader imageLoader;
    private Context mContext;
    private int primaryDish;
    public static final int MAP_VIEW_MODE = 1;
    public static final int LIST_VIEW_MODE = 2;
    public static final int RELATES_VIEW_MODE = 3;
    public static final int CARD_VIEW_MODE = 4;
    private int modeView;

    private LunchAPI api;

    public DishesAdapter(Context context, List<Dish> dishes, int modeView){
        this.dishList = dishes;
        imageLoader = new ImageLoader(context, android.R.color.transparent);
        this.mContext = context;
        this.modeView = modeView;
        String baseURL = FormatUtils.getHostURL(context);
        api = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if(modeView == MAP_VIEW_MODE){
            layoutId = R.layout.dish_item_on_map;
        }else if(modeView == CARD_VIEW_MODE){
            layoutId = R.layout.list_item_dish_card;
        }else{
            layoutId = R.layout.dish_item_layout;
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new DishViewHolder(itemView, modeView);
    }

    @Override
    public void onBindViewHolder(DishViewHolder holder, int position) {
        final Dish dish = dishList.get(position);
        holder.dishName.setText(dish.getFoodName());

        if(dish.getPrice() > 0){
            holder.price.setText(FormatUtils.friendlyPrice(dish.getPrice()));
        }

        if(dish.getRating() > 0){
            holder.ratingBar.setRating(dish.getRating());
            holder.ratePoint.setText(FormatUtils.friendlyRating(dish.getRating()));
        }else {
            holder.ratingBar.setVisibility(View.INVISIBLE);
        }

        imageLoader.loadImage(dish.getImage() , holder.dishImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DishDetailActivity.class);
                intent.putExtra("dish_id", dish.getId());
                mContext.startActivity(intent);
            }
        });

        if(modeView == LIST_VIEW_MODE || modeView == CARD_VIEW_MODE || modeView == RELATES_VIEW_MODE){
            holder.restAddress.setText(dish.getShortAddress());
        }

        if(modeView == RELATES_VIEW_MODE){
            holder.btnCompare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dishPair = primaryDish + "_" + dish.getId();
                    Intent intent = new Intent(mContext, ComparingActivity.class);
                    intent.putExtra("dish_pair", dishPair);
                    mContext.startActivity(intent);
                }
            });
        }

        if(modeView == CARD_VIEW_MODE){
            if(dish.isSaved()){
                holder.icSaved.setImageResource(R.drawable.ic_love);
            }
            holder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uid = AccountUtils.getFacebookId(mContext);
//                    saveToFavorite(String.valueOf(dish.getId()), uid);
                }
            });

            holder.direction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri directUri = Uri.parse("http://maps.google.com/maps?saddr=" +
                            ExploreDishesFragment.getLocation().getLatitude()+ "," +
                            ExploreDishesFragment.getLocation().getLongitude()+ "&daddr=" +
                            dish.getLatitude()+","+dish.getLongitude());

                    Intent intent = new Intent(Intent.ACTION_VIEW, directUri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER );
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    mContext.startActivity(intent);
                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = dish.getFoodName() + "\n" +
                            "Address: " + dish.getShortAddress();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, dish.getFoodName());
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    mContext.startActivity(Intent.createChooser(intent, "Chia sẻ qua"));
                }
            });
        }
    }

    private void saveToFavorite(String dish, String uid){
        String baseURL = FormatUtils.getHostURL(mContext);
        LunchAPI api = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
        Map<String, String> data = new HashMap<>();
        data.put("dish", dish);
        data.put("user", uid);
        data.put("lat", String.valueOf(ExploreDishesFragment.getLocation().getLatitude()));
        data.put("lon", String.valueOf(ExploreDishesFragment.getLocation().getLongitude()));
        Call<Object> call = api.save(data);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body() != null){
                    Toast.makeText(mContext, "Đã lưu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(mContext, "Lưu thất bại!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setPrimaryDish(int id){
        this.primaryDish = id;
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder{

        ImageView dishImage, btnCompare, icSaved;
        TextView dishName, restAddress, price, ratePoint;
        RelativeLayout direction, share, save;
        RatingBar ratingBar;
        public View lineActive;

        public DishViewHolder(View itemView, int viewMode) {
            super(itemView);

            dishImage = (ImageView)itemView.findViewById(R.id.thumbnail);
            dishName = (TextView)itemView.findViewById(R.id.food_name);
            price = (TextView)itemView.findViewById(R.id.price);
            ratePoint = (TextView)itemView.findViewById(R.id.rate_point);
            ratingBar = (RatingBar)itemView.findViewById(R.id.stars);

            if(viewMode == DishesAdapter.LIST_VIEW_MODE ||
                    viewMode == DishesAdapter.CARD_VIEW_MODE ||
                    viewMode == DishesAdapter.RELATES_VIEW_MODE){
                restAddress = (TextView)itemView.findViewById(R.id.restaurant_add);
                restAddress.setVisibility(View.VISIBLE);
            }

            if(viewMode == DishesAdapter.MAP_VIEW_MODE){
                lineActive = itemView.findViewById(R.id.line_active);
            }

            if(viewMode == DishesAdapter.RELATES_VIEW_MODE){
                btnCompare = (ImageView)itemView.findViewById(R.id.compare);
            }

            if(viewMode == DishesAdapter.CARD_VIEW_MODE){
                direction = (RelativeLayout)itemView.findViewById(R.id.directions);
                share = (RelativeLayout)itemView.findViewById(R.id.share);
                save = (RelativeLayout)itemView.findViewById(R.id.save);
                icSaved = (ImageView)itemView.findViewById(R.id.icon_save);
            }

        }
    }
}
