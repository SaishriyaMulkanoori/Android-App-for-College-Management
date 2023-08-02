package com.example.smartcollege;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProfileViewholder extends RecyclerView.ViewHolder {
    TextView textViewName,textViewProfession,viewUserprofile,sendmessagebtn;
    TextView namell,vp_ll,namefollower,vpfollower,professionFollower;
    DatabaseReference blockref;
    ImageView imageView,iv_ll,iv_follower;
    CardView cardView;
    LinearLayout llprofile;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ProfileViewholder(@NonNull View itemView) {
        super(itemView);
    }

    public void setProfile(FragmentActivity fragmentActivity, String name, String uid, String prof,
                           String url){


        cardView = itemView.findViewById(R.id.cardview_profile);
        textViewName = itemView.findViewById(R.id.tv_name_profile);
        textViewProfession = itemView.findViewById(R.id.tv_profession_profile);
        viewUserprofile = itemView.findViewById(R.id.viewUser_profile);
        imageView = itemView.findViewById(R.id.profile_imageview);
        llprofile = itemView.findViewById(R.id.ll_profile);


        Picasso.get().load(url).into(imageView);
        textViewProfession.setText(prof);
        textViewName.setText(name);






    }



    public void setFollower(Application application, String name, String url,
                            String profession, String bio, String privacy, String email, String followers, String website){

        iv_follower = itemView.findViewById(R.id.iv_follower);
        professionFollower = itemView.findViewById(R.id.profession_follower);
        namefollower = itemView.findViewById(R.id.name_follower);
        vpfollower = itemView.findViewById(R.id.vp_follower);

        Picasso.get().load(url).into(iv_follower);
        namefollower.setText(name);
        professionFollower.setText(profession);



    }
}
