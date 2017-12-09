package com.hieuapp.lunch.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hieuapp.lunch.R;
import com.hieuapp.lunch.util.Mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hieuapp on 26/05/2017.
 */

public class SearchOptionsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ListView listSetting;
    private SearchSettingAdapter settingAdapter;
    private List<SearchSetting> searchSettingList;
    private Spinner spPrice, spRating, spDistance;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_options_fragment, container, false);

        listSetting = (ListView)root.findViewById(R.id.search_setting);
        searchSettingList = setupSettingList();
        settingAdapter = new SearchSettingAdapter(getContext(),
                R.layout.list_item_search_setting_layout, searchSettingList);
        listSetting.setAdapter(settingAdapter);

        spPrice = (Spinner)root.findViewById(R.id.sp_price);
        spDistance = (Spinner)root.findViewById(R.id.sp_distance);
        spRating = (Spinner)root.findViewById(R.id.sp_rating);
        spPrice.setOnItemSelectedListener(this);
        spRating.setOnItemSelectedListener(this);
        spRating.setOnItemSelectedListener(this);

        setupSpinnerOption();

        return root;
    }

    public List<SearchSetting> getOptionsList(){
        return searchSettingList;
    }
    public String getRangePrice(){
        return (String)spPrice.getSelectedItem();
    }

    public String getMaxDistance(){
        return (String)spDistance.getSelectedItem();
    }

    public String getMinRating(){
        return (String)spRating.getSelectedItem();
    }

    public SparseBooleanArray getChecked(){
        return settingAdapter.getListChecked();
    }

    private List<SearchSetting> setupSettingList(){
        Resources resources = this.getResources();
        SearchSetting wifi = new SearchSetting(resources.getString(R.string.wifi), Mapping.WIFI);
        SearchSetting parking = new SearchSetting(resources.getString(R.string.free_bike_parking), Mapping.PARKING);
        SearchSetting seat = new SearchSetting(resources.getString(R.string.out_door_seat), Mapping.OUT_DOOR_SEAT);
        SearchSetting air = new SearchSetting(resources.getString(R.string.air_conditioner), Mapping.AIR_COND);
        SearchSetting zone = new SearchSetting(resources.getString(R.string.smoking_zone), Mapping.SMOKING);
        SearchSetting floor = new SearchSetting(resources.getString(R.string.many_floor), Mapping.FLOOR);
        SearchSetting delivery = new SearchSetting(resources.getString(R.string.delivery_service), Mapping.DELIVER);
        SearchSetting celebrate = new SearchSetting(resources.getString(R.string.birthday_celebrate), Mapping.CELEBRATE);
        SearchSetting takeway = new SearchSetting(resources.getString(R.string.take_way), Mapping.TAKE_WAY);

        List<SearchSetting> settingList = new ArrayList<>();
        settingList.add(wifi);
        settingList.add(parking);
        settingList.add(zone);
        settingList.add(takeway);
        settingList.add(seat);
        settingList.add(delivery);
        settingList.add(air);
        settingList.add(floor);
        settingList.add(celebrate);

        return settingList;
    }


    private void setupSpinnerOption(){
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.price_range, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrice.setAdapter(priceAdapter);

        ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.min_rating, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRating.setAdapter(ratingAdapter);

        ArrayAdapter<CharSequence> distanceAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.max_distance, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistance.setAdapter(distanceAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class SearchSettingAdapter extends ArrayAdapter<SearchSetting> implements CompoundButton.OnCheckedChangeListener{

        private List<SearchSetting> settings;
        private int settingLayout;
        private SparseBooleanArray checkedState;

        public SearchSettingAdapter(Context context, int resource, List<SearchSetting> settingList) {
            super(context, resource);
            this.settings = settingList;
            this.settingLayout = resource;
            checkedState = new SparseBooleanArray(settingList.size());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            SettingHolder holder;
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(settingLayout, parent, false);
                holder = new SettingHolder();
            }else {
                holder = (SettingHolder)convertView.getTag();
            }
            holder.feature = (TextView)convertView.findViewById(R.id.feature);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
            holder.checkBox.setTag(position);
            holder.checkBox.setChecked(checkedState.get(position, false));
            holder.checkBox.setOnCheckedChangeListener(this);
            convertView.setTag(holder);

            SearchSetting setting = settings.get(position);
            holder.feature.setText(setting.getLabel());

            return convertView;
        }

        public boolean isChecked(int position) {
            return checkedState.get(position, false);
        }

        public void setChecked(int position, boolean isChecked) {
            checkedState.put(position, isChecked);

        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));

        }

        public SparseBooleanArray getListChecked(){
            return checkedState;
        }

        @Override
        public int getCount(){
            return settings.size();
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            checkedState.put((Integer) compoundButton.getTag(), b);
        }

        class SettingHolder{
            TextView feature;
            CheckBox checkBox;
        }
    }
}
