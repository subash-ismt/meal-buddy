package com.ismt.learning;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class FirestoreService {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String USERS_COLLECTION = "users";
    private static final String TAG = "FirestoreService";

    public interface RegistrationCallback {
        void onRegistrationSuccess();
        void onRegistrationFailure(String errorMessage);
    }

    public void registerUser(String email, String password, boolean isAdmin, final RegistrationCallback callback) {
        db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)
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
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("password", password); // WARNING: Insecure
                                user.put("isAdmin", isAdmin);

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
                .whereEqualTo("email", email)
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
                                .update(updates)
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
                                .delete()
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully deleted!"))
                                .addOnFailureListener(e -> Log.w(TAG, "Error deleting user", e));
                    } else {
                        Log.w(TAG, "User not found for deletion.", task.getException());
                    }
                });
    }
}
