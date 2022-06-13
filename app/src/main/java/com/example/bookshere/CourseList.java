package com.example.bookshere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CourseList extends AppCompatActivity {
    Button mca,mba,ee,civil,mech,cs,is,bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        }
        setContentView(R.layout.activity_course_list);

        mca = (Button)findViewById(R.id.mca);
        mca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);
                intent.putExtra("courseName","MCA");
                startActivity(intent);

            }
        });
        mba = (Button)findViewById(R.id.mba);
        mba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);
                intent.putExtra("courseName","MBA");
                startActivity(intent);

            }
        });
        ee = (Button)findViewById(R.id.eee);
        ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);
                intent.putExtra("courseName","EEE");
                startActivity(intent);

            }
        });
        civil = (Button)findViewById(R.id.civil);
        civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);
                intent.putExtra("courseName","Civil");
                startActivity(intent);

            }
        });
        mech = (Button)findViewById(R.id.mech);
        mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);
                intent.putExtra("courseName","Mechanical");
                startActivity(intent);

            }
        });
        cs = (Button)findViewById(R.id.cs);
        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);
                intent.putExtra("courseName","CS");
                startActivity(intent);

            }
        });
        is = (Button)findViewById(R.id.is);
        is.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);
                intent.putExtra("courseName","IS");
                startActivity(intent);

            }
        });
        bt = (Button)findViewById(R.id.biote);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CourseList.this, DisplayImageMCA.class);

                intent.putExtra("courseName","Biotech");
                startActivity(intent);

            }
        });
    }
}