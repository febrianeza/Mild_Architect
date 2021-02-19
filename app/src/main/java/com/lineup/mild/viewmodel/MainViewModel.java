package com.lineup.mild.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lineup.mild.api.ApiService;
import com.lineup.mild.api.BaseApi;
import com.lineup.mild.model.Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    public MutableLiveData<List<Model>> randomList = new MutableLiveData<>();
    public MutableLiveData<List<Model>> recentList = new MutableLiveData<>();

    public MainViewModel() {
        loadRandomList();
        loadRecentList();
    }

    public void refresh() {
        loadRandomList();
        loadRecentList();
    }

    private void loadRecentList() {
        ApiService recentService = BaseApi.getRetrofit().create(ApiService.class);
        final Call<List<Model>> call = recentService.getRecent();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful()) {
                    recentList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.e("MainViewModel", "onFailure: " + t.getLocalizedMessage());

                //send empty array
                List<Model> emptyList = new ArrayList<>();
                recentList.postValue(emptyList);
            }
        });
    }

    private void loadRandomList() {
        ApiService service = BaseApi.getRetrofit().create(ApiService.class);
        Call<List<Model>> call = service.getRandom();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful()) {
                    randomList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.e("MainViewModel", "onFailure: " + t.getLocalizedMessage());

                //send empty array
                List<Model> emptyList = new ArrayList<>();
                randomList.postValue(emptyList);
            }
        });
    }
}
