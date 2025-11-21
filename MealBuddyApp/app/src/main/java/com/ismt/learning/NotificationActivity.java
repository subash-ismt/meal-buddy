package com.ismt.learning;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ismt.foodbuddy.R;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACT_PERMISSION = 1;
    private ArrayList<String> contactsList = new ArrayList<>();
    private ArrayList<String> contactNumbers = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Button notificationBtn = findViewById(R.id.show_notification_button);

        notificationBtn.setOnClickListener(v -> {

            System.out.println("Notification button clicked");

            //now code to show the notification
            showNotification();



        });


//        Uri uri = Uri.parse("tel:9807868686");
//
//        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
//        startActivity(intent);

       //Notification.show(this);
    }

    private void showNotification() {
        //this code is used to ask the permission for post notification

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            //this code is used to ask permission for post notification
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        } else {
            Notification.show(this);
        }

        }


}