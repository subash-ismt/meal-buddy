package com.ismt.foodbuddy.service;

import android.provider.ContactsContract;

import com.ismt.foodbuddy.model.Equipment;
import com.ismt.foodbuddy.model.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FitLifeApiService {

    @GET("/workouts")
    Call<List<Workout>> getWorkouts();

    @GET("/equipments")
    Call<List<Equipment>> getEquipments();

    @GET("/profile")
    Call<ContactsContract.Profile> getProfileDetails();

    @POST("/profile")
    Call<ContactsContract.Profile> updateProfileDetails(ContactsContract.Profile profile);
}
