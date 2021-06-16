package com.myapplications.mishlavim.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.myapplications.mishlavim.R;

public class SplashScreenActivity extends AppCompatActivity {
    //variables:
    private static int SPLASH_SCREEN = 2000;
    Animation topAnimation, bottomRightAnimation,bottomLeftAnimation;
//    ImageView image;
    TextView leftText, rightText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Animations:
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomRightAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_right_animation);
        bottomLeftAnimation= AnimationUtils.loadAnimation(this, R.anim.bottom_left_animation);
        //find Views by id
//        image = findViewById(R.id.logo);
        leftText = findViewById(R.id.splashText2);
        rightText = findViewById(R.id.splashText1);

        //set animation
//        image.setAnimation(topAnimation);
        leftText.setAnimation(bottomLeftAnimation);
        rightText.setAnimation(bottomRightAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fragment_fade_in,R.anim.fragment_fade_out);
                finish();
            }

        }, SPLASH_SCREEN);
    }






}