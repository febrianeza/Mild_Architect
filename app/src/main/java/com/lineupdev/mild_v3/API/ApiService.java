package com.lineupdev.mild_v3.API;

import com.lineupdev.mild_v3.Model.Model;
import com.lineupdev.mild_v3.Model.ModelPreview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("random.php")
    Call<List<Model>> getRandom();

    @GET("recent.php")
    Call<List<Model>> getRecent();

    @GET("preview.php")
    Call<ModelPreview> getContentFromId(
            @Query("id") String id
    );
}
