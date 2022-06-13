package com.example.bookshere;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayImageMCA extends AppCompatActivity {



    DatabaseReference databaseReference;
    RecyclerView recyclerView;

    TextView nobooks;
    RecyclerViewAdapter adapter ;
    ProgressDialog progressDialog;
    List<ImageUploadInfo> list = new ArrayList<>();
    List<String> list2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        setContentView(R.layout.activity_displa_image_mca);
        Intent intent = getIntent();
        String CourseName = intent.getStringExtra("courseName");


        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        nobooks=(TextView)findViewById(R.id.nobook) ;

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayImageMCA.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(DisplayImageMCA.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Getting books for you");

        progressDialog.setTitle("Hang On");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Showing progress dialog.
        progressDialog.show();



        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference(UploadBooks.Database_Path);

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                String up_user = sharedpreferences.getString("UserName",null);
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    System.out.println("UniqueKey"+postSnapshot.getKey());
//                    System.out.println("UniqueKey1"+postSnapshot.getValue());
                    if(imageUploadInfo.c_name .equals(CourseName) && !(imageUploadInfo.u_user .equals(up_user) )){
                        imageUploadInfo.keys = postSnapshot.getKey();
                        System.out.println("UniqueKey"+postSnapshot.getKey());
                        list.add(imageUploadInfo);



                    }



                }
                System.out.println("list2"+list2);
                if(list.isEmpty()) {
                    nobooks.setVisibility(View.VISIBLE);

                }

                adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.actionSearch);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<ImageUploadInfo> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (ImageUploadInfo item : list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getImageName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Book found", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

}