package com.example.myhealthyagenda.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealFoodJoin;
import com.example.myhealthyagenda.food.MealFoodJoinWithFood;
import com.example.myhealthyagenda.food.MealWithFoods;

import java.util.List;

@Dao
public interface MealFoodJoinDAO {
    @Query("SELECT TARGET.FOOD_ID as 'FOOD_ID', TARGET.NAME as 'NAME', TARGET.SOURCE as 'SOURCE', TARGET.CARBS_GRAMS as 'CARBS_GRAMS', TARGET.PROTEIN_GRAMS as 'PROTEIN_GRAMS',TARGET.FAT_GRAMS as 'FAT_GRAMS'," +
            "MFJ.QUANTITY_GRAMS as 'QUANTITY_GRAMS' FROM FOOD AS TARGET JOIN  MEAL_FOOD_JOIN  AS MFJ ON (TARGET.FOOD_ID = MFJ.CHILD_FOOD_ID)" +
            "WHERE MFJ.OWNER_MEAL_ID = :mealId")
    LiveData<List<Food>> getAllFoodsByMealId(int mealId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFoodIntoMeal(MealFoodJoin mealFoodJoin);
    @Delete
    void deleteFoodFromMeal(MealFoodJoin mealFoodJoin);
    @Update
    void updateFoodInMeal(MealFoodJoin mealFoodJoin);
    @Query("DELETE FROM MEAL_FOOD_JOIN WHERE OWNER_MEAL_ID = :mealId AND CHILD_FOOD_ID = :foodId")
    void deleteFoodInMealByFoodId(int mealId, int foodId);
    @Query("DELETE FROM MEAL_FOOD_JOIN")
    void deleteAll();
    @Query("SELECT * FROM MEAL")
    LiveData<List<MealWithFoods>> finddAllMealsWithMealFoodJoins();
    @Query("SELECT * FROM MEAL")
    List<Meal> findAllMeals();
    @Query("SELECT * FROM MEAL_FOOD_JOIN WHERE OWNER_MEAL_ID = :ownerMealId")
    LiveData<List<MealFoodJoin>> findAllJoinsByMealId(int ownerMealId);
    @Transaction
    @Query("SELECT * FROM MEAL_FOOD_JOIN WHERE OWNER_MEAL_ID = :ownerMealId")
    LiveData<List<MealFoodJoinWithFood>> findAllMealFoodJoinsWithFoodsByMealId(int ownerMealId);

    @Transaction
    @Query("SELECT * FROM MEAL")
    LiveData<List<MealWithFoods>> findAllMealsWithAllFoods();
    /*
    @Transaction
    @Query("SELECT * FROM MEAL WHERE USER_ID = :userId")
    */

}
