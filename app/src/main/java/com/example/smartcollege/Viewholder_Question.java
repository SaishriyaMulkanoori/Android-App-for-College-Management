package com.example.smartcollege;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Viewholder_Question extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView time_result, name_result, question_result; //deletebtn, replybtn, replybtn1;
    ImageButton fvrt_btn;
    DatabaseReference favouriteref;// blockref;
    CardView cv_question;
    LinearLayout ll_question;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public Viewholder_Question(@NonNull View itemView) {
        super(itemView);
    }

    public void SetItem(FragmentActivity activity, String name, String url, String userid, String key, String question, String privacy, String time) {
        imageView = itemView.findViewById(R.id.iv_question_item);
        time_result = itemView.findViewById(R.id.time_que_item);
        name_result = itemView.findViewById(R.id.name_que_item_tv);
        question_result = itemView.findViewById(R.id.que_item_tv);
       // replybtn = itemView.findViewById(R.id.reply_item_que);
       // cv_question = itemView.findViewById(R.id.cv_question);
       // ll_question = itemView.findViewById(R.id.ll_questions);

        Picasso.get().load(url).into(imageView);
        time_result.setText(time);
        name_result.setText(name);
        question_result.setText(question);

        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();

        blockref = database.getReference("Block users").child(currentuid);

        blockref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(userid)) {
                    cv_question.setVisibility(View.GONE);
                    ll_question.setVisibility(View.GONE);
                } else {
                    Picasso.get().load(url).into(imageView);
                    name_result.setText(name);
                    question_result.setText(question);
                    time_result.setText(time);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    public void favouriteChecker(final String postkey) {
        fvrt_btn = itemView.findViewById(R.id.fvrt_search_item);


        favouriteref = database.getReference("favourites");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        favouriteref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postkey).hasChild(uid)) {
                    fvrt_btn.setImageResource(R.drawable.ic_baseline_turned_in_24);
                } else {
                    fvrt_btn.setImageResource(R.drawable.ic_baseline_turned_in_not_24);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
