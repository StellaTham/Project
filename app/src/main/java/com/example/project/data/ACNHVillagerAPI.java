package com.example.project.data;

import com.example.project.presentation.model.RestVillagerResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ACNHVillagerAPI {
    @GET("fakeVillagerAPI.json")
    Call<RestVillagerResponse> getVillagerResponse();
}
