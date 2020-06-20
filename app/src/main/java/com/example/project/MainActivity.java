package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    public static Gson gson;
    public List<Integer> favoriteList;
    public List<Integer> onIslandList;
    public List<Integer> mysteryIslandList;
    public List<Integer> leftList;
    public List<Integer> campingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("application_acnh", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .setLenient()
                .create();
        List<Villager> villagerList = getDataFromCache();
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

    //TODO others getXfromCache

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

        /*ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        villagerList.remove(viewHolder.getAdapterPosition());
                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);*/


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
        //TODO update on refresh
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
        Toast.makeText(getApplicationContext(), "Villager list saved", Toast.LENGTH_SHORT).show();
    }
    private void saveFavoriteList(List<Integer> favoriteList) {
        String jsonString = gson.toJson(favoriteList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_FAVORITE_LIST, jsonString)
                .apply();
        Toast.makeText(getApplicationContext(), "Favorite list saved", Toast.LENGTH_SHORT).show();
    }
    private void saveMysteryList(List<Integer> mysteryList) {
        String jsonString = gson.toJson(mysteryList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_MYSTERY_LIST, jsonString)
                .apply();
        Toast.makeText(getApplicationContext(), "Mystery list saved", Toast.LENGTH_SHORT).show();
    }
    private void saveLeftList(List<Integer> leftList) {
        String jsonString = gson.toJson(leftList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_LEFT_LIST, jsonString)
                .apply();
        Toast.makeText(getApplicationContext(), "Left list saved", Toast.LENGTH_SHORT).show();
    }
    private void saveIslandList(List<Integer> islandList) {
        String jsonString = gson.toJson(islandList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_ISLAND_LIST, jsonString)
                .apply();
        Toast.makeText(getApplicationContext(), "On island list saved", Toast.LENGTH_SHORT).show();
    }
    private void saveCampsiteList(List<Integer> campsiteList) {
        String jsonString = gson.toJson(campsiteList);
        sharedPreferences
                .edit()
                .putString(Constants.KEY_CAMPSITE_LIST, jsonString)
                .apply();
        Toast.makeText(getApplicationContext(), "Campsite list saved", Toast.LENGTH_SHORT).show();
    }

    private void showError(){
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }

    public void navigateToDetails(Villager villager){
        Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class);
        myIntent.putExtra("villagerKey", gson.toJson(villager)); //Optional parameters
        MainActivity.this.startActivity(myIntent);
        //Toast.makeText(getApplicationContext(), "TODO NAVIGATE", Toast.LENGTH_SHORT).show();
    }
}
