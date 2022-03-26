package com.warnstudio.growtopiamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import maes.tech.intentanim.CustomIntent;

public class RegisterActivity extends AppCompatActivity {

    private EditText email_edittext,password_edittext,password_verify_edittext;
    private MaterialButton registerButton;
    private TextView loginButton;

    FirebaseAuth auth;
    DatabaseReference reference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        specialFinds();
        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setEnabled(false);
                String str_email = email_edittext.getText().toString();
                String str_password = password_edittext.getText().toString();
                String str_expassword = password_verify_edittext.getText().toString();

                if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_expassword)){
                    Toast.makeText(getApplicationContext(), ""+getText(R.string.donotleave), Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                }else if(str_password.length() <= 5){
                    Toast.makeText(getApplicationContext(), ""+getText(R.string.passwordmustbe6), Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                }else if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
                    Toast.makeText(getApplicationContext(), ""+getText(R.string.invalidemail), Toast.LENGTH_SHORT).show();
                    registerButton.setEnabled(true);
                }else if(str_password.equals(str_expassword)){

                    auth.fetchSignInMethodsForEmail(str_email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                    boolean check = !task.getResult().getSignInMethods().isEmpty();
                                    if (!check){
                                        Toast.makeText(RegisterActivity.this, ""+getText(R.string.waitforsec), Toast.LENGTH_SHORT).show();
                                        registerButton.setEnabled(false);
                                        CreateAccountMethod(str_email,str_password);

                                    }else{
                                        Toast.makeText(RegisterActivity.this, ""+getText(R.string.emailadressisused), Toast.LENGTH_SHORT).show();
                                        registerButton.setEnabled(true);
                                    }

                                }
                            });


                }
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                CustomIntent.customType(RegisterActivity.this,"left-to-right");
                finish();
            }
        });


    }

    private void CreateAccountMethod(String str_email, String str_password) {


        auth.createUserWithEmailAndPassword(str_email,str_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            registerButton.setEnabled(true);
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            Intent intent = new Intent(RegisterActivity.this,RegisterTwoActivity.class);
                            startActivity(intent);
                            CustomIntent.customType(RegisterActivity.this,"left-to-right");
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, ""+getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
                            registerButton.setEnabled(true);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, ""+getText(R.string.afewproblems), Toast.LENGTH_SHORT).show();
                registerButton.setEnabled(true);
            }
        });


    }


    private void specialFinds() {

        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        password_verify_edittext = findViewById(R.id.password_verify_edittext);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this,PageChoosenActivity.class);
        startActivity(intent);
        CustomIntent.customType(RegisterActivity.this,"right-to-left");
        finish();
    }
}