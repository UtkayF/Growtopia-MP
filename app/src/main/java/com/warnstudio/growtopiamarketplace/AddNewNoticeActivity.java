package com.warnstudio.growtopiamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import maes.tech.intentanim.CustomIntent;

public class AddNewNoticeActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    Spinner category_spinner,buysell_spinner;
    String category,sellbuy,worldname,itemname,price;

    //CURRENT DATA IN LAST METHOD

    MaterialButton addButton,cancelButton;
    EditText worldname_edittext,itemname_edittext,price_edittext;

    private RewardedAd mRewardedAd;
    private String TAG = "---->ADS STATUS: ";
    private int rewardNumber = 0;
    private int rewardTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_notice);
        getSupportActionBar().hide();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        cancelButton = findViewById(R.id.cancelButton);
        addButton = findViewById(R.id.addButton);
        worldname_edittext = findViewById(R.id.worldname_edittext);
        itemname_edittext = findViewById(R.id.itemname_edittext);
        price_edittext = findViewById(R.id.price_edittext);

        category_spinner = findViewById(R.id.category_spinner);
        buysell_spinner = findViewById(R.id.buysell_spinner);

        ArrayAdapter<CharSequence> charSequenceArrayAdapter = ArrayAdapter.createFromResource
                (this,R.array.categories, R.layout.my_spinner_item);
        charSequenceArrayAdapter.setDropDownViewResource(R.layout.my_spinner_item_drop);
        category_spinner.setAdapter(charSequenceArrayAdapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        category = "block";
                        break;

                    case 2:
                        category = "seed";
                        break;

                    case 3:
                        category = "world";
                        break;

                    case 4:
                        category = "dailyquest";
                        break;

                    case 5:
                        category = "services";
                        break;

                    case 6:
                        category = "stores";
                        break;

                    case 7:
                        category = "packs";
                        break;

                    case 8:
                        category = "rent";
                        break;

                    case 9:
                        category = "utilities";
                        break;

                    case 10:
                        category = "bfg";
                        break;

                    case 11:
                        category = "clothes";
                        break;

                    case 12:
                        category = "surgery";
                        break;

                    case 13:
                        category = "fishing";
                        break;

                    case 14:
                        category = "iotm";
                        break;

                    case 15:
                        category = "festival";
                        break;

                    default:
                        category = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> sequenceArrayAdapter = ArrayAdapter.createFromResource
                (this,R.array.buysellArray, R.layout.my_spinner_item);
        sequenceArrayAdapter.setDropDownViewResource(R.layout.my_spinner_item_drop);
        buysell_spinner.setAdapter(sequenceArrayAdapter);
        buysell_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        sellbuy = "sell";
                        break;

                    case 2:
                        sellbuy = "buy";
                        break;

                    default:
                        sellbuy = "";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadNewRewarded();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton.setEnabled(false);
                worldname = worldname_edittext.getText().toString().toUpperCase();
                itemname = itemname_edittext.getText().toString();
                price = price_edittext.getText().toString();

                if (worldname.length() >= 1){
                    if (itemname.length() >= 1){
                        if (price.length() >= 1){
                            if (sellbuy.length() >= 1){
                                if (category.length() >= 1){
                                    Toast.makeText(getApplicationContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                                    showRewardedAd();
                                }else{
                                    addButton.setEnabled(false);
                                    Toast.makeText(getApplicationContext(), ""+getText(R.string.categorybos), Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                addButton.setEnabled(true);
                                Toast.makeText(getApplicationContext(), ""+getText(R.string.sellbuybos), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            addButton.setEnabled(true);
                            Toast.makeText(getApplicationContext(), ""+getText(R.string.pricebos), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        addButton.setEnabled(true);
                        Toast.makeText(getApplicationContext(), ""+getText(R.string.itemnamebos), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    addButton.setEnabled(true);
                    Toast.makeText(getApplicationContext(), ""+getText(R.string.worldnamebos), Toast.LENGTH_SHORT).show();
                }



            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(AddNewNoticeActivity.this, MainActivity.class);
                startActivity(a);
                CustomIntent.customType(AddNewNoticeActivity.this,"right-to-left");
                finish();
            }
        });

    }

    private void loadNewRewarded() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-2075225031935302/9894629402",
                adRequest, new RewardedAdLoadCallback(){
                    @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError){
                        mRewardedAd = null;
                        Log.d(TAG,""+loadAdError.getMessage());
                        Log.d(TAG,"onAdFailedToLoad");
                    }
                    @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd){
                        mRewardedAd = rewardedAd;
                        Log.d(TAG,"Ad is Loaded");
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                loadNewRewarded();
                            }
                        });
                    }
                });

    }

    private void showRewardedAd(){
        if (mRewardedAd != null) {
            mRewardedAd.show(this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    int reward = rewardNumber;

                    rewardTotal = reward + rewardAmount;

                    uploadPost();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    private void uploadPost() {
        if (rewardTotal >= 1) {
            String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("shop").child(sellbuy).child(category);
            String postid = ref.push().getKey();

            HashMap hashMap = new HashMap();

            hashMap.put("postid", postid);
            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("date", currentDate);
            hashMap.put("worldname", worldname);
            hashMap.put("itemname", itemname);
            hashMap.put("price", price);
            hashMap.put("buyselldemo", sellbuy);
            hashMap.put("type", category);


            ref.child(postid)
                    .setValue(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddNewNoticeActivity.this, "" + getText(R.string.succes), Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(AddNewNoticeActivity.this, MainActivity.class);
                                startActivity(a);
                                CustomIntent.customType(AddNewNoticeActivity.this, "right-to-left");
                                finish();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, ""+getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(AddNewNoticeActivity.this, MainActivity.class);
        startActivity(a);
        CustomIntent.customType(AddNewNoticeActivity.this,"right-to-left");
        finish();
    }
}


