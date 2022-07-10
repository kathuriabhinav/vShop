package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText vName;
    private EditText vEmail;
    private EditText vPassword;
    private Button vRegButton;
    private Toolbar tToolbar;

    private String name,email,password;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tToolbar= findViewById(R.id.reg_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vName=findViewById(R.id.regis_name);
        vEmail=findViewById(R.id.regis_email);
        vPassword=findViewById(R.id.regis_password);
        vRegButton=findViewById(R.id.signup_button);

        name=vName.getText().toString().trim();
        email=vEmail.getText().toString().trim();
        password=vPassword.getText().toString().trim();

        mAuth= FirebaseAuth.getInstance();

        vRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validDetails()){
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this,task.getException().getMessage()+"...try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this,"Fill valid details",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void signIn(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validDetails() {

        name=vName.getText().toString().trim();
        email=vEmail.getText().toString().trim();
        password=vPassword.getText().toString().trim();

        boolean valid = true;
        if (TextUtils.isEmpty(email) || !email.contains(".") || !email.contains("@")) {
            vEmail.setError("Invalid email");
            valid = false;
        }
        if (TextUtils.isEmpty(password) ) {
            vPassword.setError("Invalid password");
            valid = false;
        }
        if(TextUtils.isEmpty(name)){
            vName.setError("Invalid Name");
            valid = false;
        }
        return valid;
    }
}