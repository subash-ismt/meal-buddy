package com.ismt.foodbuddy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ismt.foodbuddy.R;

public class GeotagLocationActivity extends AppCompatActivity {

    private Button btnViewOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geotag_location);

        btnViewOnMap = findViewById(R.id.btnViewOnMap);

        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hardcoded location for a park (e.g., ISMT College Butwal, Nepal)
                Uri location = Uri.parse("geo:27.664318965471278, 83.46606493779113?q=ISMT+College+Butwal,+Butwal,+Nepal");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });
    }
}
