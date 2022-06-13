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

public class PdsDelete extends AppCompatActivity {
    ListView listView_user;
    TextView textView_user;
    public static String Database_Path1 = "All_Pdf_Uploads_Database/";
    //database reference to get uploads data
    DatabaseReference uDatabaseReference;
    SearchView searchView_user;
    String Key;
    //list to store uploads data
    List<fileinfomodel> uploadList_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        setContentView(R.layout.activity_pds_delete);
        uploadList_user = new ArrayList<>();
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String up_user = sharedpreferences.getString("UserName",null);
        textView_user=findViewById(R.id.down_user);
        textView_user.setAnimation(AnimationUtils.loadAnimation(PdsDelete.this, R.anim.flash_leave_now));
        searchView_user = findViewById(R.id.searchView_user);
        listView_user= (ListView) findViewById(R.id.listView_user);

        uDatabaseReference = FirebaseDatabase.getInstance().getReference(Database_Path1);
        uDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String user=postSnapshot.child("u_user").getValue().toString();
                    System.out.println("users"+user);
                    System.out.println("users"+up_user);
                    if(user.equals(up_user)){
                        fileinfomodel upload = postSnapshot.getValue(fileinfomodel.class);
                        uploadList_user.add(upload);
                    }

                }

                String[] uploads = new String[uploadList_user.size()];
                List<String> a = new ArrayList<String>();
                for (int i = 0; i < uploads.length; i++) {
                  uploads[i] = uploadList_user.get(i).getPdfName()+".pdf";
                }
                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_change, uploads);
                listView_user.setAdapter(adapter);
                if (uploadList_user.size()==0){
                    textView_user.setText("NO PDFs UPLOADED");
                }
                listView_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //getting the upload
                        //Opening the upload file in browser using the upload url
                        fileinfomodel upload = uploadList_user.get(i);
                        if( uploadList_user.get(i).getU_user().equals(up_user)){
                            Key=upload.getPdfId();
                            System.out.println("User key "+Key);
                        }

                        AlertDialog.Builder adb=new AlertDialog.Builder(PdsDelete.this);
                        adb.setTitle("Delete?");
                        adb.setMessage("Are you sure you want to delete "+uploadList_user.get(i).getPdfName()+".pdf");
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               uDatabaseReference.child(Key).removeValue();

                                Toast.makeText(PdsDelete.this,"deleted ",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(PdsDelete.this,BuySellScreen.class);
                                startActivity(intent);
                            }});
                        adb.show();
                    }
                });

                searchView_user.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(uploadList_user.contains(query)){
                            adapter.getFilter().filter(query);
                        }else{
                            Toast.makeText(PdsDelete.this, "No Match found",Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PdsDelete.this,BuySellScreen.class);
        startActivity(intent);
    }
}