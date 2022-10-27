package com.example.shelter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shelter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    EditText  etEmail, etPass;
    LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        lin = findViewById(R.id.bandar);
    }



    public void Login(View v)
    {
        String Email = etEmail.getText().toString();
        String Password = etPass.getText().toString();

        if(Email.length() <= 0 )
        {
            etEmail.setError("Something Is Wrong with your Email");
        }
        if(Password.length() <= 0)
        {
            etEmail.setError("Password Can't be Empty");
        }
        else
        {
            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        if(mAuth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(LoginActivity.this, "Welcome To Bark", Toast.LENGTH_LONG).show();
                            Intent intent1 = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent1);
                            finish();
                        }
                        else {
                            Snackbar.make(lin,"You Have to Verify your Email First lw sm7t",50).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void SignUpRedirect(View v)
    {
        Intent intent1 = new Intent(LoginActivity.this,MainActivity.class); //redirects you to the login page
        startActivity(intent1);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            if(mAuth.getCurrentUser().isEmailVerified()) {
                Toast.makeText(LoginActivity.this, "Welcome To Bark", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(LoginActivity.this, HomePage.class);
                startActivity(intent1);
                finish();
            }
        }
    }
}