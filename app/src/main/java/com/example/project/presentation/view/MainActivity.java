package com.example.project.presentation.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project.data.Constants;
import com.example.project.R;
import com.example.project.presentation.controller.MainController;
import com.example.project.presentation.model.Villager;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


import static com.example.project.presentation.controller.MainController.gson;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



    private MainController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MainController(
                this,
                new GsonBuilder()
                .setLenient()
                .create(),
                getSharedPreferences("application_acnh", Context.MODE_PRIVATE),
                        new ArrayList<Integer>(),
                        new ArrayList<Integer>(),
                        new ArrayList<Integer>(),
                        new ArrayList<Integer>(),
                        new ArrayList<Integer>())
        ;
        controller.onStart();

    }

    public void showList(final List<Villager> villagerList) {
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


    }





    public void showError(){
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }

    public void navigateToDetails(Villager villager){
        Intent myIntent = new Intent(MainActivity.this, DetailsActivity.class);
        myIntent.putExtra(Constants.KEY_VILLAGER, gson.toJson(villager)); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }
}
