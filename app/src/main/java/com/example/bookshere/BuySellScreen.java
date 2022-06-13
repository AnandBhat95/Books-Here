package com.example.bookshere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class BuySellScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Button buy,sell;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.teal_700));
        setContentView(R.layout.buy_sell_screen);
        buy= findViewById(R.id.buy);
        sell=findViewById(R.id.sell);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

       //  bottomNavigationView.setSelectedItemId(R.id.home);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BuySellScreen.this,BuyHardSoftCopy.class);
                startActivity(intent);
            }
        });
        sell.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent=new Intent(BuySellScreen.this,SoftHardCopy.class);
                startActivity(intent);


            }

        });
        if (! isOnline()) {

            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishAffinity();
                }
            });
            snackbar.setAnchorView(bottomNavigationView);
            snackbar.show();
        }

    }



    First_Fragment firstFragment = new First_Fragment();
    FragmentSecond secondFragment = new FragmentSecond();
    FragmentThird thirdFragment = new FragmentThird();



    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,firstFragment ).commit();
                return true;

            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                return true;

            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                return true;

        }

        return true;
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}