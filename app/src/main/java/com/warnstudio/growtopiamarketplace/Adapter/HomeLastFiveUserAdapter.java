package com.warnstudio.growtopiamarketplace.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.warnstudio.growtopiamarketplace.Fragment.ProfileFragment;
import com.warnstudio.growtopiamarketplace.Models.User;
import com.warnstudio.growtopiamarketplace.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeLastFiveUserAdapter extends RecyclerView.Adapter<HomeLastFiveUserAdapter.ViewHolder> {


    private Context mContext;
    private List<User> mUsers;


    private FirebaseUser firebaseUser;


    public HomeLastFiveUserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new HomeLastFiveUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);

        holder.username.setText(user.getUsername());
        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);
        holder.ic_admin_symbol.setVisibility(View.GONE);

        if (user.getAdmin().equals("true")){
            holder.ic_admin_symbol.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", user.getId());
                editor.apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView username;
        public CircleImageView image_profile;
        public ImageView ic_admin_symbol;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.username);
            image_profile = itemView.findViewById(R.id.image_profile);

            ic_admin_symbol = itemView.findViewById(R.id.ic_admin_symbol);


        }
    }

}