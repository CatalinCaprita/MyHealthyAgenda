package com.example.myhealthyagenda.pages.home;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myhealthyagenda.BaseMenuActivity;
import com.example.myhealthyagenda.DAO.FoodDAO;
import com.example.myhealthyagenda.DAO.MealDAO;
import com.example.myhealthyagenda.MHADatabase;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealWithFoods;
import com.example.myhealthyagenda.viewmodel.FoodViewModel;
import com.example.myhealthyagenda.viewmodel.MealFoodJoinViewModel;
import com.example.myhealthyagenda.viewmodel.MealViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class LoadingActivity extends AppCompatActivity implements MHADatabase.PopulatorAsyncTask.OnPopulationCompleteListener {
    private static final String TAG = LoadingActivity.class.getSimpleName();
    public static final String EXTRA_USER_ACTUAL_KCAL = "USER_ACTUAL_KCAL";
    private boolean foodsLoaded = false;
    /*
        Resources to Load
          Foods
          User-Defined Meals
          EveryFood For Every Meal
     */
    private static final int RESOURCES_TO_LOAD = 3;
    private int remainingResources = RESOURCES_TO_LOAD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d(TAG + "onCreate","Instantiating DB");

        new MHADatabase.PopulatorAsyncTask(MHADatabase.getInstance(this),this).execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPopulationComplete() {
        Log.d(TAG +"::onPopulationComplete","The Db Has been Populated. Calculating User Total Calories");
        MealFoodJoinViewModel mealFoodJoinViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealFoodJoinViewModel.class);

        mealFoodJoinViewModel.getMealsWithFoods().observe(this,new Observer<List<MealWithFoods>>(){
            @Override
            public void onChanged(List<MealWithFoods> mealWithFoods) {
                if(mealWithFoods != null){
                    Log.d(LoadingActivity.TAG + "onMealWithFoodsLoaded","Calculating Total User Calories");
                    double todayKcal = 0;
                     todayKcal = mealWithFoods.stream().flatMap( mwf -> mwf.getMealFoodJoinsWithFoods().stream())
                            .collect(Collectors.summingDouble(mealFoodJoinWithFood ->
                                mealFoodJoinWithFood.getFoods().get(0).getTotalKCal()));
                    Log.d(LoadingActivity.TAG + "onMealWithFoodsLoaded","User Total Kcal:" + todayKcal + " Launching Main Activity");
                    Intent startMainActivity = new Intent();
                    startMainActivity.putExtra(EXTRA_USER_ACTUAL_KCAL,(int)todayKcal);
                    setResult(Activity.RESULT_OK,startMainActivity);
                    LoadingActivity.this.finish();
                }
            }
        });

        }

}
