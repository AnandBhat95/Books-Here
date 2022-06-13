package com.example.bookshere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText emailEdit;
    private Button reset;
    private ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        }
        setContentView(R.layout.activity_forgot_password);

        emailEdit=(EditText) findViewById(R.id.reset_email);
        reset=(Button) findViewById(R.id.btn_reset);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        auth=FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();

            }
        });
    }
    private void   resetPassword(){
        String fo_email = emailEdit.getText().toString().trim();
        if(fo_email.isEmpty())
        {
            emailEdit.setError("Email is empty");
            emailEdit.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(fo_email).matches())
        {
            emailEdit.setError("Enter the valid email address");
            emailEdit.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(fo_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check Your mail to reset", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(ForgotPassword.this,"Not a registered or valid email..Try Again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}