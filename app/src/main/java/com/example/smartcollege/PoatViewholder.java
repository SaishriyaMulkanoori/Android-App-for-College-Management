package com.example.smartcollege;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PoatViewholder extends RecyclerView.ViewHolder {

    ImageView imageViewprofile,iv_post;
    TextView tv_name,tv_desc,tv_likes,tv_comment,tv_time,tv_nameprofile;
    ImageButton likebtn,menuoptions,commentbtn;
    DatabaseReference likeref;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    int likescount;


    public PoatViewholder(@NonNull View itemView) {
        super(itemView);
    }
    public void SetPost(FragmentActivity activity,String name,String url,String postUri,String time,String uid,String type,String desc) {
        imageViewprofile = itemView.findViewById(R.id.ivprofile_item);
        iv_post = itemView.findViewById(R.id.iv_post_item);
       // tv_comment = itemView.findViewById(R.id.commentbutton_post);
        tv_desc = itemView.findViewById(R.id.tv_desc_post);
        commentbtn = itemView.findViewById(R.id.commentbutton_post);
        likebtn = itemView.findViewById(R.id.likebutton_post);
        tv_likes = itemView.findViewById(R.id.tv_likes_post);
        menuoptions = itemView.findViewById(R.id.morebutton_post);
        tv_time = itemView.findViewById(R.id.tv_time_post);
        tv_nameprofile = itemView.findViewById(R.id.tv_name_post);


        SimpleExoPlayer exoPlayer;
        PlayerView playerView = itemView.findViewById(R.id.exoplayer_item_post);

        if (type.equals("iv")) {

            Picasso.get().load(postUri).into(iv_post);
            tv_desc.setText(desc);
            tv_time.setText(time);
            tv_nameprofile.setText(name);
            //Toast.makeText(activity, "name="+name, Toast.LENGTH_SHORT).show();
            //Toast.makeText(activity, "url="+url, Toast.LENGTH_SHORT).show();

            Picasso.get().load(url).into(imageViewprofile);
           playerView.setVisibility(View.INVISIBLE);
        } else if (type.equals("vv")) {
            iv_post.setVisibility(View.INVISIBLE);
            tv_desc.setText(desc);
            tv_time.setText(time);
            tv_nameprofile.setText(name);
            //Toast.makeText(activity, "name="+name, Toast.LENGTH_SHORT).show();
            Toast.makeText(activity, "url="+url, Toast.LENGTH_SHORT).show();
            Picasso.get().load(url).into(imageViewprofile);
            Toast.makeText(activity, "video loaded", Toast.LENGTH_SHORT).show();

            try {
                //BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                //TrackSelection.Factory videoTrackSelectionFactory=new AdaptiveTrackSelection.Factory(bandwidthMeter);
                //TrackSelector trackSelector = new DefaultTrackSelector();
                //exoPlayer=(ExoPlayer) ExoPlayerFactory.newSimpleInstance(activity);
                //TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                //DefaultTrackSelector trackSelector =new DefaultTrackSelector(videoTrackSelectionFactory);


                ExoPlayer player = new ExoPlayer.Builder(activity).build();
                playerView.setPlayer(player);
                // Uri video=Uri.parse(postUri);
                MediaItem postItem = MediaItem.fromUri(postUri);
                player.addMediaItem(postItem);
                player.prepare();
                player.setPlayWhenReady(false);

            } catch (Exception e) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void likeChecker(final String postkey){
       likebtn=itemView.findViewById(R.id.likebutton_post);
       likeref= database.getReference("post likes");
       FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
       final String uid=user.getUid();
       likeref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.child(postkey).hasChild(uid)){
                  likebtn.setImageResource(R.drawable.ic_favorite);
                  addNotification(postkey,uid);

               }else{
                   likebtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
               }
               likescount=(int)snapshot.child(postkey).getChildrenCount();
               tv_likes.setText(Integer.toString(likescount)+"likes");
           }



           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
    private  void addNotification(String postId,String publisherId){
        HashMap<String, Object> map=new HashMap<>();
        map.put("userid",publisherId);
        map.put("text","liked your post");
        map.put("postid",postId);
        map.put("isPost",true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }

}
