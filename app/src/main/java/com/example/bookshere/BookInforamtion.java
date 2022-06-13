package com.example.bookshere;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.squareup.picasso.Picasso;


public class BookInforamtion extends AppCompatActivity implements Serializable {


    Button delete_btn,call_btn,report;
    TextView name,author,c_year,p_year,price,u_by;
    ImageView img;
    public String boo_na="";
    public String c_n="";
    public String upload_by="";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        }
        setContentView(R.layout.activity_book_inforamtion);
        delete_btn = (Button)findViewById(R.id.Delete) ;
        call_btn = (Button)findViewById(R.id.btn_call) ;
        report=(Button) findViewById(R.id.report);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String up_user = sharedpreferences.getString("UserName",null);
        Intent intent = getIntent();
        String[] easyPuzzle = intent.getExtras().getStringArray("Bo_na");
        System.out.println("Getting"+easyPuzzle[1]);
        databaseReference = FirebaseDatabase.getInstance().getReference(UploadBooks.Database_Path);
        boo_na=easyPuzzle[0];
        c_n=easyPuzzle[2];
        upload_by=easyPuzzle[6];
        name=findViewById(R.id.name);
        author=findViewById(R.id.author);
        c_year=findViewById(R.id.c_year);
        p_year=findViewById(R.id.p_year);
        price=findViewById(R.id.price);
//        u_by=findViewById(R.id.u_by);
        name.setText(easyPuzzle[0]);
        author.setText(easyPuzzle[8]);
        c_year.setText(easyPuzzle[3]);
        p_year.setText(easyPuzzle[4]);
        price.setText(easyPuzzle[5]);
//        u_by.setText(easyPuzzle[6]);
//        infoTetxt = (TextView) findViewById(R.id.info_text);
//        infoTetxt.setText( "Name: "+easyPuzzle[0]+"\n"+"Author: "+easyPuzzle[8]+"\n"+"Course Name: "+easyPuzzle[2]+"\n"+"Course Year: "+easyPuzzle[3]+"\n"+"Published Year: "+easyPuzzle[4]+"\n"+"Price: "+easyPuzzle[5]);
        System.out.println("IDK"+easyPuzzle[7]);
        img=findViewById(R.id.imageView1);
        Picasso.with(this).load(easyPuzzle[1]).resize(400, 300) .placeholder(R.drawable.giphy)
                .into(img);

        if(easyPuzzle[6] .equals(up_user)){
            delete_btn.setVisibility(View.VISIBLE);
            call_btn.setVisibility(View.INVISIBLE);
            report.setVisibility(View.INVISIBLE);
        }

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        databaseReference.child(easyPuzzle[7]).removeValue();
                        System.out.println("Deleted Successfully");
                        Intent intent=new Intent(view.getContext(), BuySellScreen.class);
                        view.getContext().startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent=new Intent(view.getContext(), BookInforamtion.class);
                        view.getContext().startActivity(intent);
                    }
                   });

                builder.setMessage("You won't see this book again.");
                builder.setTitle("Confirm delete?");

                AlertDialog d = builder.create();
                d.show();

            }
        });
        class Constants{
            public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
        }
       call_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent callIntent = new Intent(Intent.ACTION_CALL);
                       String co=easyPuzzle[9];
                       callIntent.setData(Uri.parse("tel:"+co));
                       if (ActivityCompat.checkSelfPermission(BookInforamtion.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                           if (ActivityCompat.shouldShowRequestPermissionRationale(BookInforamtion.this,
                                   android.Manifest.permission.CALL_PHONE)) {
                           } else {
                               ActivityCompat.requestPermissions(BookInforamtion.this,
                                       new String[]{android.Manifest.permission.CALL_PHONE},
                                       Constants.MY_PERMISSIONS_REQUEST_CALL_PHONE);
                           }
                       }
                       startActivity(callIntent);
                   }
               });
               builder.setNegativeButton("No", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialog, int which)
                   {
                       Intent intent=new Intent(view.getContext(), BookInforamtion.class);
                       view.getContext().startActivity(intent);
                   }
               });

               builder.setMessage("You want to call this owner?");
               builder.setTitle("Confirm Call?");

               AlertDialog d = builder.create();
               d.show();
           }



       });

    }
    class Constants {
        public static final int sms = 100;
    }

    public void countReport(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String number = "6361115019";
                String msg = "Name: " + boo_na + "\n" + "Course: " + c_n + "\n" + "uploader: " + upload_by;
                try {
                    SmsManager smsManager = SmsManager.getDefault();

                    if (ActivityCompat.checkSelfPermission(BookInforamtion.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(BookInforamtion.this,
                                Manifest.permission.SEND_SMS)) {
                        } else {
                            ActivityCompat.requestPermissions(BookInforamtion.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    Constants.sms);
                        }
                    }
                    smsManager.sendTextMessage(number, null, msg, null, null);

                    Toast.makeText(getApplicationContext(), "Report Submitted", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Went wrong..Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent=new Intent(view.getContext(), BookInforamtion.class);
                view.getContext().startActivity(intent);
            }
        });

        builder.setMessage("You want to report this post");
        builder.setTitle("Confirm Report?");

        AlertDialog d = builder.create();
        d.show();
    }



}