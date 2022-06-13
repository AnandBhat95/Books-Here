package com.example.bookshere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class PdfdownloadDelete extends AppCompatActivity {
    String pdf_url,user_key,upload_user,pdf_name;
    Button dwnld;
    TextView inff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfdownload_delete);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pdf_url=getIntent().getStringExtra("url");
        user_key=getIntent().getStringExtra("user_key");
        upload_user=getIntent().getStringExtra("upload_user");
        pdf_name=getIntent().getStringExtra("pdf_name");
        dwnld=findViewById(R.id.btn_download);
        inff=findViewById(R.id.inff);
        inff.setText("Download \n"+pdf_name+".pdf ?");
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String up_user = sharedpreferences.getString("UserName",null);
        if (up_user.equals(upload_user)){
            dwnld.setVisibility(View.INVISIBLE);
        }
        dwnld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(pdf_url));
                        startActivity(intent);
                        Toast.makeText(PdfdownloadDelete.this,"Downloaded To storage",Toast.LENGTH_LONG).show();
            }
        });

    }
}