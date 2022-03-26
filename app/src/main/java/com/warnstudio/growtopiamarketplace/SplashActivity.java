package com.warnstudio.growtopiamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.warnstudio.growtopiamarketplace.Models.User;

import maes.tech.intentanim.CustomIntent;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    specialCheck();

                }
            }
        };
        timerThread.start();

    }

    private void specialCheck() {
        if (firebaseUser != null){
            String currentUserID = firebaseUser.getUid();
            if (!currentUserID.equals("")){

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("users").orderByChild("id").equalTo(currentUserID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(currentUserID);
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);
                                            if (user.getUsername().equals("")){
                                                Intent intent = new Intent(SplashActivity.this, RegisterTwoActivity.class);
                                                startActivity(intent);
                                                CustomIntent.customType(SplashActivity.this,"fadein-to-fadeout");
                                                finish();
                                            }else{
                                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                CustomIntent.customType(SplashActivity.this,"fadein-to-fadeout");
                                                finish();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(SplashActivity.this, ""+getText(R.string.afewproblems), Toast.LENGTH_LONG).show();
                                        }
                                    });


                                }else{
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(SplashActivity.this, ""+getText(R.string.suspendedmessage), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SplashActivity.this, PageChoosenActivity.class);
                                    startActivity(intent);
                                    CustomIntent.customType(SplashActivity.this,"fadein-to-fadeout");
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        }else{
            Intent intent = new Intent(SplashActivity.this, PageChoosenActivity.class);
            startActivity(intent);
            CustomIntent.customType(SplashActivity.this,"fadein-to-fadeout");
            finish();
        }
    }
}