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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.warnstudio.growtopiamarketplace.Adapter.HomeLastFiveUserAdapter;
import com.warnstudio.growtopiamarketplace.Adapter.UserAdapter;
import com.warnstudio.growtopiamarketplace.AddNewNoticeActivity;
import com.warnstudio.growtopiamarketplace.EditProfileActivity;
import com.warnstudio.growtopiamarketplace.MainActivity;
import com.warnstudio.growtopiamarketplace.Models.Post;
import com.warnstudio.growtopiamarketplace.Models.SliderModel;
import com.warnstudio.growtopiamarketplace.Models.User;
import com.warnstudio.growtopiamarketplace.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class HomeFragment extends Fragment {

    private TextView usernameTextView;
    private String profileid;
    FirebaseUser firebaseUser;
    private SliderLayout sliderLayout;

    String image1,image2,image3,image4;
    String link1,link2,link3,link4;

    Context contexts = null;

    ImageView categoriesButtonImageView;
    Dialog categoriesDialog;

    ImageView worldLockVend_ImageView;

    TextView categoryBlockTextView, categorySeedTextView, categoryWorldTextView, categoryServicesTextView,
            categoryStoresTextView, categoryPacksTextView, categoryRentTextView,categoryUtilitiesTextView,
            categoryBFGTextView,categoryClothesTextView,categorySurgeryTextView,categoryFishingTextView,
            categoryIOTMTextView,categoryFestivalTextView, categoryDailyQuestTextView;


    RecyclerView recyclerView;
    private HomeLastFiveUserAdapter userAdapter;
    private List<User> mUsers;
    Query reference;

    private int sliderNumberTotal = 0;


    @Override
    public void onStart() {
        super.onStart();
        userInfoDemo();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


         categoryBlockTextView = view.findViewById(R.id.categoryBlockTextView);
         categorySeedTextView = view.findViewById(R.id.categorySeedTextView);
         categoryWorldTextView = view.findViewById(R.id.categoryWorldTextView);
         categoryServicesTextView = view.findViewById(R.id.categoryServicesTextView);
         categoryStoresTextView = view.findViewById(R.id.categoryStoresTextView);
         categoryPacksTextView = view.findViewById(R.id.categoryPacksTextView);
         categoryRentTextView = view.findViewById(R.id.categoryRentTextView);
         categoryUtilitiesTextView = view.findViewById(R.id.categoryUtilitiesTextView);
         categoryBFGTextView = view.findViewById(R.id.categoryBFGTextView);
         categoryClothesTextView = view.findViewById(R.id.categoryClothesTextView);
         categorySurgeryTextView = view.findViewById(R.id.categorySurgeryTextView);
         categoryFishingTextView = view.findViewById(R.id.categoryFishingTextView);
         categoryIOTMTextView = view.findViewById(R.id.categoryIOTMTextView);
         categoryFestivalTextView = view.findViewById(R.id.categoryFestivalTextView);
         categoryDailyQuestTextView = view.findViewById(R.id.categoryDailyQuestTextView);

         recyclerView = view.findViewById(R.id.last_recyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            mUsers = new ArrayList<>();
            userAdapter = new HomeLastFiveUserAdapter(getContext(), mUsers);
            recyclerView.setAdapter(userAdapter);


         categoriesButtonSendingData();

        worldLockVend_ImageView = view.findViewById(R.id.worldLockVend_ImageView);
        addnewNoticePage();


        contexts = getActivity().getApplicationContext();
        usernameTextView = view.findViewById(R.id.usernameTextView);
        categoriesButtonImageView = view.findViewById(R.id.categoriesButtonImageView);
        categoriesDialog = new Dialog(getContext());

        sliderLayout = view.findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(7);
        getSliderINFO();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profileid =  firebaseUser.getUid();


        categoriesButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCategoriesShowerDialog();
            }
        });

        readUser();

        return view;
    }

    private void readUser() {


            reference = FirebaseDatabase.getInstance()
                    .getReference("users").child("")
                    .limitToLast(5);

        reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mUsers.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (!user.getImageurl().equals("")){
                            mUsers.add(user);
                        }
                    }

                   // Collections.reverse(mUsers);
                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }

    private void addnewNoticePage() {

        worldLockVend_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewNoticeActivity.class);
                startActivity(intent);
                CustomIntent.customType(getActivity(),"left-to-right");
                getActivity().finish();
            }
        });

    }

    private void categoriesButtonSendingData() {

        categoryBlockTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "block");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categorySeedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "seed");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryWorldTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "world");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });



        categoryDailyQuestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "dailyquest");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();
            }
        });

        categoryServicesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "services");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryStoresTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "stores");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryPacksTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "packs");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryRentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "rent");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryUtilitiesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "utilities");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryBFGTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "bfg");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryClothesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "clothes");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categorySurgeryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "surgery");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryFishingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "fishing");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryIOTMTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "iotm");
                editor.apply();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryFestivalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "festival");
                editor.apply();



                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

    }

    private void allCategoriesShowerDialog() {
        categoriesDialog.setContentView(R.layout.all_categories_menu);
        categoriesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView categoryBlockTextView = categoriesDialog.findViewById(R.id.categoryBlockTextView);
        TextView categorySeedTextView = categoriesDialog.findViewById(R.id.categorySeedTextView);
        TextView categoryWorldTextView = categoriesDialog.findViewById(R.id.categoryWorldTextView);
        TextView categoryServicesTextView = categoriesDialog.findViewById(R.id.categoryServicesTextView);
        TextView categoryStoresTextView = categoriesDialog.findViewById(R.id.categoryStoresTextView);
        TextView categoryPacksTextView = categoriesDialog.findViewById(R.id.categoryPacksTextView);
        TextView categoryRentTextView = categoriesDialog.findViewById(R.id.categoryRentTextView);
        TextView categoryUtilitiesTextView = categoriesDialog.findViewById(R.id.categoryUtilitiesTextView);
        TextView categoryBFGTextView = categoriesDialog.findViewById(R.id.categoryBFGTextView);
        TextView categoryClothesTextView = categoriesDialog.findViewById(R.id.categoryClothesTextView);
        TextView categorySurgeryTextView = categoriesDialog.findViewById(R.id.categorySurgeryTextView);
        TextView categoryFishingTextView = categoriesDialog.findViewById(R.id.categoryFishingTextView);
        TextView categoryIOTMTextView = categoriesDialog.findViewById(R.id.categoryIOTMTextView);
        TextView categoryFestivalTextView = categoriesDialog.findViewById(R.id.categoryFestivalTextView);
        TextView categoryDailyQuestTextView = categoriesDialog.findViewById(R.id.categoryDailyQuestTextView);

        Button cancelButton = categoriesDialog.findViewById(R.id.cancelButton);

        categoryBlockTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "block");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categorySeedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "seed");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryWorldTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "world");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryDailyQuestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("dailyquest", "world");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryServicesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "services");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryStoresTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "stores");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryPacksTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "packs");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryRentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "rent");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryUtilitiesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "utilities");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryBFGTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "bfg");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryClothesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "clothes");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categorySurgeryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "surgery");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryFishingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "fishing");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryIOTMTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "iotm");
                editor.apply();
                categoriesDialog.dismiss();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });

        categoryFestivalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("categories", "festival");
                editor.apply();

                categoriesDialog.dismiss();

                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new fragment_categories_shower()).addToBackStack("").commit();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesDialog.dismiss();
            }
        });

        categoriesDialog.show();

    }

    private void getSliderINFO() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profileid = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("slider");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }

                SliderModel sliderModel = snapshot.getValue(SliderModel.class);

                image1 = sliderModel.getImage1();
                image2 = sliderModel.getImage2();
                image3 = sliderModel.getImage3();
                image4 = sliderModel.getImage4();


                link1 = sliderModel.getLink1();
                link2 = sliderModel.getLink2();
                link3 = sliderModel.getLink3();
                link4 = sliderModel.getLink4();

                for (int i = 0; i <= 3; i++){

                    DefaultSliderView sliderView = new DefaultSliderView(contexts);

                    switch (i){
                        case 0:
                            sliderView.setImageUrl(image1);
                            break;
                        case 1:
                            sliderView.setImageUrl(image2);
                            break;

                        case 2:
                            sliderView.setImageUrl(image3);
                            break;

                        case 3:
                            sliderView.setImageUrl(image4);
                            break;


                    }

                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    final int finalI = i;
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                            sliderNumberTotal = finalI + 1;
                            switch (sliderNumberTotal){
                                case 1:
                                    Toast.makeText(getContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                                    Intent linkonebrowse = new Intent(Intent.ACTION_VIEW, Uri.parse(""+link1));
                                    startActivity(linkonebrowse);
                                    break;

                                case 2:
                                    Toast.makeText(getContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                                    Intent linktwobrowse = new Intent(Intent.ACTION_VIEW, Uri.parse(""+link2));
                                    startActivity(linktwobrowse);
                                    break;

                                case 3:
                                    Toast.makeText(getContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                                    Intent linkthreebrowse = new Intent(Intent.ACTION_VIEW, Uri.parse(""+link3));
                                    startActivity(linkthreebrowse);
                                    break;

                                case 4:
                                    Toast.makeText(getContext(), ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                                    Intent linkfourbrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(""+link3));
                                    startActivity(linkfourbrowser);
                                    break;

                                default:
                                    Toast.makeText(getContext(), ""+getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
                                    break;
                            }


                        }
                    });

                    sliderLayout.addSliderView(sliderView);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfoDemo() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profileid =  firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }

                User user = snapshot.getValue(User.class);
                usernameTextView.setText(user.getUsername()+"!");

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}




