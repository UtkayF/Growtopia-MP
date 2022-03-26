package com.warnstudio.growtopiamarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import maes.tech.intentanim.CustomIntent;

public class ForgotActivity extends AppCompatActivity {

    private EditText email_edittext;
    private MaterialButton rememberButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        email_edittext = findViewById(R.id.email_edittext);
        rememberButton = findViewById(R.id.rememberButton);

        rememberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tellMeResetPasswordLink();
            }
        });

    }

    private void tellMeResetPasswordLink() {
        String str_email = email_edittext.getText().toString();

        if (TextUtils.isEmpty(str_email)){
            Toast.makeText(this, ""+getText(R.string.bosbirakilamaz2), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            Toast.makeText(this, ""+getText(R.string.invalidemail), Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(str_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotActivity.this, ""+getText(R.string.gosendmailadress), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotActivity.this,PageChoosenActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(ForgotActivity.this,"right-to-left");
                    finish();
                }else{
                    Toast.makeText(ForgotActivity.this, ""+getText(R.string.afewproblems), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotActivity.this,PageChoosenActivity.class);
                    startActivity(intent);
                    CustomIntent.customType(ForgotActivity.this,"right-to-left");
                    finish();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgotActivity.this,LoginActivity.class);
        startActivity(intent);
        CustomIntent.customType(ForgotActivity.this,"right-to-left");
        finish();
    }
}