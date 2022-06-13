package com.example.bookshere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BuyHardSoftCopy extends AppCompatActivity {
    Button hard1, soft1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_hard_soft_copy);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hard1=findViewById(R.id.btn_hard1);
        soft1=findViewById(R.id.btn_soft1);
        hard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BuyHardSoftCopy.this,CourseList.class);
                startActivity(intent);
            }
        });

        soft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BuyHardSoftCopy.this,PdfView.class);
                startActivity(intent);
            }
        });
    }
}