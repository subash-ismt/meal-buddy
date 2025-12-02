package com.ismt.foodbuddy.service;

import com.ismt.foodbuddy.model.Equipment;
import com.ismt.foodbuddy.model.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FitLifeApiService {

    @GET("/workouts")
    Call<List<Workout>> getWorkouts();

    @GET("/equipments")
    Call<List<Equipment>> getEquipments();
}
