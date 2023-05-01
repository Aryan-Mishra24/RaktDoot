package com.android.csvtu.raktdevdoot;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class signin extends AppCompatActivity  {

    private static final int RC_SIGN_IN = 40;
    private static final String TAG = "GOOGLEAUTH";
    GoogleSignInClient mGoogleSignInClient;

    private FusedLocationProviderClient mFusedLocationClient;
    Double latitude = Double.valueOf(0);
    Double longitude = Double.valueOf(0);
    private FirebaseAuth mauth;
    Dialog dialog;
    int q = 0;
    String gpsmethod = "gps";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
  //  FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

    @Override
    protected void onStart() {
        super.onStart();
        //checkGpsEnabled();

    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate(); // recreate the activity
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        mauth =FirebaseAuth.getInstance();


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 80);
        }


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

        if(latitude == 0 || longitude ==0){
            locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 5000, 0, locationListener);
        }

        //Configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        //Getting button click
        Button signinbtn = findViewById(R.id.gsignin);
       if(q == 0 ) {
           q = 1;
           signinbtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   signIN();
               }
           });
       }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 80) {
            // Check if permission is granted
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 80);
            }
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get last known location
                Task<Location> lastLocationTask = mFusedLocationClient.getLastLocation();
                lastLocationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Last known location retrieved successfully
                        if (location != null) {
                             latitude = location.getLatitude();
                             longitude = location.getLongitude();
                            // Do something with the location data
                        }
                    }
                });
            }
        }
    }


    private boolean showOneTapUI = true;
    private void signIN() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount>task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);

        mauth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mauth.getCurrentUser();
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(db.collection("Users").whereEqualTo("email",user.getEmail())
                                    .get().isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(), register0.class);
                                intent.putExtra("fullname",user.getDisplayName());
                                intent.putExtra("email",user.getEmail());
                                startActivity(intent);
                                finish();
                            }
                            else if (!isNew ){
                                Toast.makeText(signin.this,"Signed in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), select_blood.class);
                                startActivity(intent);
                                finish();
                            }
                            else if(isNew){
                                Intent intent = new Intent(getApplicationContext(), register0.class);
                                intent.putExtra("fullname",user.getDisplayName());
                                intent.putExtra("email",user.getEmail());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
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

        public void onProviderDisabled(String provider) {
            checkGpsEnabled();
        }
    };

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