package com.ismt.foodbuddy.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ismt.foodbuddy.model.Plan;
import com.ismt.foodbuddy.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple SQLite helper for the app.
 * Contains two tables:
 * - users: stores registered user credentials (email, password)
 * - plans: stores recipe plans saved by the user (name, instructions, timestamp)
 * - recipes: stores recipes created by admin (name, instructions, ingredients)
 *
 * Note: This is a minimal implementation for demo/learning purposes. Passwords are
 * stored in plain text here which is NOT secure for a production app. Prefer using
 * a secure credential storage mechanism and hashing in real applications.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "foodbuddy.db"; //database name
    private static final int DB_VERSION = 4; // bumped version to add is_admin for users

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_IS_ADMIN = "is_admin"; // 0 or 1

    // Plans table
    public static final String TABLE_PLANS = "plans";
    public static final String COL_PLAN_ID = "id";
    public static final String COL_RECIPE_NAME = "recipe_name";
    public static final String COL_RECIPE_INSTRUCTIONS = "recipe_instructions";
    public static final String COL_CREATED_AT = "created_at";

    // Recipes table (admin-created recipes)
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id"; // note: same column name pattern, used with table context
    public static final String COL_REC_NAME = "name";
    public static final String COL_REC_INSTRUCTIONS = "instructions";
    public static final String COL_REC_INGREDIENTS = "ingredients";

    //constructor to initialize the database with the given name and version
    public DatabaseHelper(Context context) {

        //this is going to create the database with the specified name and version
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String sqlUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_IS_ADMIN + " INTEGER DEFAULT 0" +
                ")";
        db.execSQL(sqlUsers);

        // Create plans table
        String sqlPlans = "CREATE TABLE IF NOT EXISTS " + TABLE_PLANS + " (" +
                COL_PLAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT, " +
                COL_RECIPE_INSTRUCTIONS + " TEXT, " +
                COL_CREATED_AT + " INTEGER" +
                ")";

        //this line executes the SQL command to create the plans table in the database
        db.execSQL(sqlPlans);

        // Create recipes table
        String sqlRecipes = "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REC_NAME + " TEXT, " +
                COL_REC_INSTRUCTIONS + " TEXT, " +
                COL_REC_INGREDIENTS + " TEXT" +
                ")";
        db.execSQL(sqlRecipes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simple upgrade policy: drop and recreate (data loss on upgrade)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    /**
     * Add a new user to the users table.
     * @return true if insert succeeded, false otherwise
     */
    public boolean addUser(String email, String password) {
        return addUser(email, password, false);
    }

    /**
     * Add a new user with admin flag.
     */
    public boolean addUser(String email, String password, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase(); // Ensure writable DB
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_IS_ADMIN, isAdmin ? 1 : 0);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id != -1;
    }

    /**
     * Check if an email is already registered.
     */
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_EMAIL + "=?", new String[]{email},
                null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    /**
     * Validate user credentials (email + password).
     */
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password},
                null, null, null);
        boolean valid = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return valid;
    }

    /**
     * Return true if the provided credentials belong to an admin user.
     */
    public boolean isUserAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_IS_ADMIN},
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password},
                null, null, null);
        boolean isAdmin = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int val = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_ADMIN));
                isAdmin = (val == 1);
            }
            cursor.close();
        }
        db.close();
        return isAdmin;
    }

    /**
     * Add a planned recipe to the plans table.
     * @return true when insertion succeeds
     */
    public boolean addPlan(String recipeName, String instructions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, recipeName);
        values.put(COL_RECIPE_INSTRUCTIONS, instructions);
        values.put(COL_CREATED_AT, System.currentTimeMillis());
        long id = db.insert(TABLE_PLANS, null, values);
        db.close();
        return id != -1;
    }

    //This method is used to remove the plan from the database
    public boolean removePlan(int planId) {
        SQLiteDatabase db = this.getWritableDatabase();

        //this method deletes the row from the plans table where the id matches the provided planId
        int rowsAffected = db.delete(TABLE_PLANS, COL_PLAN_ID + "=?", new String[]{String.valueOf(planId)});
        db.close();
        return rowsAffected > 0;
    }

    /**
     * Check if a plan with the given recipe name already exists. Useful to avoid duplicates.
     */
    public boolean isPlanExists(String recipeName) {
        if (recipeName == null) return false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLANS, new String[]{COL_PLAN_ID},
                COL_RECIPE_NAME + "=?", new String[]{recipeName},
                null, null, null);
        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }


    /**
     * Get all planned recipes ordered by created timestamp descending (newest first).
     */
    public List<Plan> getAllPlans() {
        List<Plan> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLANS,
                new String[]{COL_PLAN_ID, COL_RECIPE_NAME, COL_RECIPE_INSTRUCTIONS, COL_CREATED_AT},
                null, null, null, null, COL_CREATED_AT + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PLAN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_NAME));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow(COL_RECIPE_INSTRUCTIONS));
                long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT));
                Plan p = new Plan(id, name, instructions, createdAt);
                list.add(p);
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    // ---- Recipes related methods ----

    /**
     * Insert a recipe (name, instructions, ingredients) into recipes table.
     * @return true when insertion succeeds
     */
    public boolean addRecipe(String name, String instructions, String ingredients) {
        if (name == null || name.trim().isEmpty()) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_REC_NAME, name);
        values.put(COL_REC_INSTRUCTIONS, instructions == null ? "" : instructions);
        values.put(COL_REC_INGREDIENTS, ingredients == null ? "" : ingredients);
        long id = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return id != -1;
    }

    /**
     * Get all recipes stored in the recipes table ordered by id desc (newest first).
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES,
                new String[]{COL_RECIPE_ID, COL_REC_NAME, COL_REC_INSTRUCTIONS, COL_REC_INGREDIENTS},
                null, null, null, null, COL_RECIPE_ID + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_REC_NAME));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow(COL_REC_INSTRUCTIONS));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COL_REC_INGREDIENTS));
                Recipe r = new Recipe(id, name, instructions, ingredients);
                list.add(r);
            }
            cursor.close();
        }
        db.close();
        return list;
    }
}
