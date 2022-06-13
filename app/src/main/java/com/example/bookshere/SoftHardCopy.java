package com.example.bookshere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoftHardCopy extends AppCompatActivity {
    Button hard,soft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hard=findViewById(R.id.btn_hard);
        soft=findViewById(R.id.btn_soft);
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SoftHardCopy.this,UploadBooks.class);
                startActivity(intent);
            }
        });

        soft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SoftHardCopy.this,UploadSoftCopy.class);
                startActivity(intent);
            }
        });
    }

}