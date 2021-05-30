package com.example.mishlavim.volunteerActivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mishlavim.R;

public class fillOutFormActivity extends AppCompatActivity {
    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;
    public int progress = 1;
    public  int[] intArray = new int[]{ 1,2,3,4,5,6,7,8,9,10 };
    public int max = intArray.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_out_form);

        init();

        Button btNext = (Button)findViewById(R.id.next_btn);
        Button btBack = (Button)findViewById(R.id.back_btn);
        Button btSave = (Button)findViewById(R.id.save_btn);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = progressBarAnimation.getProgress();
                Toast.makeText(getBaseContext(), "btNEXT detected", Toast.LENGTH_SHORT).show();
                progressBarAnimation.setProgress(progress+1);
                progress ++;

            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = progressBarAnimation.getProgress();
                Toast.makeText(getBaseContext(), "btBack detected", Toast.LENGTH_SHORT).show();
                progressBarAnimation.setProgress(progress-1);
                progress --;

            }
        });

//        progressAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                Toast.makeText(getBaseContext(), "סיימת את הטופס בהצלחה!", Toast.LENGTH_LONG).show();
//                progressBarAnimation.setVisibility(View.GONE);
//            }
//        });

    }

    private void init() {
        progressBarAnimation = findViewById(R.id.questionProgressBar);
        progressAnimator = ObjectAnimator.ofInt(progressBarAnimation, "progress", progress, max);
        setMax(intArray);
        progressBarAnimation.setMax(max);
        progressBarAnimation.setProgress(progress);
    }

    private void setMax(int[] questionArray){
        max = intArray.length;
    }
    private int getMax(int max){
        return max;
    }

}