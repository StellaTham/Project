package com.example.project.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project.data.Constants;
import com.example.project.R;
import com.example.project.data.ACNHVillagerAPI;
import com.example.project.presentation.model.RestVillagerResponse;
import com.example.project.presentation.model.Villager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private boolean darkMode = false;
    private static SharedPreferences sharedPreferences;
    public static Gson gson;
    public static List<Integer> favoriteList= new ArrayList<>();
    public static List<Integer> onIslandList = new ArrayList<>();
    public static List<Integer> mysteryIslandList= new ArrayList<>();
    public static List<Integer> leftList= new ArrayList<>();
    public static List<Integer> campingList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("application_acnh", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();
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
            showList(villagerList);
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


    private void showList(final List<Villager> villagerList) {
        recyclerView = findViewById(R.id.recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // define an adapter
        mAdapter = new ListAdapter(villagerList, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Villager item) {
                navigateToDetails(item);
            }
        });
        recyclerView.setAdapter(mAdapter);

        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        doYourUpdate();
                    }
                }
        );

    }

    private void doYourUpdate() {

        /*RelativeLayout layout = findViewById(R.id.rowLayout);
        TextView nameText = findViewById(R.id.firstLine);
        TextView favoriteText = findViewById(R.id.secondLine);
        if(darkMode == false){
            darkMode = true;
            layout.setBackgroundColor(Color.BLACK);
            nameText.setTextColor(Color.WHITE);
            favoriteText.setTextColor(Color.WHITE);

        }
        if(darkMode == true){
            darkMode = false;
            layout.setBackgroundColor(Color.WHITE);
            nameText.setTextColor(Color.DKGRAY);
            favoriteText.setTextColor(Color.DKGRAY);
        }
        recyclerView.invalidate();
*/
        mySwipeRefreshLayout.setRefreshing(false);
    }

    private static final String BASE_URL = "https://raw.githubusercontent.com/StellaTham/Project/master/";

    private void makeApiCall(){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
                    showList(villagerList);
                    }
            }

            @Override
            public void onFailure(Call<RestVillagerResponse> call, Throwable t) {
                showError();
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

    private void showError(){
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }

    public void navigateToDetails(Villager villager){
        Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class);
        myIntent.putExtra(Constants.KEY_VILLAGER, gson.toJson(villager)); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }
}
