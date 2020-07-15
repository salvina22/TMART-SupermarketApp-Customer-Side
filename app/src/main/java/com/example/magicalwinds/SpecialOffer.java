package com.example.magicalwinds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SpecialOffer extends AppCompatActivity {

    ViewFlipper viewFlipper,v1;
    private TextView hr,min,sec;
    private int duration;
    private long dura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_offer);
        viewFlipper = findViewById(R.id.promo_view_flipper);
        v1=findViewById(R.id.coming);

        hr= findViewById(R.id.hr);
        min=findViewById(R.id.min);
        sec=findViewById(R.id.sec);

        int images[] ={R.drawable.sale1,R.drawable.wild,R.drawable.sale2};
        int img[]={R.drawable.vf1,R.drawable.vf2,R.drawable.vf3};

        for (int image: images)
        {
            flipperimages(image);
        }
        for (int ima: img)
        {
            flipperimages1(ima);
        }

        startTimer();

    }

    private void startTimer() {
        //duration=81200000; //6 hours

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countdate="14-07-2020 00:00:00";
        Date now = new Date();

        try {
            Date date = sdf.parse(countdate);
            long currentTime=now.getTime();
            long newDate= date.getTime();
             dura=newDate-currentTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        new CountDownTimer(dura, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String hours=String.format("%02d",elapsedHours);
                String minutes=String.format("%02d",elapsedMinutes);
                String seconds=String.format("%02d",elapsedSeconds);

                String yy = String.format("%02d:%02d", elapsedHours, elapsedMinutes);
                hr.setText(hours);
                min.setText(minutes);
                sec.setText(seconds);
            }

            public void onFinish() {
                hr.setText("00");
                min.setText("00");
                sec.setText("00");


            }
        }.start();
    }


    private void flipperimages1(int ima) {
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(ima);

        v1.addView(imageView);
        v1.setFlipInterval(6000);
        v1.setAutoStart(true);

        v1.setInAnimation(this,android.R.anim.slide_out_right);
        v1.setOutAnimation(this,android.R.anim.slide_in_left);
    }

    private void flipperimages(int image) {
        ImageView imageView=new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }
}
