package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private TextView villagerName;
    private TextView villagerPersoSpec;
    private TextView villagerCatchphrase;
    private TextView villagerGenderBirthday;
    private ImageView villagerImage;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_item);

        villagerName = findViewById(R.id.currentVillagerName);
        villagerCatchphrase = findViewById(R.id.currentVillagerCatchphrase);
        villagerPersoSpec = findViewById(R.id.currentVillagerPersoSpec);
        villagerGenderBirthday = findViewById(R.id.currentVillagerGenderBirthday);
        villagerImage = findViewById(R.id.currentVillagerImage);

        Intent intent = getIntent();
        String villagerJson = intent.getStringExtra("villagerKey");
        Villager villager = MainActivity.gson.fromJson(villagerJson, Villager.class);
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
    }
}
