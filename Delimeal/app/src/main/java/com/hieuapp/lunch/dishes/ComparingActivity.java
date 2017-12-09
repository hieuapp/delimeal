package com.hieuapp.lunch.dishes;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hieuapp.lunch.LunchIOActivity;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.api.DishPair;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.DishDetail;
import com.hieuapp.lunch.util.FormatUtils;
import com.hieuapp.lunch.util.ImageLoader;
import com.hieuapp.lunch.util.LunchConfig;
import com.hieuapp.lunch.util.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hieuapp on 01/05/2017.
 */

public class ComparingActivity extends AppCompatActivity {
    CircleImageView avatar1, avatar2;
    TextView foodName1, foodName2, reportFailed, address1, address2;
    ImageLoader imageLoader;
    RecyclerView listAttr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.comparing_act);
        avatar1 = (CircleImageView)findViewById(R.id.ava_dish1);
        avatar2 = (CircleImageView)findViewById(R.id.ava_dish2);
        foodName1 = (TextView)findViewById(R.id.food_name1);
        foodName2 = (TextView)findViewById(R.id.food_name2);
        address1 = (TextView)findViewById(R.id.address1);
        address2 = (TextView)findViewById(R.id.address2);
        listAttr = (RecyclerView)findViewById(R.id.list_attribute);
        reportFailed = (TextView)findViewById(R.id.load_data_failed);

        imageLoader = new ImageLoader(this, android.R.color.transparent);

        String pairId = bundle.getString("dish_pair");
        if(validatePairId(pairId)){
            String[] ids = pairId.split("_");
            loadDish(ids[0], ids[1]);
        }else {
            finish();
        }

    }

    private boolean validatePairId(String pair){
        if(pair == null || pair.equals("")){
            return false;
        }

        try{
            String[] ids = pair.split("_");
            Integer.valueOf(ids[0]);
            Integer.valueOf(ids[1]);
            return true;
        }catch (PatternSyntaxException syntaxEx){
            return false;
        }catch (NumberFormatException formatEx){
            return false;
        }
    }

    private void loadDish(String id1, String id2){
        String baseURL = FormatUtils.getHostURL(this);
        LunchAPI lunchAPI = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
        Call<DishPair> call = lunchAPI.getPair(id1, id2);
        call.enqueue(new Callback<DishPair>() {
            @Override
            public void onResponse(Call<DishPair> call, Response<DishPair> response) {
                if(response.body() != null){
                    showComparing(response.body().getPair().get(0), response.body().getPair().get(1));
                }else {
                    showComparing(null, null);
                }
            }

            @Override
            public void onFailure(Call<DishPair> call, Throwable t) {
                showComparing(null, null);
            }
        });
    }

    private void showComparing(DishDetail d1, DishDetail d2){
        if(d1 == null || d2 == null) {
            reportFailed.setVisibility(View.VISIBLE);
            return;
        }

        imageLoader.loadImage(d1.getImage().trim() + "?w=480", avatar1);
        imageLoader.loadImage(d2.getImage().trim() + "?w=480", avatar2);
        foodName1.setText(d1.getFoodName());
        foodName2.setText(d2.getFoodName());
        address1.setText(d1.getShortAddress());
        address2.setText(d2.getShortAddress());

        ComparisonAdapter adapter = new ComparisonAdapter(this, dish2List(d1, d2));
        listAttr.setAdapter(adapter);
    }

    private List<Couple> dish2List(DishDetail d1, DishDetail d2){
        List<Couple> coupleList = new ArrayList<>();
        Resources resources = this.getResources();

        Couple rating = new Couple(R.drawable.ic_rating,
                resources.getString(R.string.attr_rating),
                String.valueOf(d1.getRating()),
                String.valueOf(d2.getRating()));

        Couple price = new Couple(R.drawable.ic_price,
                resources.getString(R.string.attr_price),
                String.valueOf(d1.getPrice()),
                String.valueOf(d2.getPrice()));

        double userLat = Double.valueOf(LunchIOActivity.session.get(Mapping.LATITUDE_KEY));
        double userLon = Double.valueOf(LunchIOActivity.session.get(Mapping.LONGITUDE_KEY));

        double distance1 = FormatUtils.distance(userLat, userLon,
                d1.getLatitude(), d1.getLongitude(),"K");
        double distance2 = FormatUtils.distance(userLat, userLon,
                d2.getLatitude(), d2.getLongitude(), "K");

        Couple distance = new Couple(R.drawable.ic_distance,
                resources.getString(R.string.attr_distance), String.valueOf(distance1), String.valueOf(distance2));

        Couple wifi = new Couple(R.drawable.ic_wifi_border,
                resources.getString(R.string.attr_wifi), String.valueOf(d1.getAmenities().getWifi()),
                String.valueOf(d2.getAmenities().getWifi()));

        Couple parking = new Couple(R.drawable.ic_parking_border,
                resources.getString(R.string.attr_parking),
                String.valueOf(d1.getAmenities().getParking()),
                String.valueOf(d2.getAmenities().getParking()));

        Couple takeWay = new Couple(R.drawable.ic_takeaway,
                resources.getString(R.string.attr_takeWay),
                String.valueOf(d1.getAmenities().getTakeWay()),
                String.valueOf(d2.getAmenities().getTakeWay()));

        Couple air = new Couple(R.drawable.ic_air_conditioner_border,
                resources.getString(R.string.attr_air),
                String.valueOf(d1.getAmenities().getAirConditioner()),
                String.valueOf(d2.getAmenities().getAirConditioner()));

        Couple celebration = new Couple(R.drawable.ic_birthday_cake,
                resources.getString(R.string.attr_celebration),
                String.valueOf(d1.getAmenities().getCelebrate()),
                String.valueOf(d2.getAmenities().getCelebrate()));

        Couple floor = new Couple(R.drawable.ic_stairs_border,
                resources.getString(R.string.attr_manyFloor),
                String.valueOf(d1.getAmenities().getManyFloor()),
                String.valueOf(d2.getAmenities().getManyFloor()));

        Couple delivery = new Couple(R.drawable.ic_delivery_border,
                resources.getString(R.string.attr_delivery),
                String.valueOf(d1.getAmenities().getDelivery()),
                String.valueOf(d2.getAmenities().getDelivery()));

        Couple outdoor = new Couple(R.drawable.ic_outdoor_seat_border,
                resources.getString(R.string.attr_outdoor_seat),
                String.valueOf(d1.getAmenities().getOutSeat()),
                String.valueOf(d2.getAmenities().getOutSeat()));

        Couple smoking = new Couple(R.drawable.ic_smoking_border,
                resources.getString(R.string.attr_smoking_zone),
                String.valueOf(d1.getAmenities().getSmokingZone()),
                String.valueOf(d2.getAmenities().getSmokingZone()));

        coupleList.add(rating);
        coupleList.add(price);
        coupleList.add(distance);
        coupleList.add(wifi);
        coupleList.add(takeWay);
        coupleList.add(parking);
        coupleList.add(smoking);
        coupleList.add(air);
        coupleList.add(delivery);
        coupleList.add(floor);
        coupleList.add(celebration);
        coupleList.add(outdoor);

        return coupleList;
    }

    class ComparisonAdapter extends RecyclerView.Adapter<CoupleVH>{
        private List<Couple> couples;
        private Context context;

        public ComparisonAdapter(Context context, List<Couple> coupleList){
            this.couples = coupleList;
            this.context = context;
        }

        @Override
        public CoupleVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_attribute_comparison, parent, false);
            return new CoupleVH(itemView);
        }

        @Override
        public void onBindViewHolder(CoupleVH holder, int position) {
            final Couple couple = couples.get(position);
            holder.icon.setImageResource(couple.getIcon());
            holder.attrName.setText(couple.getAttribute());

            int red = getColor(R.color.material_red_300);
            int green = getColor(R.color.material_green_300);
            try{
                float v1 = Float.valueOf(couple.getValue1());
                float v2 = Float.valueOf(couple.getValue2());
                Resources resources = context.getResources();
                if(couple.getAttribute().equals(resources.getString(R.string.attr_rating))){
                    if(v1 < v2){
                        holder.attr1.setBackgroundColor(red);
                        holder.attr2.setBackgroundColor(green);
                    }else if(v1 > v2){
                        holder.attr2.setBackgroundColor(red);
                        holder.attr1.setBackgroundColor(green);
                    }else {
                        holder.attr1.setBackgroundColor(green);
                        holder.attr2.setBackgroundColor(green);
                    }
                    holder.attr1.setText(FormatUtils.friendlyRating(v1));
                    holder.attr2.setText(FormatUtils.friendlyRating(v2));
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_price))){
                    if(v1 > v2){
                        holder.attr1.setBackgroundColor(red);
                        holder.attr2.setBackgroundColor(green);
                    }else if(v1 < v2){
                        holder.attr2.setBackgroundColor(red);
                        holder.attr1.setBackgroundColor(green);
                    }else {
                        holder.attr1.setBackgroundColor(green);
                        holder.attr2.setBackgroundColor(green);
                    }
                    holder.attr1.setText(FormatUtils.friendlyPrice(v1));
                    holder.attr2.setText(FormatUtils.friendlyPrice(v2));
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_distance))){
                    if(v1 > v2){
                        holder.attr1.setBackgroundColor(red);
                        holder.attr2.setBackgroundColor(green);
                    }else if(v1 < v2){
                        holder.attr2.setBackgroundColor(red);
                        holder.attr1.setBackgroundColor(green);
                    }else {
                        holder.attr1.setBackgroundColor(green);
                        holder.attr2.setBackgroundColor(green);
                    }
                    holder.attr1.setText(FormatUtils.friendlyDistance(v1));
                    holder.attr2.setText(FormatUtils.friendlyDistance(v2));
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_wifi))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_parking))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_takeWay))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_air))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_celebration))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_manyFloor))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_delivery))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_outdoor_seat))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }else if(couple.getAttribute().equals(resources.getString(R.string.attr_smoking_zone))){
                    displayBooleanAttr(holder, v1, v2, resources);
                }
            }catch (NullPointerException ex){
                ex.printStackTrace();
            }
        }

        private void displayBooleanAttr(CoupleVH holder, float v1, float v2, Resources resources){
            int red = getColor(R.color.material_red_300);
            int green = getColor(R.color.material_green_300);
            if(v1 < v2){
                holder.attr1.setBackgroundColor(red);
                holder.attr2.setBackgroundColor(green);
            }else if(v1 > v2){
                holder.attr2.setBackgroundColor(red);
                holder.attr1.setBackgroundColor(green);
            }else {
                holder.attr1.setBackgroundColor(green);
                holder.attr2.setBackgroundColor(green);
            }

            if(v1 > 0){
                holder.attr1.setText(resources.getString(R.string.yes));
            }else {
                holder.attr1.setText(resources.getString(R.string.no));
            }

            if(v2 > 0){
                holder.attr2.setText(resources.getString(R.string.yes));
            }else {
                holder.attr2.setText(resources.getString(R.string.no));
            }
        }

        @Override
        public int getItemCount() {
            return couples.size();
        }
    }

    class CoupleVH extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView attrName, attr1, attr2;

        public CoupleVH(View itemView) {
            super(itemView);

            icon = (ImageView)itemView.findViewById(R.id.iv_icon_attr);
            attrName = (TextView)itemView.findViewById(R.id.attr_name);
            attr1 = (TextView)itemView.findViewById(R.id.attr1);
            attr2 = (TextView)itemView.findViewById(R.id.attr2);
        }
    }
}
