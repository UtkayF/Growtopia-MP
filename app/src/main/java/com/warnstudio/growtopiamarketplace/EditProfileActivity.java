package com.warnstudio.growtopiamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.warnstudio.growtopiamarketplace.Models.User;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView circleProfileImage;
    TextView circleProfileImageText;
    MaterialButton cancelButton, changeButton;
    EditText growid_edittext, mainworld_edittext, instagram_edittext, telegram_edittext;

    FirebaseUser firebaseUser;
    String profileid;

    Dialog dialog;

    String goesPhotolink;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().hide();

        dialog = new Dialog(EditProfileActivity.this);

        goesPhotolink = "null";

        circleProfileImage = findViewById(R.id.circleProfileImage);
        cancelButton = findViewById(R.id.cancelButton);
        changeButton = findViewById(R.id.changeButton);
        growid_edittext = findViewById(R.id.growid_edittext);
        mainworld_edittext = findViewById(R.id.mainworld_edittext);
        instagram_edittext = findViewById(R.id.instagram_edittext);
        telegram_edittext = findViewById(R.id.telegram_edittext);
        circleProfileImageText = findViewById(R.id.circleProfileImageText);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid","none");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        userInfoDemo();

        circleProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickForPhoto();
            }
        });

        circleProfileImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickForPhoto();
            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(a);
                CustomIntent.customType(EditProfileActivity.this,"right-to-left");
                finish();
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButton.setEnabled(false);
                String usernames = growid_edittext.getText().toString();

                if (TextUtils.isEmpty(usernames)){
                    Toast.makeText(EditProfileActivity.this, ""+getText(R.string.bosbirakilamaz), Toast.LENGTH_SHORT).show();
                    changeButton.setEnabled(true);
                }else if(usernames.length() >= 1 && usernames.length() <= 13){

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("users").orderByChild("username").equalTo(usernames)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    DatabaseReference ad = FirebaseDatabase.getInstance().getReference("users").child(profileid);
                                    ad.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);

                                            if (usernames.equals(user.getUsername())) {
                                                changeInformation(growid_edittext.getText().toString(),
                                                        mainworld_edittext.getText().toString().toUpperCase(),
                                                        instagram_edittext.getText().toString(),
                                                        telegram_edittext.getText().toString());
                                            } else {
                                                if (dataSnapshot.exists()) {
                                                    Toast.makeText(EditProfileActivity.this, ""+getText(R.string.usingusername), Toast.LENGTH_SHORT).show();
                                                    changeButton.setEnabled(true);
                                                } else {
                                                    changeInformation(growid_edittext.getText().toString(),
                                                            mainworld_edittext.getText().toString().toUpperCase(),
                                                            instagram_edittext.getText().toString(),
                                                            telegram_edittext.getText().toString());
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }

            }
        });



    }

    private void clickForPhoto() {

        dialog.setContentView(R.layout.profile_photo_chooser);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton profile0_ImageButton = dialog.findViewById(R.id.profile0_ImageButton);
        ImageButton profile1_ImageButton = dialog.findViewById(R.id.profile1_ImageButton);
        ImageButton profile2_ImageButton = dialog.findViewById(R.id.profile2_ImageButton);
        ImageButton profile3_ImageButton = dialog.findViewById(R.id.profile3_ImageButton);
        ImageButton profile5_ImageButton = dialog.findViewById(R.id.profile5_ImageButton);
        ImageButton profile6_ImageButton = dialog.findViewById(R.id.profile6_ImageButton);
        ImageButton profile7_ImageButton = dialog.findViewById(R.id.profile7_ImageButton);
        ImageButton profile8_ImageButton = dialog.findViewById(R.id.profile8_ImageButton);
        ImageButton profile9_ImageButton = dialog.findViewById(R.id.profile9_ImageButton);
        ImageButton profile10_ImageButton = dialog.findViewById(R.id.profile10_ImageButton);
        ImageButton profile11_ImageButton = dialog.findViewById(R.id.profile11_ImageButton);
        ImageButton profile12_ImageButton = dialog.findViewById(R.id.profile12_ImageButton);
        ImageButton profile13_ImageButton = dialog.findViewById(R.id.profile13_ImageButton);
        ImageButton profile14_ImageButton = dialog.findViewById(R.id.profile14_ImageButton);

        Button okButton = dialog.findViewById(R.id.changeBButton);
        Button cancelButton = dialog.findViewById(R.id.cancelBButton);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String foundedImage;
                foundedImage = user.getImageurl();
                if (foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_1.jpg?alt=media&token=bfbdd912-1d9d-4387-b85b-a29e3cbe5e11")){
                    profile1_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_10.jpg?alt=media&token=b05c50f6-7897-49c6-9f20-a256a19eec60")){
                    profile10_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_11.jpg?alt=media&token=5de912f5-1390-443e-97c4-709a6ad088cc")){
                    profile11_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_12.jpg?alt=media&token=a79587ff-5829-499c-8ff7-f95e6d317bd2")){
                    profile12_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_13.jpg?alt=media&token=560616af-32f0-478e-84a2-c5e029bcf8d1")){
                    profile13_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_14.jpg?alt=media&token=20c3dc4b-1866-444b-9012-62898cd348a3")){
                    profile14_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_2.jpg?alt=media&token=1439b923-e903-482e-ae80-8b8d8a71dd61")){
                    profile2_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_3.jpg?alt=media&token=8002dd8f-9e81-48d3-8aca-ee55ccee442a")){
                    profile3_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_5.jpg?alt=media&token=e50f469c-77e4-4e14-98e6-ce85570e56b1")){
                    profile5_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_6.jpg?alt=media&token=0a5aa82e-8c07-4a7d-9983-42cf9b541330")){
                    profile6_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_7.jpg?alt=media&token=1c7a7402-3a78-49fd-9792-8671308d3f1e")){
                    profile7_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_8.jpg?alt=media&token=91db2246-2cfa-4d60-a886-cd00f4ae0180")){
                    profile8_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if(foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_9.jpg?alt=media&token=096b8052-4f76-4da1-8728-94df16aa468a")){
                    profile9_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else if (foundedImage.equals("https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_image_starter.jpg?alt=media&token=61d0467e-5e30-4881-ad36-d4624b3bea1e")){
                    profile0_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile0_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_image_starter.jpg?alt=media&token=61d0467e-5e30-4881-ad36-d4624b3bea1e";
                profile0_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile1_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_1.jpg?alt=media&token=bfbdd912-1d9d-4387-b85b-a29e3cbe5e11";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile2_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_2.jpg?alt=media&token=1439b923-e903-482e-ae80-8b8d8a71dd61";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile3_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_3.jpg?alt=media&token=8002dd8f-9e81-48d3-8aca-ee55ccee442a";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile3_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile5_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_5.jpg?alt=media&token=e50f469c-77e4-4e14-98e6-ce85570e56b1";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile6_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_6.jpg?alt=media&token=0a5aa82e-8c07-4a7d-9983-42cf9b541330";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile7_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_7.jpg?alt=media&token=1c7a7402-3a78-49fd-9792-8671308d3f1e";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile8_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_8.jpg?alt=media&token=91db2246-2cfa-4d60-a886-cd00f4ae0180";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile9_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_9.jpg?alt=media&token=096b8052-4f76-4da1-8728-94df16aa468a";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile10_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_10.jpg?alt=media&token=b05c50f6-7897-49c6-9f20-a256a19eec60";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile11_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_11.jpg?alt=media&token=5de912f5-1390-443e-97c4-709a6ad088cc";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile12_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_12.jpg?alt=media&token=a79587ff-5829-499c-8ff7-f95e6d317bd2";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile13_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_13.jpg?alt=media&token=560616af-32f0-478e-84a2-c5e029bcf8d1";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });

        profile14_ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goesPhotolink = "https://firebasestorage.googleapis.com/v0/b/growtopiamarketplace01-10-2021.appspot.com/o/Profile_IMAGE%2Fprofile_14.jpg?alt=media&token=20c3dc4b-1866-444b-9012-62898cd348a3";
                profile0_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile1_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile2_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile14_ImageButton.setBackgroundResource(R.color.boardcancelcolor);
                profile3_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile5_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile6_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile7_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile8_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile10_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile9_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile11_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile13_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
                profile12_ImageButton.setBackgroundResource(R.drawable.bullettin_board_layout);
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void changePhoto() {
        if (goesPhotolink.equals("null")){
            dialog.dismiss();
            Intent a = new Intent(EditProfileActivity.this, MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(a);
            CustomIntent.customType(EditProfileActivity.this,"right-to-left");
        }else{

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("imageurl",goesPhotolink);

            reference.updateChildren(hashMap);

            Intent a = new Intent(EditProfileActivity.this, MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(a);
            CustomIntent.customType(EditProfileActivity.this,"right-to-left");

        }

    }

    private void changeInformation(String username, String mainworld_name, String instagram_name, String telegram_name) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("username",username);
        hashMap.put("mainworld",mainworld_name);
        hashMap.put("instagram",instagram_name);
        hashMap.put("telegram",telegram_name);

        reference.updateChildren(hashMap);

        Intent a = new Intent(EditProfileActivity.this, MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(a);
        CustomIntent.customType(EditProfileActivity.this,"right-to-left");

    }

    private void userInfoDemo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(circleProfileImage);
                growid_edittext.setText(user.getUsername());
                mainworld_edittext.setText(user.getMainworld());
                instagram_edittext.setText(user.getInstagram());
                telegram_edittext.setText(user.getTelegram());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(EditProfileActivity.this, MainActivity.class);
        startActivity(a);
        CustomIntent.customType(EditProfileActivity.this,"right-to-left");
        finish();
    }

}





