package com.example.myhealthyagenda.food;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class MealFoodJoinWithFood {
    @Embedded
    private MealFoodJoin mealFoodJoin;

    @Relation(parentColumn = "CHILD_FOOD_ID",
            entityColumn = "FOOD_ID",
            entity = Food.class)
    private List<Food> foods;

    public MealFoodJoin getMealFoodJoin() {
        return mealFoodJoin;
    }

    public void setMealFoodJoin(MealFoodJoin mealFoodJoin) {
        this.mealFoodJoin = mealFoodJoin;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        for(Food food : this.foods){
            food.setQuantityGrams(mealFoodJoin.getQuantityGrams());
        }
    }

    @Override
    public String toString() {
        return "MealFoodJoinWithFood{" +
                "mealFoodJoin=" + mealFoodJoin +
                ", foods=" + foods +
                '}';
    }
}
