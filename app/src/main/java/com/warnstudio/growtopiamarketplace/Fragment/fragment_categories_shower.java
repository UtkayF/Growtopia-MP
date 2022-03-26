package com.warnstudio.growtopiamarketplace.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.warnstudio.growtopiamarketplace.Adapter.PostAdapter;
import com.warnstudio.growtopiamarketplace.Models.Post;
import com.warnstudio.growtopiamarketplace.R;

import java.util.ArrayList;
import java.util.List;

public class fragment_categories_shower extends Fragment {

    String showingCategories;

    TextView sellLogoText, buyLogoText;
    TextView my_sell, my_buy,sell_buy_bar1;

    RecyclerView recycler_view_sell, recycler_view_buy;
    ImageView outoforderImageView;



    int lastLimit = 30;
    String listeningBS;
    TextView limitUp;

    private PostAdapter postAdapter;
    private List<Post> postLists;

    boolean controlForPost = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_shower, container, false);


        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        showingCategories = prefs.getString("categories","none");

        sellLogoText = view.findViewById(R.id.sellLogoText);
        buyLogoText = view.findViewById(R.id.buyLogoText);
        outoforderImageView = view.findViewById(R.id.outoforderImageView);
        limitUp = view.findViewById(R.id.limitUp);
        my_sell = view.findViewById(R.id.my_sell);
        my_buy = view.findViewById(R.id.my_buy);
        recycler_view_sell = view.findViewById(R.id.recycler_view_sell);
        recycler_view_buy = view.findViewById(R.id.recycler_view_buy);

        sell_buy_bar1 = view.findViewById(R.id.sell_buy_bar1);
        sell_buy_bar1.setText(showingCategories);

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

                readPost();

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

                readPost();

            }
        });

        limitUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastLimit = lastLimit + 30;

                Query query = FirebaseDatabase.getInstance().getReference("shop")
                        .child(listeningBS).child(showingCategories).limitToLast(lastLimit);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postLists.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            postLists.add(post);
                        }

                        switch ((int) dataSnapshot.getChildrenCount()){
                            case 0:
                                outoforderImageView.setVisibility(View.VISIBLE);
                                limitUp.setVisibility(View.GONE);
                                break;

                            default:
                                outoforderImageView.setVisibility(View.GONE);
                                limitUp.setVisibility(View.VISIBLE);
                                break;
                        }

                        postAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        readPost();

        return view;
    }

    private void sellRecyclerOptions() {
        recycler_view_sell.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_view_sell.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recycler_view_sell.setAdapter(postAdapter);
    }

    private void buyRecyclerOptions() {
        recycler_view_buy.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_view_buy.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recycler_view_buy.setAdapter(postAdapter);
    }

    private void readPost() {

        Query query = FirebaseDatabase.getInstance().getReference("shop")
                .child(listeningBS).child(showingCategories).limitToLast(lastLimit);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postLists.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    postLists.add(post);
                }

                switch ((int) snapshot.getChildrenCount()){
                    case 0:
                        outoforderImageView.setVisibility(View.VISIBLE);
                        limitUp.setVisibility(View.GONE);
                        break;

                    default:
                        outoforderImageView.setVisibility(View.GONE);
                        limitUp.setVisibility(View.VISIBLE);
                        break;
                }

                postAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}







