package com.example.myhealthyagenda.food;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class MealWithFoods {
    @Embedded
    private Meal meal;
    @Relation(entity = MealFoodJoin.class,
    parentColumn = "MEAL_ID",
    entityColumn = "OWNER_MEAL_ID")
    private List<MealFoodJoinWithFood> mealFoodJoinsWithFoods;

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public List<MealFoodJoinWithFood> getMealFoodJoinsWithFoods() {
        return mealFoodJoinsWithFoods;
    }

    public void setMealFoodJoinsWithFoods(List<MealFoodJoinWithFood> mealFoodJoinsWithFoods) {
        this.mealFoodJoinsWithFoods = mealFoodJoinsWithFoods;
    }
}
