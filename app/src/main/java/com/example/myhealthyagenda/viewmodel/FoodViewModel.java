package com.example.myhealthyagenda.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.SurfaceControl;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.myhealthyagenda.MHADatabase;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.repositories.FoodRepository;

import java.util.List;

public class FoodViewModel  extends AndroidViewModel {
    private FoodRepository foodRepository;
    private LiveData<List<Food>> allFoods;
    private MutableLiveData<Integer> addedFoodId = new MutableLiveData<>();
    private MutableLiveData<Integer> addableFoodId = new MutableLiveData<>();

    private LiveData<Food> mealAddedClickedFood = Transformations.switchMap(this.addedFoodId, foodId ->{
            Log.d(FoodViewModel.class.getSimpleName(),"UPDATING LiveData<Food> refference for ADDED Food of a Meal");
            return foodRepository.findById(foodId);
    });

    private LiveData<Food> addableClickedFood = Transformations.switchMap(this.addableFoodId, foodId->{
        Log.d(FoodViewModel.class.getSimpleName(),"UPDATING LiveData<Food> refference for ADDABLE Food");
        return foodRepository.findById(foodId);
    });

    public FoodViewModel(@NonNull Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
        allFoods = foodRepository.findAll();
    }

    public LiveData<List<Food>> getAllFoods() {
        return allFoods;
    }
    public void setAllFoods(LiveData<List<Food>> allFoods) {
        this.allFoods = allFoods;
    }
    public void setAddedFoodId(int foodId){
        //if(this.addedFoodId.getValue() == null ||  this.addedFoodId.getValue() != foodId) {
            Log.d(FoodViewModel.class.getSimpleName(), "Setting a new Id for the food to be expanded" + foodId);
            this.addedFoodId.setValue(foodId);
        //}
    }
    public void setAddableFoodId(int foodId){
        //if(this.addableFoodId.getValue() == null ||  this.addableFoodId.getValue() != foodId) {
            Log.d(FoodViewModel.class.getSimpleName(), "Setting a new Id for the food to be expanded" + foodId);
            this.addableFoodId.setValue(foodId);
        //}
    }


    public LiveData<Food> getMealAddedClickedFood(){
        return mealAddedClickedFood;
    }

    public LiveData<Food> getAddableClickedFood(){
        return addableClickedFood;
    }
    public void updateFood(Food toUpdate){
        foodRepository.updateFood(toUpdate);
    }
}
