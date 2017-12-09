package com.hieuapp.lunch.dishes;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hieuapp.lunch.LunchIOActivity;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.Tabs;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishCategory;
import com.hieuapp.lunch.data.source.DishRepository;
import com.hieuapp.lunch.data.source.local.DishLocalData;
import com.hieuapp.lunch.data.source.remote.DishRemoteData;
import com.hieuapp.lunch.map.MapActivity;
import com.hieuapp.lunch.ui.widget.recyclerview.ItemMarginDecoration;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.Mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by hieuapp on 26/02/2017.
 */

public class ExploreDishesFragment extends Fragment implements DishesContract.View,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private DishesContract.Presenter mPresenter;

    private RecyclerView recyclerCategories;

    @Override
    public void showListDish(List<Dish> dishes) {

    }

    private List<DishCategory> categoriesData = new ArrayList<>();

    private CategoriesAdapter categoriesAdapter;

    private final int REQUEST_LOCATION_CODE = 1994;

    CircularProgressView progressbar;
    private GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;

    private FloatingActionButton fabMapMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.explorer_dishes_fragment, container, false);
        recyclerCategories = (RecyclerView) root.findViewById(R.id.recycler_categories);
        recyclerCategories.setHasFixedSize(true);
        int coursesVerticalMargin = getResources().getDimensionPixelSize(R.dimen.spacing_micro);
        recyclerCategories.addItemDecoration(new ItemMarginDecoration(0, coursesVerticalMargin,
                0, coursesVerticalMargin));

        categoriesAdapter = new CategoriesAdapter(getContext(), categoriesData);
        recyclerCategories.setAdapter(categoriesAdapter);
        recyclerCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy < 0) {
                    fabMapMode.show();
                } else if (dy > 0) {
                    fabMapMode.hide();
                }
            }
        });

        progressbar = (CircularProgressView) root.findViewById(R.id.load_dish_progress);
        fabMapMode = (FloatingActionButton) root.findViewById(R.id.fab_map_mode);
        fabMapMode.setOnClickListener(this);

        getActivity().setTitle(getResources().getString(R.string.explore));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //set this fragment is active
        LunchIOActivity.CURRENT_TAB = Tabs.EXPLORER;
    }

    @Override
    public void onStart() {
        if(categoriesData.size() == 0){
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    private void initPresenter() {
        DishRemoteData dishRemoteData = DishRemoteData.getInstance(getContext());
        DishLocalData dishLocalData = DishLocalData.getInstance(getContext());
        DishRepository dishRepository = DishRepository.getInstance(dishRemoteData, dishLocalData);

        mPresenter = new DishesPresenter(dishRepository, this);
    }

    @Override
    public void showCategories(List<DishCategory> categories) {
        progressbar.stopAnimation();
        progressbar.setVisibility(View.GONE);
        if (categories == null) {
            //TODO show message load data failed
            Toast.makeText(getContext(), "Cannot load data", Toast.LENGTH_LONG).show();
            return;
        }
        categoriesData.clear();
        for (int i = 0; i < categories.size(); i++) {
            categoriesData.add(categories.get(i));
        }
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGoogleApiClient.connect();
                } else {
                    Toast.makeText(getContext(), "Location permission deny", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_CODE);
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        initPresenter();
        mPresenter.loadDishes(true);
        progressbar.setVisibility(View.VISIBLE);
        progressbar.startAnimation();
    }

    public static Location getLocation(){
        Location location = new Location("Location demo.");
        location.setLatitude(37.447617);
        location.setLongitude(-122.159599);
        return location;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab_map_mode){
            Intent intent = new Intent(getContext(), MapActivity.class);
            startActivity(intent);
        }
    }

    public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>{

        private Context mContext;

        private List<DishCategory> dishCategories = null;
        public CategoriesAdapter(Context context, List<DishCategory> categories){
            this.mContext = context;
            this.dishCategories = categories;
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_category, parent, false);
            return new CategoryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            final DishCategory category = dishCategories.get(position);
            holder.categoryName.setText(category.getCategoryName());
            holder.categorySize.setText(category.getNumberDishes() + " items");

            DishesAdapter dishesAdapter = new DishesAdapter(mContext, category.getDishes(),
                    DishesAdapter.LIST_VIEW_MODE);
            holder.dishList.setAdapter(dishesAdapter);

            holder.seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ListAllDishActivity.class);
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(),
                            R.anim.act_animation, R.anim.act_animation2).toBundle();
                    intent.putExtra("category", category.getDishes().get(0).getCategoryId());
                    startActivity(intent, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dishCategories.size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder{

            TextView categoryName;
            TextView categorySize;
            RecyclerView dishList;
            LinearLayout seeAll;

            public CategoryViewHolder(View itemView) {
                super(itemView);
                categoryName = (TextView)itemView.findViewById(R.id.category_name);
                categorySize = (TextView)itemView.findViewById(R.id.number_dish);
                dishList = (RecyclerView)itemView.findViewById(R.id.listDishes);
                seeAll = (LinearLayout) itemView.findViewById(R.id.see_all_action);
            }
        }

    }
}
