package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText vEmail;
    private EditText vPassword;
    private Button bLog;
    private Toolbar tToolbar;

    private String email,password;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tToolbar= findViewById(R.id.login_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vEmail=findViewById(R.id.login_email);
        vPassword=findViewById(R.id.login_pass);
        bLog=findViewById(R.id.signup_button);

        email=vEmail.getText().toString().trim();
        password=vPassword.getText().toString().trim();

        mAuth=FirebaseAuth.getInstance();

        bLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validDetails()){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

//                                Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();


                            }else{
                                Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this,"Enter valid details",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void signUp(View view) {
        Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validDetails() {

        email=vEmail.getText().toString().trim();
        password=vPassword.getText().toString().trim();

        boolean valid = true;
        if (TextUtils.isEmpty(email) || !email.contains(".") || !email.contains("@")) {
            vEmail.setError("Invalid email");
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            vPassword.setError("Invalid password");
            valid = false;
        }
        return valid;
    }

}