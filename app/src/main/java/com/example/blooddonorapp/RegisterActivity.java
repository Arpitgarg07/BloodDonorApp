package com.example.blooddonorapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText regName, regAge, regPhone, regCity;
    Spinner regSpinnerBloodGroup;
    Button btnSubmit;
    FirebaseFirestore db;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Firestore instance created: " + db.toString());

        regName              = findViewById(R.id.regName);
        regAge               = findViewById(R.id.regAge);
        regPhone             = findViewById(R.id.regPhone);
        regCity              = findViewById(R.id.regCity);
        regSpinnerBloodGroup = findViewById(R.id.regSpinnerBloodGroup);
        btnSubmit            = findViewById(R.id.btnSubmit);

        // BACK BUTTON
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // BLOOD GROUP SPINNER
        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, bloodGroups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regSpinnerBloodGroup.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> registerDonor());
    }

    void registerDonor() {
        String name       = regName.getText().toString().trim();
        String age        = regAge.getText().toString().trim();
        String phone      = regPhone.getText().toString().trim();
        String city       = regCity.getText().toString().trim();
        String bloodGroup = regSpinnerBloodGroup.getSelectedItem().toString();

        if (name.isEmpty())  { regName.setError("Name is required");             return; }
        if (age.isEmpty())   { regAge.setError("Age is required");               return; }
        if (phone.isEmpty() || phone.length() < 10) {
            regPhone.setError("Enter valid 10-digit number"); return; }
        if (city.isEmpty())  { regCity.setError("City is required");             return; }

        Log.d(TAG, "Attempting to save donor: " + name);

        btnSubmit.setEnabled(false);
        btnSubmit.setText("Registering...");

        Map<String, Object> donor = new HashMap<>();
        donor.put("name",        name);
        donor.put("age",         age);
        donor.put("phone",       phone);
        donor.put("city",        city);
        donor.put("bloodGroup",  bloodGroup);
        donor.put("lastDonated", "Not yet");
        donor.put("timestamp",   System.currentTimeMillis());

        Log.d(TAG, "Data map created, sending to Firestore...");

        db.collection("donors")
                .add(donor)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "SUCCESS! Document ID: " + documentReference.getId());
                    Toast.makeText(this,
                            "✅ " + name + " registered successfully!",
                            Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "FAILED to save: " + e.getMessage(), e);
                    Toast.makeText(this,
                            "❌ Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("🩸  Register as Donor");
                });
    }
}