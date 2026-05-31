package com.example.blooddonorapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DonorDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_detail);

        String name       = getIntent().getStringExtra("name");
        String bloodGroup = getIntent().getStringExtra("bloodGroup");
        String city       = getIntent().getStringExtra("city");
        String phone      = getIntent().getStringExtra("phone");
        String age        = getIntent().getStringExtra("age");

        // last donated is no longer displayed — removed from UI

        ((TextView) findViewById(R.id.detailName)).setText(name);
        ((TextView) findViewById(R.id.detailBloodGroup)).setText(bloodGroup);
        ((TextView) findViewById(R.id.detailCity)).setText(city);
        ((TextView) findViewById(R.id.detailPhone)).setText(phone);
        ((TextView) findViewById(R.id.detailAge)).setText(age + " years");

        // BACK BUTTON
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // CALL BUTTON
        Button btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        });

        // WHATSAPP BUTTON
        Button btnWhatsapp = findViewById(R.id.btnWhatsapp);
        btnWhatsapp.setOnClickListener(v -> {
            String message = "Hi " + name + ", I found your profile on Blood Donor App. I need "
                    + bloodGroup + " blood. Can you please help?";
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
            whatsappIntent.setData(Uri.parse(
                    "https://wa.me/91" + phone + "?text=" + Uri.encode(message)));
            startActivity(whatsappIntent);
        });
    }
}