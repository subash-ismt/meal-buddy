package com.ismt.learning;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.adapter.ToDoListAdaptor;

import java.util.ArrayList;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewToDo;
    private ToDoListAdaptor toDoListAdaptor;

    //sensor related classes
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        List<String> todoItems = new ArrayList<>();
        todoItems.add("Buy groceries");
        todoItems.add("Walk the dog");
        todoItems.add("Read a book");
        todoItems.add("Exercise for 30 minutes");
        todoItems.add("Call a friend");

        toDoListAdaptor = new ToDoListAdaptor(todoItems, this);
        recyclerViewToDo = findViewById(R.id.recyclerViewToDo);
        recyclerViewToDo.setAdapter(toDoListAdaptor);
        recyclerViewToDo.setLayoutManager(new LinearLayoutManager(this));

        // Shake Detector
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Initialize accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(count -> {
            //reset the to-do list on shake
            toDoListAdaptor.resetToDos();
            Toast.makeText(ToDoActivity.this, "List Reset!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
