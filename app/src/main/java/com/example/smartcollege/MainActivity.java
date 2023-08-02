package com.example.smartcollege;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mail,password;

    public void onClickSignup(View view)
    {
        String email=mail.getText().toString();
        String pass_wrd=password.getText().toString();

        Pattern pattern;
        Matcher matcher;
        String PATTERN="^([a-z\\d\\.-]+)@([a-z]+)\\.([a-z]{2})(\\.[a-z]{2})$";
        pattern=Pattern.compile(PATTERN);
        matcher=pattern.matcher(email);
        if (!email.isEmpty() && matcher.matches() && !pass_wrd.isEmpty()) {
            //Toast.makeText(this, "Email Verified !", Toast.LENGTH_SHORT).show();
            signUp(email,pass_wrd);
        } else
        {
            Toast.makeText(this, "Enter mail id or password correctly !", Toast.LENGTH_SHORT).show();
        }

    }
    public void onClickLogin(View view)
    {
        String email=mail.getText().toString();
        String pass_wrd=password.getText().toString();
        if (email.isEmpty() || pass_wrd.isEmpty()) {
            Toast.makeText(this, "Please Enter the credentials properly!", Toast.LENGTH_SHORT).show();
        }
        else {
            login(email, pass_wrd);
        }

    }

    private void signUp(String email,String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "SignUp Success",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"user is "+user.getEmail());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void login(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Login Success",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                            overridePendingTransition(0,0);

                            Log.d(TAG,"user is "+user.getEmail());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        mAuth=FirebaseAuth.getInstance();
        mail=findViewById(R.id.email_id);
        password=findViewById(R.id.pass1);
    }
}