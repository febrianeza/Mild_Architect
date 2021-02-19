package com.lineup.mild.api;

import com.lineup.mild.model.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("random")
    Call<List<Model>> getRandom();

    @GET("recent")
    Call<List<Model>> getRecent();
}
