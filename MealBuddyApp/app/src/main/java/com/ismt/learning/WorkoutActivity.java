package com.ismt.learning;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.adapter.EquipmentAdapter;
import com.ismt.foodbuddy.adapter.WorkoutAdapter;
import com.ismt.foodbuddy.model.Equipment;
import com.ismt.foodbuddy.model.Workout;
import com.ismt.foodbuddy.service.FitLifeApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkoutActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWorkouts;
    private RecyclerView recyclerViewEquipments;
    private WorkoutAdapter workoutAdapter;
    private EquipmentAdapter equipmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        recyclerViewWorkouts = findViewById(R.id.recyclerViewWorkouts);
        recyclerViewEquipments = findViewById(R.id.recyclerViewEquipments);

        recyclerViewWorkouts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEquipments.setLayoutManager(new LinearLayoutManager(this));

        //API Call Setup (creating object of retrofit)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create())
                .build();


        FitLifeApiService apiService = retrofit.create(FitLifeApiService.class);

        Call<List<Workout>> response = apiService.getWorkouts();
        //api call for workout list
        response.enqueue(new retrofit2.Callback<List<Workout>>() {
            @Override
            public void onResponse(Call<List<Workout>> call, retrofit2.Response<List<Workout>> response) {
                if (response.isSuccessful()) {
                    List<Workout> workouts = response.body();
                    workoutAdapter = new WorkoutAdapter(workouts);
                    recyclerViewWorkouts.setAdapter(workoutAdapter);
                } else {
                    System.out.println("Request not successful. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Workout>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });


        //calling equipment api
        Call<List<Equipment>> equipmentCall = apiService.getEquipments();
        equipmentCall.enqueue(new retrofit2.Callback<List<Equipment>>() {
            @Override
            public void onResponse(Call<List<Equipment>> call, retrofit2.Response<List<Equipment>> response) {
                if (response.isSuccessful()) {
                    List<Equipment> equipments = response.body();
                    equipmentAdapter = new EquipmentAdapter(equipments);
                    recyclerViewEquipments.setAdapter(equipmentAdapter);
                } else {
                    System.out.println("Request not successful. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Equipment>> call, Throwable t) {
                System.out.println("Error: " + t.getMessage());
            }
        });


    }
}
