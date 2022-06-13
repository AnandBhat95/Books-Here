package com.example.bookshere;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserUploadPopUp extends AppCompatActivity {
    Button user_upload_book,user_upload_pdf ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_upload_pop_up);
        this.setFinishOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        user_upload_book=findViewById(R.id.btn_us_books);
        user_upload_pdf=findViewById(R.id.btn_us_pdfs);
        user_upload_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserUploadPopUp.this,UserUploadInfo.class);
                startActivity(intent);
            }
        });

        user_upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserUploadPopUp.this,PdsDelete.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(UserUploadPopUp.this,BuySellScreen.class);
        startActivity(intent);
    }
}