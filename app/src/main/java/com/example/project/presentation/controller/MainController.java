package com.example.project.presentation.controller;

import android.content.SharedPreferences;


import com.example.project.data.ACNHVillagerAPI;
import com.example.project.data.Constants;
import com.example.project.presentation.model.RestVillagerResponse;
import com.example.project.presentation.model.Villager;
import com.example.project.presentation.view.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainController {
    private static SharedPreferences sharedPreferences;
    public static Gson gson;
    private MainActivity view;
    public static List<Integer> favoriteList;
    public static List<Integer> leftList;
    public static List<Integer> onIslandList;
    public static List<Integer> mysteryIslandList;
    public static List<Integer> campingList;

    public MainController(MainActivity mainActivity, Gson gson, SharedPreferences sharedPreferences, List<Integer> favoriteList,
                          List<Integer> leftList, List<Integer> onIslandList, List<Integer> mysteryIslandList,
                          List<Integer> campingList) {
        this.view = mainActivity;
        this.gson=gson;
        this.sharedPreferences=sharedPreferences;
        this.favoriteList=favoriteList;
    }

    public void onStart(){

        List<Villager> villagerList = getDataFromCache();
        favoriteList = getFavoriteFromCache();
        if(favoriteList==null){
            favoriteList= new ArrayList<>();
        }
        leftList = getLeftFromCache();
        if(leftList==null){
            leftList= new ArrayList<>();
        }
        onIslandList = getIslandFromCache();
        if(onIslandList==null){
            onIslandList= new ArrayList<>();
        }
        mysteryIslandList = getMysteryFromCache();
        if(mysteryIslandList==null){
            mysteryIslandList= new ArrayList<>();
        }
        campingList = getCampsiteFromCache();
        if(campingList==null){
            campingList= new ArrayList<>();
        }

        if(villagerList !=null) {
            view.showList(villagerList);
        }else{
            makeApiCall();
        }
    }


    private List<Villager> getDataFromCache() {
        String jsonVillager = sharedPreferences.getString(Constants.KEY_VILLAGER_LIST, null);
        if(jsonVillager==null){
            return null;
        }else{
            Type listType = new TypeToken<List<Villager>>(){}.getType();
            return gson.fromJson(jsonVillager, listType);
        }


    }

    private List<Integer> getFavoriteFromCache() {
        String jsonFavorite = sharedPreferences.getString(Constants.KEY_FAVORITE_LIST, null);
        if(jsonFavorite==null){
            return null;
        }else{
            Type listType = new TypeToken<List<Integer>>(){}.getType();
            return gson.fromJson(jsonFavorite, listType);
        }


    }
    private List<Integer> getCampsiteFromCache() {
        String jsonFavorite = sharedPreferences.getString(Constants.KEY_CAMPSITE_LIST, null);
        if(jsonFavorite==null){
            return null;
        }else{
            Type listType = new TypeToken<List<Integer>>(){}.getType();
            return gson.fromJson(jsonFavorite, listType);
        }


    }
    private List<Integer> getLeftFromCache() {
        String jsonFavorite = sharedPreferences.getString(Constants.KEY_LEFT_LIST, null);
        if(jsonFavorite==null){
            return null;
        }else{
            Type listType = new TypeToken<List<Integer>>(){}.getType();
            return gson.fromJson(jsonFavorite, listType);
        }


    }
    private List<Integer> getMysteryFromCache() {
        String jsonFavorite = sharedPreferences.getString(Constants.KEY_MYSTERY_LIST, null);
        if(jsonFavorite==null){
            return null;
        }else{
            Type listType = new TypeToken<List<Integer>>(){}.getType();
            return gson.fromJson(jsonFavorite, listType);
        }


    }
    private List<Integer> getIslandFromCache() {
        String jsonFavorite = sharedPreferences.getString(Constants.KEY_ISLAND_LIST, null);
        if(jsonFavorite==null){
            return null;
        }else{
            Type listType = new TypeToken<List<Integer>>(){}.getType();
            return gson.fromJson(jsonFavorite, listType);
        }


    }




    private void makeApiCall(){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ACNHVillagerAPI villagerAPI = retrofit.create(ACNHVillagerAPI.class);

        Call<RestVillagerResponse> call = villagerAPI.getVillagerResponse();
        call.enqueue(new Callback<RestVillagerResponse>() {
            @Override
            public void onResponse(Call<RestVillagerResponse> call, Response<RestVillagerResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<Villager> villagerList = response.body().getResults();
                    saveVillagerList(villagerList);
                    view.showList(villagerList);
                }
            }

            @Override
            public void onFailure(Call<RestVillagerResponse> call, Throwable t) {
                view.showError();
            }
        });

    }


    private void saveVillagerList(List<Villager> villagerList) {
        String jsonString = gson.toJson(villagerList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_VILLAGER_LIST, jsonString)
                .apply();
    }
    public static void saveFavoriteList(List<Integer> favoriteList) {
        String jsonString = gson.toJson(favoriteList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_FAVORITE_LIST, jsonString)
                .apply();
    }
    public static void saveMysteryList(List<Integer> mysteryList) {
        String jsonString = gson.toJson(mysteryList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_MYSTERY_LIST, jsonString)
                .apply();
    }
    public static void saveLeftList(List<Integer> leftList) {
        String jsonString = gson.toJson(leftList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_LEFT_LIST, jsonString)
                .apply();
    }
    public static void saveIslandList(List<Integer> islandList) {
        String jsonString = gson.toJson(islandList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_ISLAND_LIST, jsonString)
                .apply();
    }
    public static void saveCampsiteList(List<Integer> campsiteList) {
        String jsonString = gson.toJson(campsiteList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_CAMPSITE_LIST, jsonString)
                .apply();
    }


}
