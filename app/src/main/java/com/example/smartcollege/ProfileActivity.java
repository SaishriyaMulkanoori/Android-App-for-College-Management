package com.example.smartcollege;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    EditText e_name,e_branch,e_section,em_id,e_year;
    Button button;
    ImageView imageview;
    ProgressBar progressBar;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    private static final int image=1;
    All_UserMember member;
    String currentUserId;
    Postmember postmember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        postmember=new Postmember();
        member=new All_UserMember();
        imageview =findViewById(R.id.crt_pro);
        e_name=findViewById(R.id.name_p);
        e_branch=findViewById(R.id.branch_p);
        e_section=findViewById(R.id.sec_p);
        em_id=findViewById(R.id.email_p);
        e_year=findViewById(R.id.year_p);
        button=findViewById(R.id.btn_p);
        progressBar=findViewById(R.id.progressbar_p);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        currentUserId=user.getUid();
        documentReference=db.collection("user").document(currentUserId);
        storageReference= FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference=database.getReference("All users");
        button.setOnClickListener(view -> uploadData());
        imageview.setOnClickListener(v -> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction((Intent.ACTION_GET_CONTENT));
            startActivityForResult(intent,PICK_IMAGE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == PICK_IMAGE || resultCode==RESULT_OK || data !=null || data.getData()!=null){
                imageUri=data.getData();



                Picasso.get().load(imageUri).into(imageview);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Error"+e,Toast.LENGTH_SHORT).show();

        }

    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));

    }

    private void uploadData() {
     String name=e_name.getText().toString();

     String branch=e_branch.getText().toString();
     String section=e_section.getText().toString();
     String email=em_id.getText().toString();
     String year=e_year.getText().toString();

     if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(branch) || !TextUtils.isEmpty(section) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(year) || imageUri !=null ){
         progressBar.setVisibility(View.VISIBLE);
       //  Toast.makeText(this,"after visible",Toast.LENGTH_SHORT).show();
         final StorageReference reference=storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));
       //  Toast.makeText(this,"after storage reference",Toast.LENGTH_SHORT).show();
         uploadTask=reference.putFile(imageUri);
       //  Toast.makeText(this,"after upload task",Toast.LENGTH_SHORT).show();
         Task<Uri> urlTask=uploadTask.continueWithTask(task -> {
            if(!task.isSuccessful())
            {

                throw task.getException();
            }
            return reference.getDownloadUrl();
         }).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {

               Uri downloadUri=task.getResult();
                Map<String,String> profile=new HashMap<>();
                profile.put("name",name);
                profile.put("branch",branch);
                profile.put("section",section);
                profile.put("email",email);
                profile.put("year",year);
                profile.put("url",downloadUri.toString());
                profile.put("uid",currentUserId);
                profile.put("privacy","Public");

                member.setName(name);
                member.setBranch(branch);
                member.setSection(section);
                member.setYear(year);
                member.setUid(currentUserId);
                member.setEmail(email);
                member.setUrl(downloadUri.toString());

                databaseReference.child(currentUserId).setValue(member);
                documentReference.set(profile)
                        .addOnSuccessListener(aVoid -> {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ProfileActivity.this,"Profile Created",Toast.LENGTH_SHORT).show();
                            Handler handler=new Handler();
                            handler.postDelayed(() -> {
                                Intent intent=new Intent(ProfileActivity.this,ProfileFragment.class);
                               startActivity(intent);
                              //  replaceFragment(new ProfileFragment());
                              //  FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                              //  fragmentTransaction.replace(R.id.mainContainer,new ProfileFragment()).commit();
                            },2000);

                        });

            }
         });
     }
     else
     {
         Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();


     }
    }

    }

