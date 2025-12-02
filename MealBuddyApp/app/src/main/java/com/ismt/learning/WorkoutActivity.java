package com.ismt.learning;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ismt.foodbuddy.R;
import com.ismt.foodbuddy.model.Equipment;
import com.ismt.foodbuddy.model.Workout;
import com.ismt.foodbuddy.service.FitLifeApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //API Call Setup (creating object of retrofit)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/").addConverterFactory(GsonConverterFactory.create())
                .build();


        FitLifeApiService apiService = retrofit.create(FitLifeApiService.class);

        Call<List<Workout>> response =  apiService.getWorkouts();
        response.enqueue(new retrofit2.Callback<List<Workout>>() {
            @Override
            public void onResponse(Call<List<Workout>> call, retrofit2.Response<List<Workout>> response) {
                if(response.isSuccessful()){
                    List<Workout> workouts = response.body();
                    for(Workout workout: workouts){
                        System.out.println("Workout Name: " + workout.getName());
                        System.out.println("Duration: " + workout.getDuration() + " minutes");
                        System.out.println("Difficulty Level: " + workout.getDifficultyLevel());
                        System.out.println("Muscle Group: " + workout.getMuscleGroup());
                        System.out.println("Equipment Needed: " + String.join(", ", workout.getEquipmentNeeded()));
                        System.out.println("-----" );

                    }
                }else{
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
                if(response.isSuccessful()){
                    List<Equipment> equipments = response.body();

                    //print equipment details in the console( checkout catlogs)

                    for(Equipment equipment: equipments){


                        System.out.println("Equipment Name: " + equipment.getName());
                        System.out.println("Available Location: " + equipment.getAvailableLocation());
                        System.out.println("Price: $" + equipment.getPrice());

                        System.out.println("-----" );

                    }
                }else{
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