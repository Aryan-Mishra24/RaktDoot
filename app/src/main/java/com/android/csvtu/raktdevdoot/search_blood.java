package com.android.csvtu.raktdevdoot;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class search_blood extends AppCompatActivity {
    public static final double Radius = 6372.8; // Earth radius in kilometers
    Double lat1 = Double.valueOf(0);
    Double lon1 = Double.valueOf(0);
    int radius = 5;
    RecyclerView recyclerView;
    ArrayList<user> userArrayList;
    myadapter Myadapter;
    TextView rang_txt,notfound;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Button signout, km_10,km_20,km_30,km_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_blood);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        signout = findViewById(R.id.sign_out);
        mAuth = FirebaseAuth.getInstance();
        rang_txt = findViewById(R.id.rangeview);
        recyclerView = findViewById(R.id.peoples);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        km_10 = findViewById(R.id.km_10_btn);
        km_20 = findViewById(R.id.km_20_btn);
        km_30 = findViewById(R.id.km_30_btn);
        notfound = findViewById(R.id.not_found);

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<user>();
        Myadapter = new myadapter(search_blood.this, userArrayList);
        recyclerView.setAdapter(Myadapter);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 80);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 80);
        }

        checkGpsEnabled();
        // Request updates from the GPS provider
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        km_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myadapter.clearData();
                EventChangeListener(10);
            }
        });
        km_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myadapter.clearData();
                EventChangeListener(20);
            }
        });
        km_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myadapter.clearData();
                EventChangeListener(30);
            }
        });



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(search_blood.this, "Signed out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), welcome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });

            EventChangeListener(5);

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
                    Toast.makeText(search_blood.this,"Cannot fetch data",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),select_blood.class);
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
              lat1 = location.getLatitude();
              lon1 = location.getLongitude();
            // Use latitude and longitude values
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 80 ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // Start location updates or do something else that requires location permission
            } else {
                // Permission denied
                // Show a message explaining why location permission is necessary and ask the user to grant it
            }
        }
    }


    private void EventChangeListener(int limit ) {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 80);
        }

        // String userBloodGroup = "AB+"; // example blood group
        String group1 = getIntent().getStringExtra("grp");
        rang_txt.setText(String.valueOf(limit));

        // Get current GPS location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        lat1 = location.getLatitude();
        lon1 = location.getLongitude();
        if(lat1 == 0 || lon1 == 0) {
            while (lat1 != 0 && lon1 != 0) {
                locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, locationListener);
            }
        }
        db.collection("Users")
                .whereEqualTo("bloodgroup",group1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && location != null){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                double lat = document.getDouble("latitude");
                                double lon = document.getDouble("longitude");
                                Log.d("latitude", String.valueOf(location.getLatitude()));
                                Log.d("longitude", String.valueOf(location.getLongitude()));
                                Log.d("range", String.valueOf(limit));
                                double distance = distance(location.getLatitude(),location.getLongitude(),lat,lon);
                                if(distance <= limit){
                                    Log.d("Distance",String.valueOf(distance));
                                    userArrayList.add(document.toObject(user.class));

                                    Myadapter.notifyDataSetChanged();
                                }
                            }
                            if(userArrayList.isEmpty()){
                                notfound.setVisibility(View.VISIBLE);
                            }
                            else{
                                notfound.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            checkGpsEnabled();
                        }
                    }
                });

    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }



}