package com.warnstudio.growtopiamarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import maes.tech.intentanim.CustomIntent;

public class PageChoosenActivity extends AppCompatActivity {

    private MaterialButton goLoginPage, goRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_choosen);
        getSupportActionBar().hide();


        goLoginPage = findViewById(R.id.goLoginPage);
        goRegisterPage = findViewById(R.id.goRegisterPage);

        goLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageChoosenActivity.this,LoginActivity.class);
                startActivity(intent);
                CustomIntent.customType(PageChoosenActivity.this,"left-to-right");
                finish();
            }
        });

        goRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageChoosenActivity.this,RegisterActivity.class);
                startActivity(intent);
                CustomIntent.customType(PageChoosenActivity.this,"left-to-right");
                finish();
            }
        });

    }
}