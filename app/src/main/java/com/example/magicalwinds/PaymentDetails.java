package com.example.magicalwinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magicalwinds.Common.Common;
import com.example.magicalwinds.Model.MyResponse;
import com.example.magicalwinds.Model.Notification;
import com.example.magicalwinds.Model.Sender;
import com.example.magicalwinds.Model.Token;
import com.example.magicalwinds.Remote.APIService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetails extends AppCompatActivity {

    private TextView txtId, txtAccount, txtStatus;
    private Button continue_shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);


        txtId = findViewById(R.id.txtId);
        txtAccount = findViewById(R.id.txtAccount);
        txtStatus = findViewById(R.id.txtStatus);
        continue_shopping= findViewById(R.id.continue_shopping);

        //Get Intent
        Intent i = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(i.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), i.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentDetails.this,HomeActivity.class);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });
    }


    private void showDetails(JSONObject response, String paymentAmount) {
        try {

            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAccount.setText(paymentAmount + " USD");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
