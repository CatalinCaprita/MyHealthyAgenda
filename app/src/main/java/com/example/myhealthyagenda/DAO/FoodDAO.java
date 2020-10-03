package com.example.myhealthyagenda.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myhealthyagenda.food.Food;

import java.util.List;

@Dao
public interface FoodDAO {
    /*
        Basic CRUD operations
     */
    @Insert
    void insertFood(Food food);
    @Update
    void updateFood(Food food);
    @Delete
    void deleteFood(Food food);
    @Query("DELETE FROM FOOD")
    void deleteAll();

    @Query("SELECT * FROM FOOD")
    LiveData<List<Food>> findAll();

    @Query("SELECT * FROM FOOd WHERE FOOD_ID = :foodId")
    LiveData<Food> findById(int foodId);

    @Query("SELECT *  FROM FOOD WHERE NAME LIKE :foodName||'%'")
    LiveData<Food> findByName(String foodName);

    @Query("SELECT * FROM FOOD WHERE NAME LIKE :foodName||'%'")
    LiveData<List<Food>> findAllByName(String foodName);

    @Query("Select * FROM FOOD LIMIT :limit")
    List<Food> findLimitFoods(int limit);
}
