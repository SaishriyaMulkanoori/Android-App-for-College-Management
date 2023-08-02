package com.example.smartcollege;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;


public class UpdateProfileActivity extends AppCompatActivity {

    EditText etName,etBranch,etYear,etSection,etEmail;
    Button button;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String currentuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        currentuid=user.getUid();
        documentReference=db.collection("user").document(currentuid);

        etName=findViewById(R.id.name_up);
        etBranch=findViewById(R.id.branch_up);
        etYear=findViewById(R.id.year_up);
        etSection=findViewById(R.id.sec_up);
        etEmail=findViewById(R.id.email_up);
        button=findViewById(R.id.btn_up);

        button.setOnClickListener(v -> updateProfile());
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        documentReference.get()
                .addOnCompleteListener(task -> {

                    if(task.getResult().exists()) {


                        String nameResult = task.getResult().getString("name");
                        String deptResult = task.getResult().getString("branch");
                        String rollResult = task.getResult().getString("section");
                        String yearResult = task.getResult().getString("year");
                        String emailResult = task.getResult().getString("email");
                        String url = task.getResult().getString("url");

                        etName.setText(nameResult);
                        etBranch.setText(deptResult);
                        etSection.setText(rollResult);
                        etYear.setText(yearResult);
                        etEmail.setText(emailResult);
                    }else{
                        Toast.makeText(UpdateProfileActivity.this, "No Profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProfile() {
        String name=etName.getText().toString();
        String branch=etBranch.getText().toString();
        String roll_no=etSection.getText().toString();
        String year=etYear.getText().toString();
        String email=etEmail.getText().toString();


        final DocumentReference sDoc=db.collection("user").document(currentuid);
        db.runTransaction((Transaction.Function<Void>) transaction -> {

            transaction.update(sDoc, "name",name );
            transaction.update(sDoc, "branch",branch );
            transaction.update(sDoc, "section",roll_no );
            transaction.update(sDoc, "year",year );
            transaction.update(sDoc,"email",email);


            return null;
        }).addOnSuccessListener(aVoid -> Toast.makeText(UpdateProfileActivity.this, "updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}