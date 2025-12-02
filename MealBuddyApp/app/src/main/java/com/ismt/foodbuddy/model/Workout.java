package com.ismt.foodbuddy.model;

import java.util.List;

public class Workout {

    private String name;
    private String duration;
    private String timeUnit;
    private String difficultyLevel;
    private List<String> equipmentNeeded;
    private String muscleGroup;
    public Workout(String name, String duration, String timeUnit, String difficultyLevel, List<String> equipmentNeeded, String muscleGroup) {
        this.name = name;
        this.duration = duration;
        this.timeUnit = timeUnit;
        this.difficultyLevel = difficultyLevel;
        this.equipmentNeeded = equipmentNeeded;
        this.muscleGroup = muscleGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public List<String> getEquipmentNeeded() {
        return equipmentNeeded;
    }

    public void setEquipmentNeeded(List<String> equipmentNeeded) {
        this.equipmentNeeded = equipmentNeeded;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}
