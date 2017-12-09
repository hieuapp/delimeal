package com.hieuapp.lunch.data.source;

import com.hieuapp.lunch.api.DishResponse;
import com.hieuapp.lunch.api.LunchAPI;
import com.hieuapp.lunch.api.RetrofitClient;
import com.hieuapp.lunch.data.Dish;
import com.hieuapp.lunch.data.DishCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hieuapp.lunch.util.LogUtils.LOGD;

/**
 * Created by hieuapp on 26/02/2017.
 */

public class DishRepository implements DataSource {

    private static DishRepository dishRepository;

    private final DataSource remoteSource;
    private final DataSource localSource;

    private DishRepository(DataSource dishsRemoteDataSource,
                          DataSource dishsLocalDataSource){
        if(dishRepository != null){
            throw new RuntimeException("Using getInstance() insteadof constructor");
        }

        localSource = dishsLocalDataSource;
        remoteSource = dishsRemoteDataSource;
    }

    public static DishRepository getInstance(DataSource dishsRemoteSource,
                                             DataSource dishsLocalSource){
        if(dishRepository == null){
            synchronized (DishRepository.class){
                if(dishRepository == null){
                    dishRepository = new DishRepository(dishsRemoteSource, dishsLocalSource);
                }
            }
        }

        return dishRepository;
    }

    @Override
    public void getAllItems(final LoadDataListener loadCallback) {
        remoteSource.getAllItems(loadCallback);
    }

    @Override
    public void getOnce(int id, LoadDataListener loadDataListener) {
        //TODO check local data if not then remote to server
        remoteSource.getOnce(id, loadDataListener);

    }
}
