package com.example.myhealthyagenda.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.myhealthyagenda.MHADatabase;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealWithFoods;
import com.example.myhealthyagenda.repositories.MealRepository;

import java.util.List;

public class MealViewModel extends AndroidViewModel {

    private LiveData<List<Meal>> allMeals;
    private MealRepository mealRepository;
    private MutableLiveData<Integer> mealToAddFoodId = new MutableLiveData<>();
    private MutableLiveData<Integer> mealToEditFoodId = new MutableLiveData<>();

    private LiveData<Meal> mealToAddFood = Transformations.switchMap(this.mealToAddFoodId, mealId ->{
        Log.d(this.getClass().getSimpleName(),"MealId UPDATED. Fetching the MEAL to add a FOOD to" + mealId);
        return mealRepository.getMealById(mealId);
    });

    private LiveData<Meal> mealToEditFood = Transformations.switchMap(this.mealToEditFoodId, mealId->{
        if(this.mealToAddFoodId.getValue() != null && mealId.equals(this.mealToAddFoodId.getValue()))
            return this.mealToAddFood;
        return mealRepository.getMealById(mealId);
    });
    public MealViewModel(Application application){
        super(application);
        mealRepository = new MealRepository(application);
        allMeals = mealRepository.getAllMeals();
    }

    public LiveData<Meal> getMealToEditFood() {
        return mealToEditFood;
    }

    public void setMealToEditFood(LiveData<Meal> mealToEditFood) {
        this.mealToEditFood = mealToEditFood;
    }

    public MutableLiveData<Integer> getMealToAddFoodId() {
        return mealToAddFoodId;
    }

    public void setMealToAddFoodId(MutableLiveData<Integer> mealToAddFoodId) {
        this.mealToAddFoodId = mealToAddFoodId;
    }

    public MutableLiveData<Integer> getMealToEditFoodId() {
        return mealToEditFoodId;
    }

    public void setMealToEditFoodId(MutableLiveData<Integer> mealToEditFoodId) {
        this.mealToEditFoodId = mealToEditFoodId;
    }

    public int getMealToEditFoodIdValue() { return mealToEditFoodId.getValue(); }
    public void setMealToEditFoodId(int mealToEditFoodIt) { this.mealToEditFoodId.setValue(mealToEditFoodIt); }
    public void setMealToAddFoodId(int mealId){
        mealToAddFoodId.setValue(mealId);
    }
    public int getMealToAddFoodIdValue(){return mealToAddFoodId.getValue();}
    public LiveData<Meal> getMealToAddFood(){
        return mealToAddFood;
    }
    public LiveData<List<Meal>> getAllMeals() {
        return allMeals;
    }
    public void updateMeal(Meal updatedMeal){
        mealRepository.updateMeal(updatedMeal);
    }

}
