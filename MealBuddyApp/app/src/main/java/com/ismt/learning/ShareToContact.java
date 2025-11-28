package com.ismt.learning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.activity.GeotagLocationActivity;
import com.ismt.foodbuddy.adapter.CheckItemsAdapter;
import com.ismt.foodbuddy.model.CheckList;

import java.util.ArrayList;
import java.util.List;

public class ShareToContact extends AppCompatActivity {

    private Button btnShareSms;
    private Button btnGeotag;
    private RecyclerView recyclerViewChecklist;
    private CheckItemsAdapter checkItemsAdapter;
    private List<CheckList> checklist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_contact);

        // 1. Initialize Views
        btnShareSms = findViewById(R.id.btnShareSms);
        btnGeotag = findViewById(R.id.btnGeotag);
        recyclerViewChecklist = findViewById(R.id.recyclerViewChecklist);

        // Initialize checklist data
        checklist = new ArrayList<>();
        checklist.add(new CheckList("1kg Sugar", "Bhatbhateni"));
        checklist.add(new CheckList("2L Milk", "Bhatbhateni"));
        checklist.add(new CheckList("1 dozen Eggs", "Local store"));

        // Setup RecyclerView
        recyclerViewChecklist.setLayoutManager(new LinearLayoutManager(this));
        checkItemsAdapter = new CheckItemsAdapter(this, checklist);
        recyclerViewChecklist.setAdapter(checkItemsAdapter);


        // 2. Set Button Click Listener
        btnShareSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareChecklistViaSms();
            }
        });

        btnGeotag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareToContact.this, GeotagLocationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void shareChecklistViaSms() {
        StringBuilder smsText = new StringBuilder();
        smsText.append("Can you do a favour, I a quite busy. So not able to purchase " +
                "below items. can you buy for me. \n Here is your checklist:\n");
        for (CheckList item : checklist) {
            smsText.append("- ").append(item.getItemName()).append(" (").append(item.getAvailableLocation()).append(")\n");
        }

        try {
            // 3. Create the Intent
            // ACTION_SENDTO ensures we target SMS/MMS apps specifically
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO); //SMS/MMS

            // Set the data to 'smsto:' to filter for SMS apps only
            smsIntent.setData(Uri.parse("smsto:"));

            // Put the text message content (Key must be "sms_body")
            smsIntent.putExtra("sms_body", smsText.toString());

            // 4. Launch the Activity
            // This will open the user's default SMS app with the text pre-filled
            startActivity(smsIntent);



        } catch (Exception e) {
            // Handle error if no SMS app is found (rare on phones)
            //   Toast.makeText(this, "No SMS app found.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Toast.makeText(this, "No SMS app found", Toast.LENGTH_SHORT).show();

        }


    }


}
