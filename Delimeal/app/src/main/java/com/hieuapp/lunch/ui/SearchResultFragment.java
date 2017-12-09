package com.hieuapp.lunch.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.dishes.DishDetailActivity;
import com.hieuapp.lunch.util.FormatUtils;
import com.hieuapp.lunch.util.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hieuapp on 26/05/2017.
 */

public class SearchResultFragment extends Fragment {

    private RecyclerView searchResultReclv;
    private SearchResultAdapter adapter;
    CircularProgressView waitting;
    private String query = "";
    private List<Dish> results = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        query = getArguments().getString("q");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.search_result_frag, container, false);
        searchResultReclv = (RecyclerView)root.findViewById(R.id.search_result_list);
        waitting = (CircularProgressView)root.findViewById(R.id.wait_for_search);
        adapter = new SearchResultAdapter(results, getContext());
        searchResultReclv.setAdapter(adapter);

        search(query);
        waitting.setVisibility(View.VISIBLE);
        waitting.startAnimation();
        return root;
    }

    private void search(String query){
        String baseURL = FormatUtils.getHostURL(getContext());
        LunchAPI api = RetrofitClient.getInstance(baseURL).create(LunchAPI.class);
        Call<List<Dish>> call = api.search();
        call.enqueue(new Callback<List<Dish>>() {
            @Override
            public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {
                if(response.body() != null){
                    showSearchResult(response.body());
                }else {
                    String msg = getString(R.string.search_item_error);
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    showSearchResult(null);
                }
            }

            @Override
            public void onFailure(Call<List<Dish>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showSearchResult(List<Dish> result){
        waitting.stopAnimation();
        waitting.setVisibility(View.GONE);
        if(result == null || result.size() == 0){
            String msg = getContext().getResources().getString(R.string.search_not_found);
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            return;
        }

        results.clear();
        for(Dish dish : result){
            results.add(dish);
        }

        adapter.notifyDataSetChanged();
    }

    class SearchResultAdapter extends RecyclerView.Adapter<SearchResultVH>{
        private List<Dish> searchResult;
        private Context mContext;
        private ImageLoader imageLoader;
        public SearchResultAdapter(List<Dish> results, Context context){
            this.searchResult = results;
            this.mContext = context;
            imageLoader = new ImageLoader(context, android.R.color.transparent);
        }

        @Override
        public SearchResultVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_item, parent, false);
            return new SearchResultVH(itemView);
        }

        @Override
        public void onBindViewHolder(SearchResultVH holder, int position) {
            final Dish dish = searchResult.get(position);
            holder.title.setText(dish.getFoodName());
            holder.subTitle.setText(dish.getShortAddress());
            imageLoader.loadImage(dish.getImage(), holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DishDetailActivity.class);
                    intent.putExtra("dish_id", dish.getId());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return searchResult.size();
        }
    }

    class SearchResultVH extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title, subTitle;

        public SearchResultVH(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.thumbnail);
            title = (TextView)itemView.findViewById(R.id.title);
            subTitle = (TextView)itemView.findViewById(R.id.subtitle);
        }
    }
}
