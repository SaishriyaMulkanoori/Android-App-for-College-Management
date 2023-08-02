package com.example.smartcollege;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AskActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference UserQuestions,AllQuestions;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    QuestionMember member;
    String name,url,privacy,uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
       String currentUserid=user.getUid();

        editText=findViewById(R.id.ask_et_question);
        button=findViewById(R.id.btn_submit);
       documentReference=db.collection("user").document(currentUserid);

        AllQuestions=database.getReference("All Questions");
        UserQuestions=database.getReference("User Questions").child(currentUserid);

        member=new QuestionMember();
       button.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {
               String question=editText.getText().toString();

               Calendar cdate= Calendar.getInstance();
               SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
               final String savedate=currentdate.format(cdate.getTime());

               Calendar ctime=Calendar.getInstance();
               SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss");
               final String savetime=currenttime.format(ctime.getTime());

               String time=savedate +":"+ savetime;

               if(question!=null){
                   member.setQuestion(question);
                   member.setName(name);
                   member.setPrivacy(privacy);
                   member.setUrl(url);
                   member.setUserid(uid);
                   member.setTime(time);

                   String id=UserQuestions.push().getKey();
                   UserQuestions.child(id).setValue(member);

                   String child=AllQuestions.push().getKey();
                   member.setKey(id);
                   AllQuestions.child(child).setValue(member);
                   Toast.makeText(AskActivity.this, "Submitted", Toast.LENGTH_SHORT).show();

               }else{
                   Toast.makeText(AskActivity.this, "Please Ask Question", Toast.LENGTH_SHORT).show();

               }

           }
       });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(task -> {

                    if(task.getResult().exists()) {


                         name = task.getResult().getString("name");
                         url = task.getResult().getString("url");
                         uid=task.getResult().getString("uid");
                         privacy=task.getResult().getString("privacy");


                    }else{
                        Toast.makeText(AskActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}