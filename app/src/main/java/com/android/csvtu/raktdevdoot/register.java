package com.android.csvtu.raktdevdoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    Spinner gender,bgp1;
    String[] gndr = {"Male","Female","Others"};
    String[] bldgrp = {"A+","A-","B+","B-","AB+","AB-","O+","O-",};
    Button reg;
    String genderval,bgp1val;
    Double latitude = Double.valueOf(0);
    Double longitude = Double.valueOf(0);
    int q = 0;
    FirebaseFirestore db;
    FirebaseUser User;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gender = findViewById(R.id.gndr);
        bgp1 = findViewById(R.id.bloodgrp);
        reg = findViewById(R.id.registerbtn);
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<String> adapterbld = new ArrayAdapter<String>(register.this, android.R.layout.simple_spinner_item, bldgrp);
        adapterbld.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bgp1.setAdapter(adapterbld);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(register.this, android.R.layout.simple_spinner_item, gndr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        bgp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bgp1val = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bgp1val = bldgrp[0];
            }
        });

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderval = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genderval = gndr[0];
            }
        });


        // permissions for location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 80);
        }
       /* if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 80);
        }*/

        checkGpsEnabled();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

       if( latitude == 0 || longitude ==0) {
           locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 5000, 0, locationListener);
       }

        reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (q != -1) {

                    String fullname, em, ct, dist, addr;
                    long ph, age1;

                    if(genderval == null){
                        Toast.makeText(register.this,"Select Gender",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(bgp1val == null){
                        Toast.makeText(register.this,"Select Bloodgroup",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fullname = getIntent().getStringExtra("fname");
                    em = getIntent().getStringExtra("gmail");
                    ph = Long.valueOf(getIntent().getStringExtra("ph"));
                    ct = getIntent().getStringExtra("ct");
                    dist = getIntent().getStringExtra("dist");
                    addr = getIntent().getStringExtra("addr");
                    age1 = Long.valueOf(getIntent().getStringExtra("age"));

                    Map<String, Object> user1 = new HashMap<>();
                    user1.put("fullname", fullname);
                    user1.put("email", em);
                    user1.put("phone", ph);
                    user1.put("age", age1);
                    user1.put("city", ct);
                    user1.put("gender", genderval);
                    user1.put("address", addr);
                    user1.put("district", dist);
                    user1.put("bloodgroup", bgp1val);
                    user1.put("latitude", latitude);
                    user1.put("longitude", longitude);




                    if(latitude != 0 || longitude != 0){
                    db.collection("Users")
                            .add(user1)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(register.this,"Account created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), select_blood.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(register.this,"Failed connection to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                        );
                    }else{
                        Toast.makeText(register.this,"Failed getting gps", Toast.LENGTH_SHORT).show();
                    }
                    }
                }
        });
    }
    private void checkGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is not enabled, prompt the user to turn it on
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS is disabled");
            builder.setMessage("Please enable GPS to use this app");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Open the location settings page
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(register.this,"Cannot create account",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),signin.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.show();
        }
    }
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider or GPS.
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.d("latitude", String.valueOf(latitude));
            Log.d("longitude", String.valueOf(longitude));

            // Use latitude and longitude values
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };
}