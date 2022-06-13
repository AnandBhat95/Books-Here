package com.example.bookshere;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;

import java.util.ArrayList;
import java.util.List;

public class PdfView extends AppCompatActivity {
    ListView listView;
    TextView textView;
    ArrayAdapter<String> adapter1;
    public static String Database_Path1 = "All_Pdf_Uploads_Database/";
    //database reference to get uploads data
    DatabaseReference mDatabaseReference;
    SearchView searchView;
    //list to store uploads data
    List<fileinfomodel> uploadList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        setContentView(R.layout.activity_pdf_view);
        uploadList = new ArrayList<>();
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String up_user = sharedpreferences.getString("UserName",null);
        textView=findViewById(R.id.down);
//        TranslateAnimation animation = new TranslateAnimation(0.0f, 1500.0f, 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
//
//        animation.setDuration(7500); // animation duration, change accordingly
//        animation.setRepeatCount(5); // animation repeat count
//        animation.setFillAfter(false);
//        textView.startAnimation(animation);
        textView.setAnimation(AnimationUtils.loadAnimation(PdfView.this, R.anim.flash_leave_now));
        searchView = findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                fileinfomodel upload = uploadList.get(i);
                List<String> a = new ArrayList<String>();
                Intent intent=new Intent(PdfView.this,PdfdownloadDelete.class);
                intent.putExtra("url",upload.getPdfURL());
                intent.putExtra("user_key",upload.getPdfId());
                intent.putExtra("upload_user",upload.getU_user());
                intent.putExtra("pdf_name",upload.getPdfName());
                startActivity(intent);
//                AlertDialog.Builder adb=new AlertDialog.Builder(PdfView.this);
//                adb.setTitle("Download?");
//                adb.setMessage("Are you sure you want to download ?");
//                adb.setNegativeButton("Cancel", null);
//                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(upload.getPdfURL()));
//                        startActivity(intent);
//                        Toast.makeText(PdfView.this,"Downloaded To storage",Toast.LENGTH_LONG).show();
//
//                    }});
//                adb.show();
                //Opening the upload file in browser using the upload url

            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Database_Path1);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String user=postSnapshot.child("u_user").getValue().toString();
                    System.out.println("users"+user);
                    System.out.println("users"+up_user);
                    if(! user.equals(up_user)){
                        fileinfomodel upload = postSnapshot.getValue(fileinfomodel.class);
                        uploadList.add(upload);
                    }
                }

                String[] uploads = new String[uploadList.size()];
               List<String> a = new ArrayList<String>();
                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getPdfName()+".pdf";
                }
                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_change, uploads);
                listView.setAdapter(adapter);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(uploadList.contains(query)){
                            adapter.getFilter().filter(query);
                        }else{
                            Toast.makeText(PdfView.this, "No Match found",Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);
                        return false;
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}