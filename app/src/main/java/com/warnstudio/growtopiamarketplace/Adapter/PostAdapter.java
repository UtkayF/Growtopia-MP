package com.warnstudio.growtopiamarketplace.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.warnstudio.growtopiamarketplace.Fragment.ProfileFragment;
import com.warnstudio.growtopiamarketplace.Models.Post;
import com.warnstudio.growtopiamarketplace.Models.User;
import com.warnstudio.growtopiamarketplace.R;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

        public Context mContext;
        public List<Post> mPost;

        private FirebaseUser firebaseUser;

        public PostAdapter(Context mContext, List<Post> mPost) {
            this.mContext = mContext;
            this.mPost = mPost;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
            return new PostAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Post post = mPost.get(position);

            holder.worldnameTextView.setText(post.getWorldname());
            holder.itemnameTextView.setText(post.getItemname());
            holder.priceTextView.setText(post.getPrice());
            holder.dateTextView.setText(post.getDate());

            publisherInfo(holder.img_profile,
                    holder.usernameTextView,
                    post.getPublisher(),
                    holder.ic_admin_symbol,
                    holder.youcanReach);

            holder.profileLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid",post.getPublisher());
                    editor.apply();

                    SharedPreferences.Editor editor2 = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor2.putString("postAdapterTF","true");
                    editor2.apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).addToBackStack("").commit();



                }
            });

            //isSaved(post.getPostid(),holder.save);
            /*
                        holder.save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (holder.save.getTag().equals("save")){
                                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                                            .child(post.getPostid()).setValue(true);
                                }else{
                                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                                            .child(post.getPostid()).removeValue();
                                }
                            }
                        });

             */
            //SAVE METHOD UP

            //More button
            holder.more_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, v);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){

                                /*
                                case R.id.edit:
                                    editPost(post.getPostid());
                                    return true;

                                 */

                                case R.id.delete:
                                    FirebaseDatabase.getInstance().getReference("shop")
                                            .child(post.getBuyselldemo())
                                            .child(post.getType())
                                            .child(post.getPostid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(mContext, ""+mContext.getText(R.string.succes), Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(mContext, ""+mContext.getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    return true;

                                case R.id.report:

                                    FirebaseDatabase.getInstance().getReference().child("report").child(firebaseUser.getUid())
                                            .child(post.getPostid()).setValue(true);
                                    Toast.makeText(mContext, ""+mContext.getText(R.string.succes), Toast.LENGTH_SHORT).show();

                                    return true;

                                default:
                                    return false;

                            }
                        }
                    });

                    popupMenu.inflate(R.menu.post_menu);

                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(firebaseUser.getUid());

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);

                            if (user.getAdmin().equals("true")){

                            }else{
                                if (!post.getPublisher().equals(firebaseUser.getUid())){
                                    //popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                                }else{
                                    popupMenu.getMenu().findItem(R.id.report).setVisible(false);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    popupMenu.show();
                }
            });



        }

        @Override
        public int getItemCount() {
            return mPost.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public ImageView saveButton,more_button, ic_admin_symbol;
            public CircleImageView img_profile;
            public TextView usernameTextView, dateTextView, worldnameTextView, itemnameTextView, priceTextView,
                    youcanReach;
            public LinearLayout profileLinearLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                profileLinearLayout = itemView.findViewById(R.id.profileLinearLayout);
                more_button = itemView.findViewById(R.id.more_button);
                ic_admin_symbol = itemView.findViewById(R.id.ic_admin_symbol);

                img_profile = itemView.findViewById(R.id.img_profile);


                usernameTextView = itemView.findViewById(R.id.usernameTextView);
                worldnameTextView = itemView.findViewById(R.id.worldnameTextView);
                itemnameTextView = itemView.findViewById(R.id.itemnameTextView);
                priceTextView = itemView.findViewById(R.id.priceTextView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                youcanReach = itemView.findViewById(R.id.youcanReach);



                profileLinearLayout = itemView.findViewById(R.id.profileLinearLayout);

            }
        }

        private void publisherInfo(CircleImageView img_profile, TextView username, String userid, ImageView ic_admin_symbol, TextView youcanReach){

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Glide.with(mContext).load(user.getImageurl()).into(img_profile);
                    username.setText(user.getUsername());

                    if (user.getAdmin().equals("true")){
                        ic_admin_symbol.setVisibility(View.VISIBLE);
                    }

                    if (user.getInstagram().length() >= 1){
                        youcanReach.setVisibility(View.VISIBLE);
                    }else{}

                    if (user.getTelegram().length() >= 1) {
                        youcanReach.setVisibility(View.VISIBLE);
                    }else{}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // SAVE METHOD HERE
        /*
        private void isSaved(String postid, ImageView imageView){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saves")
                    .child(firebaseUser.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postid).exists()){
                        imageView.setImageResource(R.drawable.ic_save);
                        imageView.setTag("saved");
                    }else{
                        imageView.setImageResource(R.drawable.ic_save_back);
                        imageView.setTag("save");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

         */


}
