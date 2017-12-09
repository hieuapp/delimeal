package com.hieuapp.lunch.dishes;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hieuapp.lunch.LunchIOActivity;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.Comment;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishDetail;
import com.hieuapp.lunch.data.source.DishRepository;
import com.hieuapp.lunch.data.source.local.DishLocalData;
import com.hieuapp.lunch.data.source.remote.DishRemoteData;
import com.hieuapp.lunch.map.MapUtils;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.ImageLoader;
import com.hieuapp.lunch.util.FormatUtils;
import com.hieuapp.lunch.util.Mapping;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hieuapp.lunch.util.LogUtils.LOGD;
import static com.hieuapp.lunch.util.LogUtils.LOGE;
import static com.hieuapp.lunch.util.LogUtils.makeLogTag;

/**
 * Created by hieuapp on 20/04/2017.
 */

public class DishDetailActivity extends AppCompatActivity
        implements DishDetailContract.View, OnMapReadyCallback, View.OnClickListener,
        GoogleMap.OnMarkerClickListener{

    private static final String TAG = makeLogTag(DishDetailActivity.class);

    Toolbar toolbar;
    private ImageView imgCover, icSaved;
    private ImageLoader imageLoader;
    private DishDetailPresenter mPresenter;
    private TextView timeOperation, foodName, restName, description,
            price, distance, fullAddress, rating;
    private RatingBar ratingBar;
    RecyclerView listAmenities, listDishRelates;
    RelativeLayout relatesContainer;
    LinearLayout mTags, directions, share, save;
    LinearLayout priceCritiqueBar, priceDown, distanceDown;
    CircularProgressView waitLoadRelates;
    NestedScrollView detailScroll;
    DishDetail currentDish;
    EditText inputCommnent;
    ImageView btnSendComment;

    boolean saved = false;

    private List<String> amenitiesData = new ArrayList<>();
    private  AmenitiesAdapter amenitiesAdapter;
    private List<String> listChip = new ArrayList<>();

    LunchAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        String baseURL = FormatUtils.getHostURL(this);
        api = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);

        waitLoadRelates = (CircularProgressView)findViewById(R.id.wait_load_relates);
        imgCover = (ImageView)findViewById(R.id.session_photo);
        imageLoader = new ImageLoader(this, android.R.color.transparent);
        foodName = (TextView)findViewById(R.id.food_name);
        restName = (TextView)findViewById(R.id.restaurant_name);
        price = (TextView)findViewById(R.id.price);
        rating = (TextView)findViewById(R.id.rate_point);
        ratingBar = (RatingBar)findViewById(R.id.stars);
        distance = (TextView)findViewById(R.id.distance);
        fullAddress = (TextView)findViewById(R.id.full_address);
        timeOperation = (TextView)findViewById(R.id.time_closed);
        listAmenities = (RecyclerView)findViewById(R.id.amenities_list);

        priceCritiqueBar = (LinearLayout)findViewById(R.id.price_critique_bar);
        detailScroll = (NestedScrollView)findViewById(R.id.detail_scroll_view);

        priceDown = (LinearLayout)findViewById(R.id.critique_price_down);
        priceDown.setOnClickListener(this);
        distanceDown = (LinearLayout)findViewById(R.id.critique_distance_down);
        distanceDown.setOnClickListener(this);

        directions = (LinearLayout)findViewById(R.id.directions);
        share = (LinearLayout)findViewById(R.id.share);
        save = (LinearLayout)findViewById(R.id.save);
        icSaved = (ImageView)findViewById(R.id.ic_saved);

        directions.setOnClickListener(this);
        share.setOnClickListener(this);
        save.setOnClickListener(this);

        mTags = (LinearLayout) findViewById(R.id.amenities_tags);

        amenitiesAdapter = new AmenitiesAdapter(this, amenitiesData);
        listAmenities.setAdapter(amenitiesAdapter);

        inputCommnent = (EditText)findViewById(R.id.input_comment);
        btnSendComment = (ImageView)findViewById(R.id.btn_comment);
        btnSendComment.setOnClickListener(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initPresenter();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int dishId = bundle.getInt("dish_id");
            mPresenter.getDishDetail(dishId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void initPresenter(){
        DishRemoteData dishRemoteData = DishRemoteData.getInstance(this);
        DishLocalData dishLocalData = DishLocalData.getInstance(this);
        DishRepository repository = DishRepository.getInstance(dishRemoteData, dishLocalData);
        mPresenter = new DishDetailPresenter(repository, this);
    }

    @Override
    public void showDishDetail(DishDetail dishDetail) {
        if(dishDetail == null){
            return;
        }
        mPresenter.storeSession(dishDetail);
        currentDish = dishDetail;
        getSavedState();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_preview);
        mapFragment.getMapAsync(this);

        imageLoader.loadImage(dishDetail.getImage().trim() + "?w=480",imgCover);
        foodName.setText(dishDetail.getFoodName());
        restName.setText(dishDetail.getRestName());

        displaySpeakersData(dishDetail.getComments());

        ratingBar.setRating(dishDetail.getRating());
        rating.setText(FormatUtils.friendlyRating(dishDetail.getRating()));
        if(dishDetail.getPrice() > 0){
            priceCritiqueBar.setVisibility(View.VISIBLE);
            price.setText(FormatUtils.friendlyPrice(dishDetail.getPrice()));
        }

        Location lastLocation = ExploreDishesFragment.getLocation();
        double d = FormatUtils.distance(currentDish.getLatitude(), currentDish.getLongitude(),
                lastLocation.getLatitude(), lastLocation.getLongitude(), "K");
        LunchIOActivity.session.put(Mapping.LOCATION_KEY, String.valueOf(d));

        String km = FormatUtils.friendlyDistance(d);
        distance.setText(km);
        fullAddress.setText(dishDetail.getFullAddress());

        String time = this.getResources().getString(R.string.open_util) + " "
                + FormatUtils.int2Hour(dishDetail.getTimeClose());
        timeOperation.setText(time);

        for(String amenities : currentDish.getMapAmenities().keySet()){
            if(currentDish.getMapAmenities().get(amenities) == 1){
                amenitiesData.add(amenities);
            }else {
                listChip.add(amenities);
            }
        }

        amenitiesAdapter.notifyDataSetChanged();

        LayoutInflater inflater = LayoutInflater.from(this);
        for(String chipTag : listChip){
            final TextView chipView = (TextView)inflater.inflate(
                    R.layout.include_amenities_tag_chip, mTags, false);
            final int cid = AmenitiesAdapter.characterMap.get(chipTag);
            chipView.setText(getResources().getString(cid));
            chipView.setTag(chipTag);
            chipView.setOnClickListener(this);

            mTags.addView(chipView);
        }
    }

    private void getSavedState(){
        String uid = AccountUtils.getFacebookId(this);
        Call<String> call = api.checkSaved(uid, String.valueOf(currentDish.getId()));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() != null){
                    if(response.body().equals("true")){
                        icSaved.setImageResource(R.drawable.ic_love);
                        saved = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void toggleSaved(){
        saved = !saved;
        if(saved){
            icSaved.setImageResource(R.drawable.ic_love);
        }else {
            icSaved.setImageResource(R.drawable.ic_love_border);
        }
    }

    private void critiqueAmenities(final String chip){
        LunchIOActivity.session.put(chip,"1");
        LunchIOActivity.session.put("critique",chip);
        //Reset wish session
        LunchIOActivity.session.put("wish","");
        mPresenter.doCritique(LunchIOActivity.session);

    }

    @Override
    public void adaptDishRelates(List<Dish> dishes) {
        DishesAdapter relatesAdapter = new DishesAdapter(this, dishes, DishesAdapter.RELATES_VIEW_MODE);
        relatesAdapter.setPrimaryDish(currentDish.getId());
        listDishRelates.setAdapter(relatesAdapter);
        waitLoadRelates.stopAnimation();
        waitLoadRelates.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showWaitLoadRelates() {
        relatesContainer = (RelativeLayout)findViewById(R.id.relates_container);
        listDishRelates = (RecyclerView)findViewById(R.id.list_dish_relates);
        relatesContainer.setVisibility(View.VISIBLE);
        waitLoadRelates.setVisibility(View.VISIBLE);
        waitLoadRelates.startAnimation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng pos = new LatLng(currentDish.getLatitude(), currentDish.getLongitude());
        MarkerOptions markerOptions = MapUtils.createPinMarker(this, currentDish.getFoodName(),
                FormatUtils.friendlyPrice(currentDish.getPrice()), pos);
        Marker marker =  googleMap.addMarker(markerOptions);
        marker.showInfoWindow();
        googleMap.stopAnimation();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14.0f));
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_comment:
                String comment = inputCommnent.getText().toString();
                if(!comment.equals("")){
                    sendComment(comment);
                    inputCommnent.setText("");
                }
                break;
            case R.id.directions:
                mPresenter.directions(this, currentDish);
                break;
            case R.id.share:
                mPresenter.share(this, currentDish);
                break;
            case R.id.save:
                toggleSaved();
                break;
            case R.id.critique_price_down:
                LunchIOActivity.session.put("critique", Mapping.PRICE_KEY);
                LunchIOActivity.session.put("wish","down");
                mPresenter.doCritique(LunchIOActivity.session);
                break;
            case R.id.critique_distance_down:
                LunchIOActivity.session.put("critique", Mapping.LOCATION_KEY);
                LunchIOActivity.session.put("wish","down");
                mPresenter.doCritique(LunchIOActivity.session);
                break;
            default:
                //critique event
                critiqueAmenities(String.valueOf(view.getTag()));

        }
    }

    private void sendComment(String comment){
        String baseURL = FormatUtils.getHostURL(this);
        LunchAPI api = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
        Map<String, String> data = new HashMap<>();
        data.put("user", AccountUtils.getFacebookId(this));
        data.put("dish", String.valueOf(currentDish.getId()));
        data.put("comment", comment);

        //TODO call API in code block commented. This is a fake comment.
        Comment commented = new Comment();
        String avatar = FormatUtils.buildAvatarFBUrl(DishDetailActivity.this);
        commented.setAvatarURL(avatar);
        try {
            JSONObject profile = AccountUtils.getUserProfile(DishDetailActivity.this);
            String name = profile.getString(AccountUtils.USER_NAME);
            commented.setUsername(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        commented.setComment(comment);
        commented.setSubtitle(FormatUtils.getCurrentTime());
        List<Comment> commentList = new ArrayList<Comment>();
        commentList.add(commented);
        displaySpeakersData(commentList);

        /*
        Call<Comment> call = api.comment(data);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                List<Comment> commentList = new ArrayList<Comment>();
                if(response.body() != null){
                    Comment commented = response.body();
                    String avatar = FormatUtils.buildAvatarFBUrl(DishDetailActivity.this);
                    commented.setAvatarURL(avatar);
                    try {
                        JSONObject profile = AccountUtils.getUserProfile(DishDetailActivity.this);
                        String name = profile.getString(AccountUtils.USER_NAME);
                        commented.setUsername(name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    commentList.add(commented);
                    displaySpeakersData(commentList);
                }else {
                    Toast.makeText(DishDetailActivity.this, "Post comment thất bại!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(DishDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        */
    }

    private void saveToFavorite(final String dish, String uid){
        String baseURL = FormatUtils.getHostURL(this);
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
                    LOGD(TAG, "saved id = "+dish);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LOGE(TAG, "saved err");
            }
        });
    }

    private void deleteSaved(final String dish, String uid){
        String baseURL = FormatUtils.getHostURL(this);
        LunchAPI api = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
        Map<String, String> data = new HashMap<>();
        data.put("dish", dish);
        data.put("user", uid);
        Call<Object> call = api.delete(data);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body() != null){
                    LOGD(TAG, "saved id = "+dish);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LOGE(TAG, "saved err");
            }
        });
    }

    private void displaySpeakersData(List<Comment> speakers) {
        final ViewGroup speakersGroup = (ViewGroup) findViewById(R.id.comments_block);

        // Remove all existing speakers (everything but first child, which is the header)
//        for (int i = speakersGroup.getChildCount() - 1; i >= 1; i--) {
//            speakersGroup.removeViewAt(i);
//        }

        final LayoutInflater inflater = getLayoutInflater();

        boolean hasSpeakers = false;


        for (final Comment comment : speakers) {

            String speakerHeader = comment.getUsername();

            final View speakerView = inflater
                    .inflate(R.layout.speaker_detail, speakersGroup, false);
            final TextView speakerHeaderView = (TextView) speakerView
                    .findViewById(R.id.username);
            final ImageView speakerImageView = (ImageView) speakerView
                    .findViewById(R.id.speaker_image);
            final TextView subtitle = (TextView) speakerView.findViewById(R.id.subtitle);
            final TextView speakerAbstractView = (TextView) speakerView
                    .findViewById(R.id.comment_text);


            if (!TextUtils.isEmpty(comment.getAvatarURL()) && imageLoader != null) {
                imageLoader.loadImage(comment.getAvatarURL(), speakerImageView);
            }

            speakerHeaderView.setText(speakerHeader);
            subtitle.setText(comment.getSubtitle());
            speakerAbstractView.setText(comment.getComment());

            speakersGroup.addView(speakerView);
            hasSpeakers = true;
        }

        speakersGroup.setVisibility(hasSpeakers ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(saved){
            saveToFavorite(String.valueOf(currentDish.getId()), AccountUtils.getFacebookId(this));
        }else {
            deleteSaved(String.valueOf(currentDish.getId()), AccountUtils.getFacebookId(this));
        }
    }
}
