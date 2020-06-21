package com.example.project.presentation.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.project.data.Constants;
import com.example.project.R;
import com.example.project.presentation.controller.MainController;
import com.example.project.presentation.model.Villager;
import com.squareup.picasso.Picasso;

import static com.example.project.presentation.controller.MainController.campingList;
import static com.example.project.presentation.controller.MainController.favoriteList;
import static com.example.project.presentation.controller.MainController.leftList;
import static com.example.project.presentation.controller.MainController.mysteryIslandList;
import static com.example.project.presentation.controller.MainController.onIslandList;

public class DetailsActivity extends AppCompatActivity {
    private TextView villagerName;
    private TextView villagerPersoSpec;
    private TextView villagerCatchphrase;
    private TextView villagerGenderBirthday;
    private ImageView villagerImage;
    private CheckBox campingCheck;
    private CheckBox leftCheck;
    private CheckBox mysteryCheck;
    private CheckBox islandCheck;
    private int villagerID;
    private ConstraintLayout layout;
    private Switch favoriteSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_item);

        villagerName = findViewById(R.id.currentVillagerName);
        villagerCatchphrase = findViewById(R.id.currentVillagerCatchphrase);
        villagerPersoSpec = findViewById(R.id.currentVillagerPersoSpec);
        villagerGenderBirthday = findViewById(R.id.currentVillagerGenderBirthday);
        villagerImage = findViewById(R.id.currentVillagerImage);
        campingCheck = findViewById(R.id.campingCheckBox);
        islandCheck = findViewById(R.id.onIslandCheckBox);
        mysteryCheck = findViewById(R.id.mysteryIslandCheckBox);
        leftCheck = findViewById(R.id.leftCheckBox);
        layout = findViewById(R.id.detailsLayout);
        favoriteSwitch = findViewById(R.id.favoriteSwitch);



        Intent intent = getIntent();
        String villagerJson = intent.getStringExtra(Constants.KEY_VILLAGER);
        Villager villager = MainController.gson.fromJson(villagerJson, Villager.class);
        villagerID = villager.getId();

        favoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("UseValueOf")
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    favoriteList.add(villagerID);
                    MainActivity.mAdapter.notifyDataSetChanged();
                    MainController.saveFavoriteList(favoriteList);
                }
                if(!isChecked){
                    favoriteList.remove(new Integer(villagerID));
                    MainActivity.mAdapter.notifyDataSetChanged();
                    MainController.saveFavoriteList(favoriteList);
                }
            }
        });
        showDetails(villager);
    }

    @SuppressLint("SetTextI18n")
    private void showDetails(Villager villager) {
        villagerName.setText(villager.getName());
        villagerCatchphrase.setText("\""+villager.getCatchphrase()+"\"");
        villagerPersoSpec.setText("A "+villager.getPersonality()+" "+villager.getSpecies()+"!");
        String pronoun = "their";
        if(villager.getGender().equals("Male")){
            pronoun = "his";
        }
        if(villager.getGender().equals("Female")){
            pronoun = "her";
        }

        villagerGenderBirthday.setText( "Dont forget "+pronoun+" birthday on "+villager.getBirthday());
        String stringID = String.valueOf(villager.getId());
        String path = "http://acnhapi.com/v1/images/villagers/" + stringID;
        Picasso.get().load(path).into(villagerImage);
        if(campingList.contains(villagerID)){
            campingCheck.setChecked(true);
        }
        if(leftList.contains(villagerID)){
            leftCheck.setChecked(true);
            islandCheck.setEnabled(false);
        }
        if(onIslandList.contains(villagerID)){
            islandCheck.setChecked(true);
            leftCheck.setEnabled(false);
        }
        if(mysteryIslandList.contains(villagerID)){
            mysteryCheck.setChecked(true);
        }
        if(favoriteList.contains(villagerID)){
            favoriteSwitch.setChecked(true);
            favoriteList.remove(Integer.valueOf(villagerID));
            MainActivity.mAdapter.notifyDataSetChanged();
            MainController.saveFavoriteList(favoriteList);
        }
        int myColor = Color.WHITE;
        switch(villager.getPersonality()){
            case "Lazy":
                myColor = getResources().getColor(R.color.lazyBackgroundColor);
                break;
            case "Cranky":
                myColor = getResources().getColor(R.color.crankyBackgroundColor);
                break;
            case "Snooty":
                myColor = getResources().getColor(R.color.snootyBackgroundColor);
                break;
            case "Normal":
                myColor = getResources().getColor(R.color.normalBackgroundColor);
                break;
            case "Peppy":
                myColor = getResources().getColor(R.color.peppyBackgroundColor);
                break;
            case "Uchi":
                myColor = getResources().getColor(R.color.uchiBackgroundColor);
                break;
            case "Smug":
                myColor = getResources().getColor(R.color.smugBackgroundColor);
                break;
            case "Jock":
                myColor = getResources().getColor(R.color.jockBackgroundColor);
                break;
        }
        layout.setBackgroundColor(myColor);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.campingCheckBox:
                if (checked) {
                    campingList.add(villagerID);
                    MainController.saveCampsiteList(campingList);
                }else{
                    campingList.remove(Integer.valueOf(villagerID));
                    MainController.saveCampsiteList(campingList);
                }
                break;
            case R.id.leftCheckBox:
                if (checked) {
                    leftList.add(villagerID);
                    islandCheck.setEnabled(false);
                    MainController.saveLeftList(leftList);
                }else{
                    leftList.remove(Integer.valueOf(villagerID));
                    islandCheck.setEnabled(true);
                    MainController.saveLeftList(leftList);
                }
                break;
            case R.id.onIslandCheckBox:
                if (checked) {
                    onIslandList.add(villagerID);
                    leftCheck.setEnabled(false);
                    MainController.saveIslandList(onIslandList);
                }else{
                    onIslandList.remove(Integer.valueOf(villagerID));
                    leftCheck.setEnabled(true);
                    MainController.saveIslandList(onIslandList);
                }
                break;
            case R.id.mysteryIslandCheckBox:
                if (checked) {
                    mysteryIslandList.add(villagerID);
                    MainController.saveMysteryList(mysteryIslandList);
                }else{
                    mysteryIslandList.remove(Integer.valueOf(villagerID));
                    MainController.saveMysteryList(mysteryIslandList);
                }
                break;
        }
    }
}
