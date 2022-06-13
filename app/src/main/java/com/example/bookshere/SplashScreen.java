package com.example.bookshere;

import
        androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int timer=3000;

    ImageView image1;
    TextView powerByline;

    Animation sideAnim, bottomAnim;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        image1=findViewById(R.id.imageView);
        powerByline=findViewById(R.id.textView);

        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        image1.setAnimation(sideAnim);
        powerByline.setAnimation(bottomAnim);





        new Handler().postDelayed(() -> {

            sharedPreferences=getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
            boolean isFirstTime=sharedPreferences.getBoolean("firstTime",true);


            if(isFirstTime){
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("firstTime",false);
                editor.commit();
                Intent intent=new Intent(getApplicationContext(), OnBoarding.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
            else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        },timer);
    }
}