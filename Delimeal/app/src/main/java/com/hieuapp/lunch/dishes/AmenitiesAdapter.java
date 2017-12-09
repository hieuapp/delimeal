package com.hieuapp.lunch.dishes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hieuapp.lunch.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hieuapp on 23/04/2017.
 */

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.AmenitiesViewHolder>{

    private Context mContext;
    List<String> listAmenities;
    static Map<String, Integer> mapIcon;
    static Map<String, Integer> characterMap;
    static {
        Map<String, Integer> icons = new HashMap<>();
        icons.put("take_way", R.drawable.ic_takeaway);
        icons.put("wifi", R.drawable.ic_wifi_border);
        icons.put("free_bike_parking", R.drawable.ic_parking_border);
        icons.put("out_door_seat", R.drawable.ic_outdoor_seat_border);
        icons.put("air_conditioner", R.drawable.ic_air_conditioner_border);
        icons.put("smoking_zone", R.drawable.ic_smoking_border);
        icons.put("many_floor", R.drawable.ic_stairs_border);
        icons.put("delivery_service", R.drawable.ic_delivery_border);
        icons.put("birthday_celebrate", R.drawable.ic_birthday_cake);

        mapIcon = Collections.unmodifiableMap(icons);

        Map<String, Integer> character = new HashMap<>();
        character.put("take_way", R.string.take_way);
        character.put("wifi", R.string.wifi);
        character.put("free_bike_parking", R.string.free_bike_parking);
        character.put("out_door_seat", R.string.out_door_seat);
        character.put("air_conditioner", R.string.air_conditioner);
        character.put("smoking_zone", R.string.smoking_zone);
        character.put("many_floor", R.string.many_floor);
        character.put("delivery_service", R.string.delivery_service);
        character.put("birthday_celebrate", R.string.birthday_celebrate);
        characterMap = Collections.unmodifiableMap(character);
    }

    public AmenitiesAdapter(Context context, List<String> amenities){
        this.listAmenities = amenities;
        this.mContext = context;
    }

    @Override
    public AmenitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amenities_item_layout, parent, false);
        return new AmenitiesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AmenitiesViewHolder holder, int position) {
        String amenities = listAmenities.get(position);
        int icon = mapIcon.get(amenities);
        holder.icon.setImageResource(icon);
        String character = mContext.getResources().getString(characterMap.get(amenities));
        holder.character.setText(character);
    }

    @Override
    public int getItemCount() {
        return listAmenities.size();
    }

    class AmenitiesViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView character;

        public AmenitiesViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.amenities_icon);
            character = (TextView)itemView.findViewById(R.id.character);
        }
    }
}
