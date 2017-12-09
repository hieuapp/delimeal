package com.hieuapp.lunch.saved;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hieuapp.lunch.LunchIOActivity;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.Tabs;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishCategory;
import com.hieuapp.lunch.data.source.DishRepository;
import com.hieuapp.lunch.data.source.local.DishLocalData;
import com.hieuapp.lunch.data.source.remote.DishRemoteData;
import com.hieuapp.lunch.dishes.DishDetailActivity;
import com.hieuapp.lunch.dishes.DishesContract;
import com.hieuapp.lunch.dishes.DishesPresenter;
import com.hieuapp.lunch.ui.widget.CollectionView;
import com.hieuapp.lunch.ui.widget.CollectionViewCallbacks;
import com.hieuapp.lunch.util.AccountUtils;
import com.hieuapp.lunch.util.ImageLoader;
import com.hieuapp.lunch.util.UIUtils;

import java.util.List;
import java.util.Map;

import static com.hieuapp.lunch.util.LogUtils.LOGD;
import static com.hieuapp.lunch.util.LogUtils.LOGE;
import static com.hieuapp.lunch.util.LogUtils.makeLogTag;

/**
 * Created by hieuapp on 27/02/2017.
 */

public class SavedFragment extends Fragment implements CollectionViewCallbacks, DishesContract.View{

    private static final String TAG = makeLogTag(SavedFragment.class);
    private CollectionView mCollectionView;
    private View mEmptyView;
    private DishesContract.Presenter mPresenter;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initPresenter();
        String uid = AccountUtils.getFacebookId(getContext());
        mPresenter.loadDishSaved(uid);
        getActivity().setTitle(getResources().getString(R.string.saved));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.saved_fragment, container, false);
        mCollectionView = (CollectionView)root.findViewById(R.id.dish_saved_collection);
        mEmptyView = root.findViewById(R.id.empty);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageLoader = new ImageLoader(getActivity(), R.color.grey_200);
        //Do something when created
    }

    @Override
    public void onResume(){
        super.onResume();
        //set this fragment is active
        LunchIOActivity.CURRENT_TAB = Tabs.SAVED;

    }

    @Override
    public View newCollectionHeaderView(Context context, int groupId, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.list_item_explore_header, parent, false);
    }

    @Override
    public void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel, Object headerTag) {
        ((TextView) view.findViewById(android.R.id.text1)).setText(headerLabel);
    }

    @Override
    public View newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.video_library_item, parent, false);
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag) {
        final Dish dish = (Dish)tag;
        if(dish == null){
            LOGE(TAG, "Dish in bindCollectionItemView null cmnr");
            return;
        }

        ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView address = (TextView) view.findViewById(R.id.address);

        address.setText(dish.getShortAddress());
        LinearLayout textContainer = (LinearLayout)view.findViewById(R.id.text_container);
        if(dataIndex % 3 == 1){
            textContainer.setBackgroundColor(context.getResources().getColor(R.color.grey_100));
        }else if(dataIndex % 3 == 2){
            textContainer.setBackgroundColor(context.getResources().getColor(R.color.grey_200));
        }

        titleView.setText(dish.getFoodName());
        String thumbUrl = dish.getImage() + "?w=480";
        if (TextUtils.isEmpty(thumbUrl)) {
            thumbnailView.setImageResource(android.R.color.transparent);
        } else {
            mImageLoader.loadImage(thumbUrl, thumbnailView);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DishDetailActivity.class);
                intent.putExtra("dish_id", dish.getId());
                getContext().startActivity(intent);
            }
        });
    }

    private void initPresenter() {
        DishRemoteData dishRemoteData = DishRemoteData.getInstance(getContext());
        DishLocalData dishLocalData = DishLocalData.getInstance(getContext());
        DishRepository dishRepository = DishRepository.getInstance(dishRemoteData, dishLocalData);

        mPresenter = new DishesPresenter(dishRepository, this);
    }

    @Override
    public void showCategories(List<DishCategory> categories) {

    }

    @Override
    public void showListDish(List<Dish> dishes) {
        CollectionView.Inventory inventory = new CollectionView.Inventory();
        int numColumns = UIUtils.calculateNumColumns(getActivity());
        CollectionView.InventoryGroup curGroup = new CollectionView.InventoryGroup(0)
                .setDataIndexStart(0)
                .setShowHeader(false)
                .setDisplayCols(numColumns);

        for(int i = 0; i < dishes.size(); i++){
            curGroup.addItemWithTag(dishes.get(i));
        }

        if(curGroup.getRowCount() > 0){
            inventory.addGroup(curGroup);
        }

        mCollectionView.setCollectionAdapter(this);
        mCollectionView.updateInventory(inventory);

        mEmptyView.setVisibility(dishes.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
