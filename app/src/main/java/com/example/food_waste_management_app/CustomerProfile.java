package com.example.food_waste_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfile extends AppCompatActivity {
    // creating variables for
    // EditText and buttons.
    private Button mLogout, mBack;
    private EditText customerNameEdt, customerEmailEdt, customerAddressEdt;
    private Button confirm;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    CustomerInfo customerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        mLogout = (Button) findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CustomerProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mBack = (Button) findViewById(R.id.back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CustomerProfile.this, NGOMapActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        // initializing our edittext and button
        customerNameEdt = findViewById(R.id.name);
        customerEmailEdt = findViewById(R.id.email);
        customerAddressEdt = findViewById(R.id.address);

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("CustomerInfo");

        // initializing our object
        // class variable.
        customerInfo = new CustomerInfo();

        confirm = findViewById(R.id.confirm);

        // adding on click listener for our button.
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String name = customerNameEdt.getText().toString();
                String email = customerEmailEdt.getText().toString();
                String address = customerAddressEdt.getText().toString();

                // below line is for checking weather the
                // edittext fields are empty or not.
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(address)) {
                    // if the text fields are empty
                    // then show the below message.
                    Toast.makeText(CustomerProfile.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    // else call the method to add
                    // data to our database.
                    addDatatoFirebase(name, email, address);
                }
            }
        });
    }

    private void addDatatoFirebase(String name, String email, String address) {
        // below 3 lines of code is used to set
        // data in our object class.
        customerInfo.setCustomerName(name);
        customerInfo.setCustomerEmail(email);
        customerInfo.setCustomerAddress(address);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(customerInfo);

                // after adding this data we are showing toast message.
                Toast.makeText(CustomerProfile.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(CustomerProfile.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}