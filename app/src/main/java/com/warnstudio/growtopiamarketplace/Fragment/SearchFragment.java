package com.warnstudio.growtopiamarketplace.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.warnstudio.growtopiamarketplace.Adapter.UserAdapter;
import com.warnstudio.growtopiamarketplace.MainActivity;
import com.warnstudio.growtopiamarketplace.Models.User;
import com.warnstudio.growtopiamarketplace.R;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class SearchFragment extends Fragment implements BackFragment {

    public boolean onBackPressed() {

        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
        CustomIntent.customType(getActivity(),"left-to-right");

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    private EditText search_bar;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        search_bar = view.findViewById(R.id.search_bar);
        progressBar = view.findViewById(R.id.progress_circular);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);
        recyclerView.setAdapter(userAdapter);


        readUsers();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void searchUsers(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("username").limitToLast(60)
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataShapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataShapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    if (!user.getImageurl().equals("")){
                        mUsers.add(user);
                    }

                }
                progressBar.setVisibility(View.GONE);
                // Collections.reverse(mUsers);
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        Query ad = FirebaseDatabase.getInstance().getReference("users").limitToLast(60);
        ad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_bar.getText().toString().equals("")){
                    mUsers.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        if (!user.getImageurl().equals("")){
                            mUsers.add(user);
                        }
                    }

                    progressBar.setVisibility(View.GONE);
                    // Collections.reverse(mUsers);
                    userAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}










