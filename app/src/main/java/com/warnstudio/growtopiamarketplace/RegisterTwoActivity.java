package com.warnstudio.growtopiamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

import maes.tech.intentanim.CustomIntent;

public class RegisterTwoActivity extends AppCompatActivity {

    private EditText growid_edittext,mainworld_edittext,instagram_edittext,telegram_edittext;
    private MaterialButton readyButton;

    //Firebase
    private FirebaseAuth auth;
    private DatabaseReference reference;
    boolean ozel;
    int controlNum = 0;

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String uid = firebaseUser.getUid();
        //Toast.makeText(this, ""+uid, Toast.LENGTH_SHORT).show();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",uid);
        hashMap.put("username","");
        hashMap.put("imageurl","");
        hashMap.put("mainworld","");
        hashMap.put("instagram","");
        hashMap.put("telegram","");
        hashMap.put("point","0");
        hashMap.put("admin","false");

        reference.setValue(hashMap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();



        specialFinds();

        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyButton.setEnabled(false);
                //Toast.makeText(getApplicationContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();

                String str_username = growid_edittext.getText().toString();
                String str_mainworld = mainworld_edittext.getText().toString().toUpperCase();
                String str_instagram = instagram_edittext.getText().toString();
                String str_telegram = telegram_edittext.getText().toString();

                if (TextUtils.isEmpty(str_username)){
                    Toast.makeText(RegisterTwoActivity.this, ""+getText(R.string.bosbirakilamaz), Toast.LENGTH_SHORT).show();
                }else if(str_username.length() >= 1 && str_username.length() <= 13){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("users")
                            .orderByChild("username").equalTo(str_username)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //    Toast.makeText(RegisterTwoActivity.this, ""+snapshot.exists(), Toast.LENGTH_SHORT).show();

                                    boolean exists = snapshot.exists();
                                    if (exists) {
                                        Toast.makeText(RegisterTwoActivity.this, ""+getText(R.string.usingusername), Toast.LENGTH_SHORT).show();
                                        readyButton.setEnabled(true);
                                    } else if (!(exists)) {
                                       // Toast.makeText(getApplicationContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                                        readyAction(str_username, str_mainworld,str_instagram,str_telegram);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(RegisterTwoActivity.this, ""+getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private void readyAction(String str_username, String str_mainworld, String str_instagram, String str_telegram) {

        Toast.makeText(RegisterTwoActivity.this, ""+getText(R.string.againopen), Toast.LENGTH_LONG).show();

        readyButton.setEnabled(false);

        Random randomGen = new Random();
        int randomNumber = randomGen.nextInt(14);
        String image_link;



        switch (randomNumber){
            case 0:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_7.jpg?alt=media&token=1c7a7402-3a78-49fd-9792-8671308d3f1e";
                break;

            case 1:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_image_starter.jpg?alt=media&token=61d0467e-5e30-4881-ad36-d4624b3bea1e";
                break;

            case 2:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_2.jpg?alt=media&token=1439b923-e903-482e-ae80-8b8d8a71dd61";
                break;


            case 3:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_3.jpg?alt=media&token=8002dd8f-9e81-48d3-8aca-ee55ccee442a";
                break;

            case 4:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_5.jpg?alt=media&token=e50f469c-77e4-4e14-98e6-ce85570e56b1";
                break;

            case 5:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_6.jpg?alt=media&token=0a5aa82e-8c07-4a7d-9983-42cf9b541330";
                break;

            case 6:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_7.jpg?alt=media&token=1c7a7402-3a78-49fd-9792-8671308d3f1e";
                break;

            case 7:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_8.jpg?alt=media&token=91db2246-2cfa-4d60-a886-cd00f4ae0180";
                break;

            case 8:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_9.jpg?alt=media&token=096b8052-4f76-4da1-8728-94df16aa468a";
                break;

            case 9:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_10.jpg?alt=media&token=b05c50f6-7897-49c6-9f20-a256a19eec60";
                break;

            case 10:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_11.jpg?alt=media&token=5de912f5-1390-443e-97c4-709a6ad088cc";
                break;

            case 11:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_12.jpg?alt=media&token=a79587ff-5829-499c-8ff7-f95e6d317bd2";
                break;

            case 12:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_13.jpg?alt=media&token=560616af-32f0-478e-84a2-c5e029bcf8d1";
                break;

            case 13:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_14.jpg?alt=media&token=20c3dc4b-1866-444b-9012-62898cd348a3";
                break;

            case 14:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_image_starter.jpg?alt=media&token=61d0467e-5e30-4881-ad36-d4624b3bea1e";
                break;


            default:
                image_link = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_image_starter.jpg?alt=media&token=61d0467e-5e30-4881-ad36-d4624b3bea1e";
                break;
        }


        FirebaseUser firebaseUser = auth.getCurrentUser();
        String uid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("username",str_username);
        hashMap.put("imageurl",image_link);
        hashMap.put("mainworld",str_mainworld);
        hashMap.put("instagram",str_instagram);
        hashMap.put("telegram",str_telegram);

        reference.updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            System.exit(0);
                            onDestroy();

                        }else{
                            Toast.makeText(RegisterTwoActivity.this, ""+getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
                            readyButton.setEnabled(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterTwoActivity.this, ""+getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
                readyButton.setEnabled(true);
            }
        });

    }

    private void specialFinds() {
        growid_edittext = findViewById(R.id.growid_edittext);
        mainworld_edittext = findViewById(R.id.mainworld_edittext);
        instagram_edittext = findViewById(R.id.instagram_edittext);
        telegram_edittext = findViewById(R.id.telegram_edittext);
        readyButton = findViewById(R.id.readyButton);
    }



}