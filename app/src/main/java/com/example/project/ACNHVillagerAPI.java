package com.example.project;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ACNHVillagerAPI {
    @GET("fakeVillagerAPI.json")
    Call<RestVillagerResponse> getVillagerResponse();
}
