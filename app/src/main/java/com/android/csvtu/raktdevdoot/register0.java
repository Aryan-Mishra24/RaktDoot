package com.android.csvtu.raktdevdoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class register0 extends AppCompatActivity {

    EditText fname,phone,age,email1,city1,district1,address;
    Button nxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register0);

        fname = findViewById(R.id.fullname);
        email1 = findViewById(R.id.email2);
        phone = findViewById(R.id.phoneNumber);
        age = findViewById(R.id.age2);
        address = findViewById(R.id.address2);
        district1 = findViewById(R.id.district2);
        city1 = findViewById(R.id.city2);
        nxt = findViewById(R.id.next);
        fname.setText(getIntent().getStringExtra("fullname"));
        email1.setText(getIntent().getStringExtra("email"));


        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register.class);
                intent.putExtra("fname",String.valueOf(fname.getText()));
                intent.putExtra("gmail",String.valueOf(email1.getText()));
                intent.putExtra("ph",String.valueOf(phone.getText()));
                intent.putExtra("age",String.valueOf(age.getText()));
                intent.putExtra("addr",String.valueOf(address.getText()));
                intent.putExtra("dist",String.valueOf(district1.getText()));
                intent.putExtra("ct",String.valueOf(city1.getText()));
                startActivity(intent);
            }
        });
    }
}