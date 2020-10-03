package com.example.myhealthyagenda.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealFoodJoin;
import com.example.myhealthyagenda.food.MealFoodJoinWithFood;
import com.example.myhealthyagenda.food.MealWithFoods;
import com.example.myhealthyagenda.repositories.MealFoodJoinRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealFoodJoinViewModel extends AndroidViewModel {
    MealFoodJoinRepository mealFoodJoinRepository;

    private LiveData<List<MealWithFoods>> mealsWithFoods;

    public MealFoodJoinViewModel(@NonNull Application application) {
        super(application);
        mealFoodJoinRepository = new MealFoodJoinRepository(application);
        mealsWithFoods = mealFoodJoinRepository.findAllMealsWithAllFoods();
    }

    public void insertFoodIntoMeal(MealFoodJoin mealFoodJoin){
        mealFoodJoinRepository.insertFoodIntoMeal(mealFoodJoin);
    }
    public void deleteFoodFromMeal(MealFoodJoin association){
        mealFoodJoinRepository.deleteFoodFromMeal(association);
    }
    public void updateFoodInMeal(MealFoodJoin association){
        mealFoodJoinRepository.updateFoodInMeal(association);
    }
    public void deleteFoodFromMealByFoodId(int mealId, int foodId){
        mealFoodJoinRepository.deleteFoodFromMealByFoodId(mealId,foodId);
    }
    public LiveData<List<MealWithFoods>> getMealsWithFoods() {
        return mealsWithFoods;
    }

    public void setMealsWithFoods(LiveData<List<MealWithFoods>> mealsWithFoods) {
        this.mealsWithFoods = mealsWithFoods;
    }

    public  LiveData<List<MealFoodJoinWithFood>> getMealFoodJoinWithFoodByMealId(int mealId){
        return mealFoodJoinRepository.findAllMealFoodJoinWithFoodsByMealId(mealId);
    }
}
