package com.ismt.foodbuddy.model;

public class Plan {
    private int id;
    private String recipeName;
    private String instructions;
    private long createdAt;

    public Plan(int id, String recipeName, String instructions, long createdAt) {
        this.id = id;
        this.recipeName = recipeName;
        this.instructions = instructions;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getInstructions() {
        return instructions;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
