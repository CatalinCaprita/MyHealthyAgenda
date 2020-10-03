package com.example.myhealthyagenda.food;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.net.InetAddress;

@Entity(tableName = "MEAL_FOOD_JOIN",
primaryKeys = {"OWNER_MEAL_ID","CHILD_FOOD_ID"},
        foreignKeys = {
        @ForeignKey(entity = Meal.class,
        parentColumns = "MEAL_ID",
        childColumns = "OWNER_MEAL_ID"),
        @ForeignKey(entity = Food.class,
        parentColumns = "FOOD_ID",
        childColumns = "CHILD_FOOD_ID")
        }
)
public class MealFoodJoin {

    @ColumnInfo(name = "OWNER_MEAL_ID")
    private int ownerMealId;
    @ColumnInfo(name = "CHILD_FOOD_ID")
    private int childFoodId;
    @ColumnInfo(name = "QUANTITY_GRAMS")
    private double quantityGrams;

    public MealFoodJoin(int ownerMealId, int childFoodId,double quantityGrams) {
        this.ownerMealId = ownerMealId;
        this.childFoodId = childFoodId;
        this.quantityGrams = quantityGrams;
    }

    public double getQuantityGrams() {
        return quantityGrams;
    }

    public void setQuantityGrams(double quantityGrams) {
        this.quantityGrams = quantityGrams;
    }

    public int getOwnerMealId() {
        return ownerMealId;
    }

    public void setOwnerMealId(int ownerMealId) {
        this.ownerMealId = ownerMealId;
    }

    public int getChildFoodId() {
        return childFoodId;
    }

    public void setChildFoodId(int childFoodId) {
        this.childFoodId = childFoodId;
    }

    @Override
    public String toString() {
        return "MealFoodJoin{" +
                "ownerMealId=" + ownerMealId +
                ", childFoodId=" + childFoodId +
                ", quantityGrams=" + quantityGrams +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(!(obj instanceof  MealFoodJoin)) return false;
        MealFoodJoin mfj = (MealFoodJoin) obj;
        return this.getChildFoodId() == mfj.getChildFoodId() &&
                this.getOwnerMealId() == mfj.getOwnerMealId() &&
                this.quantityGrams == mfj.getQuantityGrams();
    }
}
