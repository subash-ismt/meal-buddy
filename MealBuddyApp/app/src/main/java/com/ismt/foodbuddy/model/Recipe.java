package com.ismt.foodbuddy.model;

public class Recipe {
    private int id;
    private String name;
    private String instructions;
    private String ingredients; // comma-separated or plain text list

    // constructor for recipes loaded from DB with id
    public Recipe(int id, String name, String instructions, String ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    // constructor for creating recipes before persisting (no id yet)
    public Recipe(String name, String instructions, String ingredients) {
        this(-1, name, instructions, ingredients);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getIngredients() {
        return ingredients;
    }
}
