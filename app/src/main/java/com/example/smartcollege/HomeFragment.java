package com.example.smartcollege;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{



    Button button;
    RecyclerView recyclerView;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference,likeref;
    Boolean likeChecker=false;
    DatabaseReference db1,db2,db3;

    LinearLayoutManager linearLayoutManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        button=getActivity().findViewById(R.id.createpost_f4);
        reference=database.getReference("All posts");
        likeref= database.getReference("post likes");
        recyclerView=getActivity().findViewById(R.id.rv_posts);
        recyclerView.setHasFixedSize(true);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String currentuid=user.getUid();

        db1=database.getReference("All images").child(currentuid);
        db2=database.getReference("All videos").child(currentuid);
        db3=database.getReference("All posts");

        linearLayoutManager=new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.createpost_f4:
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Postmember> options=new FirebaseRecyclerOptions.Builder<Postmember>()
                .setQuery(reference,Postmember.class)
                .build();
        FirebaseRecyclerAdapter<Postmember,PoatViewholder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Postmember, PoatViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PoatViewholder holder, int position, @NonNull Postmember model) {
                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserId =user.getUid();

                        final String postkey=getRef(position).getKey();
                        holder.SetPost(getActivity(),model.getName(),model.getUrl(),model.getPostUri(),model.getTime(),model.getUid(),model.getType(),model.getDesc());

                        final String name=getItem(position).getName();
                        final String url=getItem(position).getPostUri();
                        final String time=getItem(position).getTime();
                        final String type=getItem(position).getType();
                        final String userid=getItem(position).getUid();

                        holder.likeChecker(postkey);
                        holder.menuoptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialog(name,url,time,userid,type);
                            }
                        });

                        holder.likebtn.setOnClickListener((new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                likeChecker=true;
                                likeref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(likeChecker.equals(true)){
                                            if(snapshot.child(postkey).hasChild(currentUserId)){
                                                likeref.child(postkey).child(currentUserId).removeValue();
                                                likeChecker=false;
                                            }else{
                                                likeref.child(postkey).child(currentUserId).setValue(true);
                                                likeChecker=false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }));

                    }

                    @NonNull
                    @Override
                    public PoatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.post_layout,parent,false);
                        return new PoatViewholder(view);
                    }
                };



        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    void showDialog(String name,String url,String time,String userid,String type){
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View view=inflater.inflate(R.layout.post_options,null);
        TextView download=view.findViewById(R.id.download_tv_post);
        TextView share=view.findViewById(R.id.share_tv_post);
        TextView copyurl=view.findViewById(R.id.copyurl_tv_post);
        TextView delete=view.findViewById(R.id.delete_tv_post);

        AlertDialog alertDialog=new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        alertDialog.show();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
         String currentUserId =user.getUid();

         if(userid.equals(currentUserId)){
             delete.setVisibility(View.VISIBLE);
         }else{
             delete.setVisibility(View.INVISIBLE);
         }

         delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Query query=db1.orderByChild("time").equalTo(time);
                 query.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                             dataSnapshot1.getRef().removeValue();

                             Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });

                 Query query2=db2.orderByChild("time").equalTo(time);
                 query2.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                             dataSnapshot1.getRef().removeValue();

                             Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });

                 Query query3=db3.orderByChild("time").equalTo(time);
                 query3.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                             dataSnapshot1.getRef().removeValue();

                             Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
                 StorageReference reference= FirebaseStorage.getInstance().getReferenceFromUrl(url);
                 reference.delete()
                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {
                                         Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                     }
                                 });

                 alertDialog.dismiss();
             }
         });

         download.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {



                 if(type.equals("iv")){
                     DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
                     request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                             DownloadManager.Request.NETWORK_MOBILE);
                     request.setTitle("Download");
                     request.setDescription("Downloading image....");
                     request.allowScanningByMediaScanner();
                     request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                     request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name+System.currentTimeMillis() + ".jpg");
                     DownloadManager manager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                     manager.enqueue(request);

                     Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
                     alertDialog.dismiss();
                 }else{
                     DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
                     request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                             DownloadManager.Request.NETWORK_MOBILE);
                     request.setTitle("Download");
                     request.setDescription("Downloading video....");
                     request.allowScanningByMediaScanner();
                     request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                     request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name+System.currentTimeMillis() + ".mp4");
                     DownloadManager manager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                     manager.enqueue(request);

                     Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
                     alertDialog.dismiss();

                 }
             }
         });

         share.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String sharetext=name+"\n" +"\n"+url;
                 Intent intent=new Intent(Intent.ACTION_SEND);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 intent.putExtra(Intent.EXTRA_TEXT,sharetext);
                 intent.setType("text/plain");
                 startActivity(intent.createChooser(intent,"share via"));

                 alertDialog.dismiss();
             }
         });

         copyurl.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ClipboardManager cp=(ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                 ClipData clip=ClipData.newPlainText("String",url);
                 cp.setPrimaryClip(clip);
                 clip.getDescription();
                 Toast.makeText(getActivity(), "copied", Toast.LENGTH_SHORT).show();

                 alertDialog.dismiss();
             }
         });

    }
}