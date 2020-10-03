package com.example.myhealthyagenda.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealWithFoods;

import java.util.List;

@Dao
public interface MealDAO {
    @Insert
    void insert(Meal meal);
    @Delete
    void delete(Meal meal);
    @Query("DELETE FROM MEAL")
    void deleteAll();
    @Update
    void update(Meal meal);
    @Query("SELECT * FROM MEAL")
    LiveData<List<Meal>> findAll();
    @Query("SELECT * FROM MEAL WHERE MEAL_ID = :mealId")
    LiveData<Meal> findMealById(int mealId);
    @Query("SELECT * FROM MEAL WHERE NAME = :mealName")
    LiveData<Meal> findMealByName(String mealName);


}
