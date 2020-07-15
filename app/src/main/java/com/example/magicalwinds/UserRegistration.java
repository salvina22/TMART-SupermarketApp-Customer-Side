package com.example.magicalwinds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegistration extends AppCompatActivity {

    private CircleImageView photo_upload;
    private EditText cust_name, cust_phone, cust_email, cust_addr;
    private RadioButton radioButton;
    private TextView close, update, change;
    private Button submit;
    private RadioGroup gen_rg;
    private ProgressDialog loadingBar;
    private DatabaseReference userRef;

    private Uri imageUri;
    private String myurl="";
    private StorageReference profpic;
    private String checker = "";
    private StorageTask uploadTask;

    private String name, phone, email, address, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //String username=getIntent().getExtras().get("Name").toString();
        photo_upload = (CircleImageView) findViewById(R.id.photo_upload);
        cust_name = (EditText) findViewById(R.id.customer_name);
        cust_phone = (EditText) findViewById(R.id.customer_phone);
        cust_email = (EditText) findViewById(R.id.customer_email);
        cust_addr = (EditText) findViewById(R.id.customer_addr);
       // gen_rg = (RadioGroup) findViewById(R.id.gender_radiogrp);


        close = (TextView) findViewById(R.id.close);
        update = (TextView) findViewById(R.id.update);
        change = (TextView) findViewById(R.id.change);

        loadingBar = new ProgressDialog(this);


        submit = (Button) findViewById(R.id.submit_btn);

        //cust_name.setText(username);

        display();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* int selectedId = gen_rg.getCheckedRadioButtonId();

                radioButton = (RadioButton) findViewById(selectedId);*/


                createAccount();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked"))
                {
                    UserInfoSaved();
                }
                else
                {
                    UpdateOnlyInfo();
                }
            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(UserRegistration.this);

            }
        });

    }

    private void display() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference uref=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        name=dataSnapshot.child("name").getValue().toString();
                        phone=dataSnapshot.child("phone").getValue().toString();
                        email=dataSnapshot.child("email").getValue().toString();
                        address=dataSnapshot.child("address").getValue().toString();
                     //   String gender=dataSnapshot.child("gender").getValue().toString();

                        Picasso.get().load(image).into(photo_upload);
                        cust_name.setText(name);
                        cust_email.setText(email);
                        cust_phone.setText(phone);
                        cust_addr.setText(address);
                        radioButton.setText(gender);
                    }

                }
                else{
                    createAccount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UpdateOnlyInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userdata=new HashMap<>();
        userdata.put("name",cust_name.getText().toString());
        userdata.put("phone",cust_phone.getText().toString());
        userdata.put("email",cust_email.getText().toString());
        userdata.put("address",cust_addr.getText().toString());
       // userdata.put("gender",radioButton.getText().toString());

        ref.child(user.getUid()).updateChildren(userdata);

        Toast.makeText(UserRegistration.this, "Details Updated Successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UserRegistration.this,HomeActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            photo_upload.setImageURI(imageUri);

        }
        else
        {
            Toast.makeText(this, "Error: Try again !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserRegistration.this,UserRegistration.class));
            finish();
        }
    }

    private void UserInfoSaved() {

        if(TextUtils.isEmpty(cust_name.getText().toString()))
        {
            cust_name.setError("This field is required");
            cust_name.requestFocus();
        }
        else if(TextUtils.isEmpty(cust_phone.getText().toString()))
        {
            cust_phone.setError("This field is required");
            cust_phone.requestFocus();
        }
        else if(TextUtils.isEmpty(cust_email.getText().toString()))
        {
            cust_email.setError("This field is required");
            cust_email.requestFocus();

        }
        else if(TextUtils.isEmpty(cust_addr.getText().toString()))
        {
            cust_addr.setError("This field is required");
            cust_addr.requestFocus();
        }
        else if(TextUtils.isEmpty(radioButton.getText().toString()))
        {
            radioButton.setError("This field is required");
            radioButton.requestFocus();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();

        }



    }

    private void uploadImage() {
        loadingBar.setTitle("Updating");
        loadingBar.setMessage("Please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if(imageUri !=null)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            final StorageReference file =profpic.child(user.getUid()+ ".jpg");
            uploadTask=file.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return file.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful())
                    {
                        Uri downloaduri=task.getResult();
                        myurl=downloaduri.toString();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userdata=new HashMap<>();
                        userdata.put("name",cust_name.getText().toString());
                        userdata.put("phone",cust_phone.getText().toString());
                        userdata.put("email",cust_email.getText().toString());
                        userdata.put("address",cust_addr.getText().toString());
                       // userdata.put("gender",radioButton.getText().toString());
                        userdata.put("image",downloaduri);

                        ref.child(user.getUid()).updateChildren(userdata);

                        loadingBar.dismiss();
                        Toast.makeText(UserRegistration.this, "Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserRegistration.this,HomeActivity.class));
                        finish();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(UserRegistration.this, "Error:Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void createAccount() {
        name=cust_name.getText().toString();
        phone=cust_phone.getText().toString();
        email=cust_email.getText().toString();
        address = cust_addr.getText().toString();
        //gender=radioButton.getText().toString();


        if(TextUtils.isEmpty(name))
        {
            cust_name.setError("This field is required");
            cust_name.requestFocus();
        }
        else if(TextUtils.isEmpty(phone))
        {
            cust_phone.setError("This field is required");
            cust_phone.requestFocus();
        }
        else if(TextUtils.isEmpty(email))
        {
            cust_email.setError("This field is required");
            cust_email.requestFocus();

        }
        else if(TextUtils.isEmpty(address))
        {
            cust_addr.setError("This field is required");
            cust_addr.requestFocus();
        }
        else
        {
            loadingBar.setTitle("Registering");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            ValidatephoneNumber();
        }



    }

    private void ValidatephoneNumber() {
        HashMap<String,Object> userdata=new HashMap<>();
        userdata.put("name",name);
        userdata.put("phone",phone);
        userdata.put("email",email);
        userdata.put("address",address);
        //userdata.put("gender",gender);
        userdata.put("image",myurl);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userRef.child(user.getUid()).updateChildren(userdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(UserRegistration.this, "User Registered Successfully !", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent i = new Intent(UserRegistration.this,HomeActivity.class);
                            startActivity(i);
                            finish();




                        }
                        else
                        {
                            Toast.makeText(UserRegistration.this, "Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void UserDisplayInfo(final EditText cust_name, final EditText cust_email, final EditText cust_addr, final EditText cust_phone, final String gender) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference uref=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        name=dataSnapshot.child("name").getValue().toString();
                        phone=dataSnapshot.child("phone").getValue().toString();
                        email=dataSnapshot.child("email").getValue().toString();
                        address=dataSnapshot.child("address").getValue().toString();
                      //  String gender=dataSnapshot.child("gender").getValue().toString();

                        Picasso.get().load(image).into(photo_upload);
                        cust_name.setText(name);
                        cust_email.setText(email);
                        cust_phone.setText(phone);
                        cust_addr.setText(address);
                        radioButton.setText(gender);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
