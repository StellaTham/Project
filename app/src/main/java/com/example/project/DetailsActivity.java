package com.example.project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import static com.example.project.MainActivity.campingList;
import static com.example.project.MainActivity.leftList;
import static com.example.project.MainActivity.mysteryIslandList;
import static com.example.project.MainActivity.onIslandList;

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

        Intent intent = getIntent();
        String villagerJson = intent.getStringExtra(Constants.KEY_VILLAGER);
        Villager villager = MainActivity.gson.fromJson(villagerJson, Villager.class);
        villagerID = villager.getId();
        showDetails(villager);
    }

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

        villagerGenderBirthday.setText("Dont forget "+pronoun+" birthday on "+villager.getBirthday());
        String stringID = String.valueOf(villager.getId());
        String path = "http://acnhapi.com/v1/images/villagers/" + stringID;
        Picasso.get().load(path).into(villagerImage);
        if(campingList.contains(new Integer(villagerID))){
            campingCheck.setChecked(true);
        }
        if(leftList.contains(new Integer(villagerID))){
            leftCheck.setChecked(true);
            islandCheck.setEnabled(false);
        }
        if(onIslandList.contains(new Integer(villagerID))){
            islandCheck.setChecked(true);
            leftCheck.setEnabled(false);
        }
        if(mysteryIslandList.contains(new Integer(villagerID))){
            mysteryCheck.setChecked(true);
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
                }else{
                    campingList.remove(new Integer(villagerID));
                }
                break;
            case R.id.leftCheckBox:
                if (checked) {
                    leftList.add(villagerID);
                    islandCheck.setEnabled(false);
                }else{
                    leftList.remove(new Integer(villagerID));
                    islandCheck.setEnabled(true);
                }
                break;
            case R.id.onIslandCheckBox:
                if (checked) {
                    onIslandList.add(villagerID);
                    leftCheck.setEnabled(false);
                }else{
                    onIslandList.remove(new Integer(villagerID));
                    leftCheck.setEnabled(true);
                }
                break;
            case R.id.mysteryIslandCheckBox:
                if (checked) {
                    mysteryIslandList.add(villagerID);
                }else{
                    mysteryIslandList.remove(new Integer(villagerID));
                }
                break;
        }
    }
}
