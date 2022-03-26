package com.warnstudio.growtopiamarketplace.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.warnstudio.growtopiamarketplace.Adapter.MyPostAdapter;
import com.warnstudio.growtopiamarketplace.Adapter.PostAdapter;
import com.warnstudio.growtopiamarketplace.EditProfileActivity;
import com.warnstudio.growtopiamarketplace.LoginActivity;
import com.warnstudio.growtopiamarketplace.MainActivity;
import com.warnstudio.growtopiamarketplace.Models.Post;
import com.warnstudio.growtopiamarketplace.Models.User;
import com.warnstudio.growtopiamarketplace.PageChoosenActivity;
import com.warnstudio.growtopiamarketplace.R;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class ProfileFragment extends Fragment implements BackFragment {


    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }

    CircleImageView profile_image;
    TextView usernameTextView, my_sell, my_buy;

    FirebaseUser firebaseUser;
    String profileid;

    TextView sellLogoText, buyLogoText;
    RecyclerView recycler_view_sell, recycler_view_buy;

    ImageView profile_wrench_image, user_wrench_image;
    ImageView left_admin, right_admin;

    Dialog dialog;

    String postAdapterTF,listeningBS;

    MyPostAdapter mypostAdapter;
    List<Post> postList;
    boolean controlForPost = false;

    ImageView outoforderImageView;
    int specialControlForNull;

    private RewardedAd mRewardedAd;
    private String TAG = "---->ADS STATUS: ";
    private int rewardNumber = 0;
    private int rewardTotal = 0;
    private String whatsWrench = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        whatsWrench = "null";

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid","none");
        postAdapterTF = prefs.getString("postAdapterTF","false");

        if (postAdapterTF.equals("")) {
            postAdapterTF = "false";
        }

        specialControlForNull = 0;


        profile_image = view.findViewById(R.id.profile_image);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        left_admin = view.findViewById(R.id.left_admin);
        right_admin = view.findViewById(R.id.right_admin);
        my_sell = view.findViewById(R.id.my_sell);
        my_buy = view.findViewById(R.id.my_buy);

        sellLogoText = view.findViewById(R.id.sellLogoText);
        buyLogoText = view.findViewById(R.id.buyLogoText);
        recycler_view_sell = view.findViewById(R.id.recycler_view_sell);
        recycler_view_buy = view.findViewById(R.id.recycler_view_buy);
        user_wrench_image = view.findViewById(R.id.user_wrench_image);
        profile_wrench_image = view.findViewById(R.id.profile_wrench_image);
        outoforderImageView = view.findViewById(R.id.outoforderImageView);

        dialog = new Dialog(getContext());


        userInfo();

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadNewRewardedProfile();

            }
        });

        if(profileid.equals(firebaseUser.getUid())){ profile_wrench_image.setVisibility(View.VISIBLE); }
        else{ user_wrench_image.setVisibility(View.VISIBLE); }

        profile_wrench_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsWrench = "profile";
               // Toast.makeText(getActivity(), ""+whatsWrench, Toast.LENGTH_SHORT).show();
                showRewardedAdProfile();
            }
        });

        user_wrench_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsWrench = "user";
             //   Toast.makeText(getActivity(), ""+whatsWrench, Toast.LENGTH_SHORT).show();
                showRewardedAdProfile();

            }
        });

        listeningBS = "sell";
        if (controlForPost == false){
            sellRecyclerOptions();
            controlForPost = true;
        }

        my_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_sell.setBackgroundResource(R.drawable.bulettin_board_edittext_io);
                my_buy.setBackgroundResource(R.drawable.bullettin_board_edittext);

                listeningBS = "sell";

                recycler_view_sell.setVisibility(View.VISIBLE);
                sellLogoText.setVisibility(View.VISIBLE);

                recycler_view_buy.setVisibility(View.GONE);
                buyLogoText.setVisibility(View.GONE);

                sellRecyclerOptions();
                myPost();
            }
        });
        my_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_sell.setBackgroundResource(R.drawable.bullettin_board_edittext);
                my_buy.setBackgroundResource(R.drawable.bulettin_board_edittext_io);

                listeningBS = "buy";

                recycler_view_sell.setVisibility(View.GONE);
                sellLogoText.setVisibility(View.GONE);

                recycler_view_buy.setVisibility(View.VISIBLE);
                buyLogoText.setVisibility(View.VISIBLE);

                buyRecyclerOptions();
                myPost();
            }
        });

        myPost();





        return view;
    }



    private void loadNewRewardedProfile() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getActivity(), "ca-app-pub-2075225031935302/8272741767",
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
                                loadNewRewardedProfile();
                            }
                        });
                    }
                });

    }

    private void showRewardedAdProfile(){
        if (mRewardedAd != null) {
            mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    int reward = rewardNumber;

                    rewardTotal = reward + rewardAmount;

                    switch (whatsWrench){
                        case "profile":
                            openDialogProfileWrench();
                            break;

                        case "user":
                            openDialogUserWrench();
                            break;

                        default:
                            break;
                    }


                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }




    private void myPost() {
        DatabaseReference reference;

        postList.clear();

        String[] listItem = {"block","seed","world","dailyquest","services","stores","packs","rent","utilities"
                ,"bfg","clothes","surgery","fishing","iotm","festival"};
        for (String i : listItem) {
            reference = FirebaseDatabase.getInstance()
                    .getReference("shop")
                    .child(listeningBS)
                    .child(i);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Post post = dataSnapshot.getValue(Post.class);
                        if (post.getPublisher().equals(profileid)) {
                            postList.add(post);
                            specialControlForNull = specialControlForNull + 1;
                        }
                    }

                    switch (specialControlForNull) {
                        case 0:
                            outoforderImageView.setVisibility(View.VISIBLE);
                            break;

                        default:
                            outoforderImageView.setVisibility(View.GONE);
                            break;
                    }



                    mypostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }


    private void sellRecyclerOptions() {
        recycler_view_sell.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_view_sell.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        mypostAdapter = new MyPostAdapter(getContext(), postList);
        recycler_view_sell.setAdapter(mypostAdapter);
    }


    private void buyRecyclerOptions() {
        recycler_view_buy.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_view_buy.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        mypostAdapter = new MyPostAdapter(getContext(), postList);
        recycler_view_buy.setAdapter(mypostAdapter);
    }


    private void openDialogUserWrench() {
        dialog.setContentView(R.layout.user_detail_layout_wrench);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView usernameTextView = dialog.findViewById(R.id.usernameTextView);
        TextView mainworld_edittext = dialog.findViewById(R.id.mainworld_edittext);
        TextView instagram_edittext = dialog.findViewById(R.id.instagram_edittext);
        TextView telegram_edittext = dialog.findViewById(R.id.telegram_edittext);
        TextView itsLookEmptyText = dialog.findViewById(R.id.itsLookEmptyText);
        MaterialButton okButton = dialog.findViewById(R.id.okButton);

        LinearLayout mainworld_linearlayout = dialog.findViewById(R.id.mainworld_linearlayout);
        LinearLayout instagram_linearlayout = dialog.findViewById(R.id.instagram_linearlayout);
        LinearLayout telegram_linearlayout = dialog.findViewById(R.id.telegram_linearlayout);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null){
                    return;
                }

                User user = snapshot.getValue(User.class);

                usernameTextView.setText(user.getUsername());

                String mainworld, instagram, telegram;
                int controlNumber;

                mainworld = user.getMainworld();
                instagram = user.getInstagram();
                telegram = user.getTelegram();

                controlNumber = 0;

                if (mainworld.equals("")){
                    mainworld_linearlayout.setVisibility(View.GONE);
                    controlNumber++;
                }else{
                    mainworld_edittext.setText(mainworld);
                    mainworld_edittext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "MAIN WORLD", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (instagram.equals("")){
                    instagram_linearlayout.setVisibility(View.GONE);
                    controlNumber++;
                }else{
                    instagram_edittext.setText(instagram);
                    instagram_edittext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(getText(R.string.instagramlinkuser)+""+instagram));
                            startActivity(browse);
                        }
                    });
                }

                if (telegram.equals("")){
                    telegram_linearlayout.setVisibility(View.GONE);
                    controlNumber++;
                }else{
                    telegram_edittext.setText(telegram);
                    telegram_edittext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(getText(R.string.telegramlinkuser)+""+telegram));
                            startActivity(browse);
                        }
                    });
                }

                if (controlNumber == 3){
                    itsLookEmptyText.setVisibility(View.VISIBLE);
                }else{

                }


                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dialog.show();

    }

    private void openDialogProfileWrench() {

        dialog.setContentView(R.layout.profile_detail_layout_wrench);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView usernameTextView = dialog.findViewById(R.id.usernameTextView);
        TextView mainworld_edittext = dialog.findViewById(R.id.mainworld_edittext);
        TextView instagram_edittext = dialog.findViewById(R.id.instagram_edittext);
        TextView telegram_edittext = dialog.findViewById(R.id.telegram_edittext);
        TextView itsLookEmptyText = dialog.findViewById(R.id.itsLookEmptyText);
        MaterialButton editButton = dialog.findViewById(R.id.editButton);
        MaterialButton logoutButton = dialog.findViewById(R.id.logoutButton);

        LinearLayout mainworld_linearlayout = dialog.findViewById(R.id.mainworld_linearlayout);
        LinearLayout instagram_linearlayout = dialog.findViewById(R.id.instagram_linearlayout);
        LinearLayout telegram_linearlayout = dialog.findViewById(R.id.telegram_linearlayout);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null){
                    return;
                }

                User user = snapshot.getValue(User.class);

                usernameTextView.setText(user.getUsername());

                String mainworld, instagram, telegram;
                int controlNumber;

                mainworld = user.getMainworld();
                instagram = user.getInstagram();
                telegram = user.getTelegram();

                controlNumber = 0;

                if (mainworld.equals("")){
                    mainworld_linearlayout.setVisibility(View.GONE);
                    controlNumber++;
                }else{
                    mainworld_edittext.setText(mainworld);
                }

                if (instagram.equals("")){
                    instagram_linearlayout.setVisibility(View.GONE);
                    controlNumber++;
                }else{
                    instagram_edittext.setText(instagram);
                }

                if (telegram.equals("")){
                    telegram_linearlayout.setVisibility(View.GONE);
                    controlNumber++;
                }else{
                    telegram_edittext.setText(telegram);
                }

                if (controlNumber == 3){
                    itsLookEmptyText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                dialog.dismiss();
                CustomIntent.customType(getActivity(),"left-to-right");
                getActivity().finish();



            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), PageChoosenActivity.class);
                startActivity(intent);
                dialog.dismiss();
                CustomIntent.customType(getActivity(),"left-to-right");
                getActivity().finish();

            }
        });



        dialog.show();

    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null){
                    return;
                }

                User user = snapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageurl()).into(profile_image);
                usernameTextView.setText(user.getUsername());

                if (user.getAdmin().equals("true")){
                    left_admin.setVisibility(View.VISIBLE);
                    right_admin.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean onBackPressed() {

        if (postAdapterTF.equals("false")){

            SharedPreferences.Editor editor2 = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            editor2.putString("postAdapterTF","false");
            editor2.apply();

            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
            CustomIntent.customType(getActivity(),"right-to-left");

        }

        if(postAdapterTF.equals("true")){

            SharedPreferences.Editor editor2 = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            editor2.putString("postAdapterTF","false");
            editor2.apply();

            ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new fragment_categories_shower()).commit();


        }

        return true;
    }


}













