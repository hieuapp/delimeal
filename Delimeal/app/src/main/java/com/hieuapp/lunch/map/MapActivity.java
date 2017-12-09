package com.hieuapp.lunch.map;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hieuapp.lunch.BaseActivity;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.dishes.DishDetailContract;
import com.hieuapp.lunch.dishes.DishesAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hieuapp on 29/04/2017.
 */

public class MapActivity extends BaseActivity implements MapViewFragment.MarkerCallbacks{

    public static final String BUNDLE_STATE_MAPVIEW = "mapview";

    private MapViewFragment mMapFragment;
    private RecyclerView markerDetailList;
    private DishesAdapter dishesAdapter;
    private Map<String, Integer> mapPosition = new HashMap<>();

    public static int posActive = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        mMapFragment = (MapViewFragment) fm.findFragmentByTag("map");

        setContentView(R.layout.map_act);
        markerDetailList = (RecyclerView)findViewById(R.id.marker_detail_list);
        dishesAdapter = new DishesAdapter(this,
                MarkerHelper.getInstance().getDishData(), DishesAdapter.MAP_VIEW_MODE);
        setMapPosition(MarkerHelper.getInstance().getDishData());
        markerDetailList.setAdapter(dishesAdapter);
        overridePendingTransition(0, 0);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the mapview state in a separate bundle parameter
        final Bundle mapviewState = new Bundle();
        mMapFragment.onSaveInstanceState(mapviewState);
        outState.putBundle(BUNDLE_STATE_MAPVIEW, mapviewState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(mMapFragment == null){

            if(savedInstanceState != null){
                //Restore state
                Bundle previousState = savedInstanceState.getBundle(BUNDLE_STATE_MAPVIEW);
                mMapFragment = MapViewFragment.newInstance(previousState);
            }else {
                mMapFragment = MapViewFragment.newInstance();
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_map, mMapFragment, "map").commit();
        }
    }

    @Override
    public void switchNextCandidate(String id) {
        deselectedItem(posActive);
        posActive = mapPosition.get(id);
        markerDetailList.smoothScrollToPosition(posActive);
        markerDetailList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                selectedItem(posActive);
            }
        });
    }

    private void setMapPosition(List<Dish> dishList){
        for( int i = 0; i < dishList.size(); i++){
            Dish dish = dishList.get(i);
            mapPosition.put(String.valueOf(dish.getId()), i);
        }
    }

    private void selectedItem(int pos){
        if(pos >= 0){
            DishesAdapter.DishViewHolder itemView = (DishesAdapter.DishViewHolder) markerDetailList
                    .findViewHolderForAdapterPosition(pos);
            if(itemView != null){
                itemView.lineActive.setVisibility(View.VISIBLE);
            }
        }
    }

    private void deselectedItem(int pos){
        if(pos >= 0){
            DishesAdapter.DishViewHolder itemView = (DishesAdapter.DishViewHolder) markerDetailList
                    .findViewHolderForAdapterPosition(pos);
            if(itemView != null){
                itemView.lineActive.setVisibility(View.GONE);
            }
        }
    }
}
