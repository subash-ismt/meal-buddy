package com.ismt.learning;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ismt.foodbuddy.model.Recipe;
import com.ismt.foodbuddy.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreService {


    //object creation of Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String USERS_COLLECTION = "users";
    private static final String TAG = "FirestoreService";

    public interface RegistrationCallback {
        void onRegistrationSuccess();
        void onRegistrationFailure(String errorMessage);
    }

    public void registerUser(String email, String password, boolean isAdmin, final RegistrationCallback callback) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setAdmin(isAdmin);
        db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)//condition to check if email already exists
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                if (callback != null) {
                                    callback.onRegistrationFailure("Email already registered");
                                }
                            } else {

                                //if user is not preset then add user to the database

//                                Map<String, Object> user = new HashMap<>();
//                                user.put("email", email);
//                                user.put("password", password); // WARNING: Insecure
//                                user.put("isAdmin", isAdmin);



                                db.collection(USERS_COLLECTION)
                                        .add(user)
                                        .addOnSuccessListener(documentReference -> {
                                            if (callback != null) {
                                                callback.onRegistrationSuccess();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            if (callback != null) {
                                                callback.onRegistrationFailure(e.getMessage());
                                            }
                                        });
                            }
                        } else {
                            if (callback != null) {
                                callback.onRegistrationFailure(task.getException() != null ? task.getException().getMessage() : "An unknown error occurred");
                            }
                        }
                    }
                });
    }

    public void getUser(String email) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email) //condition to check if email exists
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        Log.d(TAG, "User data: " + document.getData());
                    } else {
                        Log.w(TAG, "Error getting user.", task.getException());
                    }
                });
    }

    public void updateUser(String email, Map<String, Object> updates) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        String docId = task.getResult().getDocuments().get(0).getId();
                        db.collection(USERS_COLLECTION).document(docId)
                                .update(updates) //this method is used to update the user details
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully updated!"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error updating user", e));
                    } else {
                        Log.w(TAG, "User not found for update.", task.getException());
                    }
                });
    }

    public void deleteUser(String email) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        String docId = task.getResult().getDocuments().get(0).getId();
                        db.collection(USERS_COLLECTION).document(docId)
                                .delete() //this method is used to delete the user details
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully deleted!"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error deleting user", e));
                    } else {
                        Log.w(TAG, "User not found for deletion.", task.getException());
                    }
                });
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        db.collection("recipes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Recipe recipe = document.toObject(Recipe.class);
                            recipeList.add(recipe);
                        }
                    } else {
                        Log.w(TAG, "Error getting recipes.", task.getException());
                    }
                });
        return recipeList;
    }
}
