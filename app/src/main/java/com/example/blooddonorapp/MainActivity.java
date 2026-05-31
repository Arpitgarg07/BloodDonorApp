package com.example.blooddonorapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout donorListContainer, bloodGroupChips, cityChipsRow;
    GridLayout cityGrid;
    TextView tvDonorSectionTitle, tvCityGridTitle, statTotalDonors, statTotalCities, statAvailable;
    EditText searchBar;
    FirebaseFirestore db;

    List<Donor> allDonors = new ArrayList<>();
    List<Donor> filteredDonors = new ArrayList<>();
    Map<String, Integer> cityCountMap = new LinkedHashMap<>();

    String selectedBloodGroup = "All";
    String selectedCity = "All";
    boolean sortByPopular = true;

    final String[] bloodGroups = {"All", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        donorListContainer  = findViewById(R.id.donorListContainer);
        bloodGroupChips     = findViewById(R.id.bloodGroupChips);
        cityChipsRow        = findViewById(R.id.cityChipsRow);
//        cityGrid            = findViewById(R.id.cityGrid);
        tvDonorSectionTitle = findViewById(R.id.tvDonorSectionTitle);
//        tvCityGridTitle     = findViewById(R.id.tvCityGridTitle);
        statTotalDonors     = findViewById(R.id.statTotalDonors);
        statTotalCities     = findViewById(R.id.statTotalCities);
        statAvailable       = findViewById(R.id.statAvailable);
        searchBar           = findViewById(R.id.searchBar);

        buildBloodGroupChips();

        searchBar.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { applyFilters(); }
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.btnPopularFilter).setOnClickListener(v -> {
            sortByPopular = !sortByPopular;
            ((TextView) findViewById(R.id.tvPopularFilter)).setText(
                    sortByPopular ? "Popular first" : "A-Z order");
            buildCityChips();
//            buildCityGrid();
        });

        ExtendedFloatingActionButton fab = findViewById(R.id.fabRegister);
        fab.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFirestore();
    }

    void loadFromFirestore() {
        db.collection("donors").get()
                .addOnSuccessListener(snapshots -> {
                    allDonors.clear();
                    cityCountMap.clear();

                    for (QueryDocumentSnapshot doc : snapshots) {
                        String name        = doc.getString("name");
                        String bloodGroup  = doc.getString("bloodGroup");
                        String city        = doc.getString("city");
                        String phone       = doc.getString("phone");
                        String age         = doc.getString("age");
                        String lastDonated = doc.getString("lastDonated");
                        if (name == null) continue;

                        allDonors.add(new Donor(name, bloodGroup, city, phone, age, lastDonated));

                        String cityKey = city != null ? city : "Unknown";
                        cityCountMap.put(cityKey, cityCountMap.getOrDefault(cityKey, 0) + 1);
                    }

                    updateStats();
                    buildCityChips();
//                    buildCityGrid();
                    applyFilters();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    void updateStats() {
        statTotalDonors.setText(String.valueOf(allDonors.size()));
        statTotalCities.setText(String.valueOf(cityCountMap.size()));

        int available = 0;
        for (Donor d : allDonors) {
            if (d.getLastDonated() == null || d.getLastDonated().equals("Not yet")) available++;
        }
        statAvailable.setText(String.valueOf(available));
    }

    void buildBloodGroupChips() {
        bloodGroupChips.removeAllViews();
        for (String bg : bloodGroups) {
            TextView chip = new TextView(this);
            chip.setText(bg);
            chip.setTextSize(12);
            chip.setTypeface(null, Typeface.BOLD);
            chip.setPadding(28, 0, 28, 0);
            chip.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, 80);
            lp.setMarginEnd(10);
            chip.setLayoutParams(lp);

            boolean sel = bg.equals(selectedBloodGroup);
            chip.setBackgroundResource(sel ? R.drawable.chip_blood_selected : R.drawable.chip_blood_unselected);
            chip.setTextColor(sel ? 0xFFFFFFFF : 0xFFC62828);

            chip.setOnClickListener(v -> {
                selectedBloodGroup = bg;
                buildBloodGroupChips();
                applyFilters();
            });
            bloodGroupChips.addView(chip);
        }
    }

    // Builds the horizontal city chips row (All + each city name)
    void buildCityChips() {
        cityChipsRow.removeAllViews();

        // Build sorted city list
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(cityCountMap.entrySet());
        if (sortByPopular) {
            entries.sort((a, b) -> b.getValue() - a.getValue());
        } else {
            entries.sort((a, b) -> a.getKey().compareTo(b.getKey()));
        }

        // "All" chip first
        addCityChip("All", -1);

        // Then all city chips in order
        for (Map.Entry<String, Integer> entry : entries) {
            addCityChip(entry.getKey(), entry.getValue());
        }
    }

    void addCityChip(String cityName, int count) {
        TextView chip = new TextView(this);
        String label = cityName.equals("All") ? "All Cities" : cityName + " (" + count + ")";
        chip.setText(label);
        chip.setTextSize(12);
        chip.setTypeface(null, Typeface.BOLD);
        chip.setPadding(24, 0, 24, 0);
        chip.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 72);
        lp.setMarginEnd(8);
        chip.setLayoutParams(lp);

        boolean sel = cityName.equals(selectedCity);
        chip.setBackgroundResource(sel ? R.drawable.chip_blood_selected : R.drawable.chip_blood_unselected);
        chip.setTextColor(sel ? 0xFFFFFFFF : 0xFFC62828);

        chip.setOnClickListener(v -> {
            selectedCity = cityName;
            tvDonorSectionTitle.setText(
                    cityName.equals("All") ? "All donors" : "Donors in " + cityName);
//            tvCityGridTitle.setText(
//                    cityName.equals("All") ? "All cities" : cityName);
            buildCityChips();
//            buildCityGrid();
            applyFilters();
        });

        cityChipsRow.addView(chip);
    }

//    void buildCityGrid() {
//        cityGrid.removeAllViews();
//        cityGrid.setColumnCount(3);
//
//        List<Map.Entry<String, Integer>> entries = new ArrayList<>(cityCountMap.entrySet());
//        if (sortByPopular) {
//            entries.sort((a, b) -> b.getValue() - a.getValue());
//        } else {
//            entries.sort((a, b) -> a.getKey().compareTo(b.getKey()));
//        }
//
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        int padding = (int) (20 * getResources().getDisplayMetrics().density);
//        int gap = (int) (6 * getResources().getDisplayMetrics().density);
//        int tileWidth = (screenWidth - padding * 2 - gap * 2) / 3;
//
//        for (Map.Entry<String, Integer> entry : entries) {
//            String city  = entry.getKey();
//            int count    = entry.getValue();
//            boolean isActive = city.equals(selectedCity);
//
//            LinearLayout tile = new LinearLayout(this);
//            tile.setOrientation(LinearLayout.VERTICAL);
//            tile.setGravity(Gravity.CENTER);
//            int tilePad = (int)(9 * getResources().getDisplayMetrics().density);
//            tile.setPadding(tilePad, tilePad, tilePad, (int)(7 * getResources().getDisplayMetrics().density));
//            tile.setBackgroundResource(isActive ? R.drawable.city_tile_active : R.drawable.city_tile_normal);
//
//            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
//            lp.width = tileWidth;
//            lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
//            lp.setMargins(0, 0, gap, gap);
//            tile.setLayoutParams(lp);
//
//            // Pin icon
//            LinearLayout iconCircle = new LinearLayout(this);
//            iconCircle.setGravity(Gravity.CENTER);
//            int sz = (int)(30 * getResources().getDisplayMetrics().density);
//            LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(sz, sz);
//            iconCircle.setLayoutParams(iconLp);
//            iconCircle.setBackgroundResource(R.drawable.circle_red_light);
//
//            TextView iconTv = new TextView(this);
//            iconTv.setText("📍");
//            iconTv.setTextSize(13);
//            iconTv.setGravity(Gravity.CENTER);
//            iconCircle.addView(iconTv);
//            tile.addView(iconCircle);
//
//            // City name
//            TextView tvName = new TextView(this);
//            tvName.setText(city);
//            tvName.setTextSize(11);
//            tvName.setTypeface(null, Typeface.BOLD);
//            tvName.setTextColor(isActive ? 0xFFC62828 : 0xFF212121);
//            tvName.setGravity(Gravity.CENTER);
//            LinearLayout.LayoutParams nameLp = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            nameLp.topMargin = (int)(3 * getResources().getDisplayMetrics().density);
//            tvName.setLayoutParams(nameLp);
//            tile.addView(tvName);
//
//            // Donor count
//            TextView tvCount = new TextView(this);
//            tvCount.setText(count + " donors");
//            tvCount.setTextSize(10);
//            tvCount.setTextColor(0xFF9E9E9E);
//            tvCount.setGravity(Gravity.CENTER);
//            tile.addView(tvCount);
//
//            tile.setOnClickListener(v -> {
//                selectedCity = city.equals(selectedCity) ? "All" : city;
//                tvDonorSectionTitle.setText(
//                        selectedCity.equals("All") ? "All donors" : "Donors in " + selectedCity);
//                tvCityGridTitle.setText(
//                        selectedCity.equals("All") ? "All cities" : selectedCity);
//                buildCityChips();
//                buildCityGrid();
//                applyFilters();
//            });
//
//            cityGrid.addView(tile);
//        }
//    }

    void applyFilters() {
        String query = searchBar.getText().toString().toLowerCase().trim();
        filteredDonors.clear();

        for (Donor d : allDonors) {
            boolean matchSearch = d.getName().toLowerCase().contains(query)
                    || d.getCity().toLowerCase().contains(query)
                    || d.getBloodGroup().toLowerCase().contains(query);
            boolean matchBlood  = selectedBloodGroup.equals("All") || d.getBloodGroup().equals(selectedBloodGroup);
            boolean matchCity   = selectedCity.equals("All") || d.getCity().equals(selectedCity);

            if (matchSearch && matchBlood && matchCity) filteredDonors.add(d);
        }

        renderDonorList();
    }

    void renderDonorList() {
        donorListContainer.removeAllViews();

        for (Donor donor : filteredDonors) {
            View card = LayoutInflater.from(this).inflate(R.layout.item_donor, donorListContainer, false);

            TextView tvBG    = card.findViewById(R.id.tvBloodGroup);
            TextView tvName  = card.findViewById(R.id.tvName);
            TextView tvSub   = card.findViewById(R.id.tvCityAge);
            TextView tvAvail = card.findViewById(R.id.tvAvailability);
            View dot         = card.findViewById(R.id.availDot);
            ImageButton btnCall = card.findViewById(R.id.btnCall);
            ImageButton btnWA   = card.findViewById(R.id.btnWhatsapp);

            tvBG.setText(donor.getBloodGroup());
            tvName.setText(donor.getName());
            tvSub.setText("Age " + donor.getAge() + " · " + donor.getCity());

            boolean available = donor.getLastDonated() == null
                    || donor.getLastDonated().equals("Not yet");
            if (available) {
                tvAvail.setText("Available to donate");
                tvAvail.setTextColor(0xFF2E7D32);
                dot.setBackgroundResource(R.drawable.dot_green);
            } else {
                tvAvail.setText("Recently donated");
                tvAvail.setTextColor(0xFF9E9E9E);
                dot.setBackgroundResource(R.drawable.dot_gray);
            }

            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, DonorDetailActivity.class);
                intent.putExtra("name",       donor.getName());
                intent.putExtra("bloodGroup", donor.getBloodGroup());
                intent.putExtra("city",       donor.getCity());
                intent.putExtra("phone",      donor.getPhone());
                intent.putExtra("age",        donor.getAge());
                startActivity(intent);
            });

            btnCall.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(android.net.Uri.parse("tel:" + donor.getPhone()));
                startActivity(i);
            });

            btnWA.setOnClickListener(v -> {
                String msg = "Hi " + donor.getName() + ", I need "
                        + donor.getBloodGroup() + " blood. Can you please help?";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(android.net.Uri.parse(
                        "https://wa.me/91" + donor.getPhone() + "?text="
                                + android.net.Uri.encode(msg)));
                startActivity(i);
            });

            donorListContainer.addView(card);
        }

        if (filteredDonors.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No donors found");
            empty.setTextSize(14);
            empty.setTextColor(0xFF9E9E9E);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(0, 48, 0, 48);
            donorListContainer.addView(empty);
        }
    }
}