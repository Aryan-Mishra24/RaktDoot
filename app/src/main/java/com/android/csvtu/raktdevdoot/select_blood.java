package com.android.csvtu.raktdevdoot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class select_blood extends AppCompatActivity {


    Button signout;
    Button A_p,A_m,B_p,B_m,AB_p,AB_m,O_p,O_m;

    FirebaseAuth mAuth;


    @Override
    protected void onResume(){
        super.onResume();
        checkGpsEnabled();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_blood);
        A_p = findViewById(R.id.a_p_button);
        A_m = findViewById(R.id.a_m_button);
        B_p = findViewById(R.id.b_p_button);
        B_m = findViewById(R.id.b_m_button);
        AB_p = findViewById(R.id.ab_p_button);
        AB_m = findViewById(R.id.ab_m_button);
        O_p = findViewById(R.id.o_p_button);
        O_m = findViewById(R.id.o_m_button);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 80);
        }

        signout = findViewById(R.id.sign_out);

        checkGpsEnabled();


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(select_blood.this,"Signed out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), welcome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });

        A_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","A+");
                startActivity(intent);

            }
        });
        A_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","A-");
                startActivity(intent);
            }
        });
        B_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","B+");

                startActivity(intent);
            }
        });
        B_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","B-");
                startActivity(intent);
            }
        });
        AB_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","AB+");
                startActivity(intent);
            }
        });
        AB_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","AB-");
                startActivity(intent);
            }
        });
        O_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","O+");
                startActivity(intent);
            }
        });
        O_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), search_blood.class);
                intent.putExtra("grp","O-");
                startActivity(intent);
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
                    recreate();
                }
            });
            builder.show();
        }
    }

}