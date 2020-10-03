package com.example.myhealthyagenda.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myhealthyagenda.DAO.MealDAO;
import com.example.myhealthyagenda.DAO.MealFoodJoinDAO;
import com.example.myhealthyagenda.MHADatabase;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealWithFoods;

import java.util.List;

public class MealRepository {

    private MealDAO mealDAO;
    private MealFoodJoinDAO mealFoodJoinDAO;
    private LiveData<List<Meal>> allMeals;
    private static final String TAG = "MealRepository::";

    public MealRepository(Application application){
        MHADatabase db = MHADatabase.getInstance(application);
        mealDAO = db.mealDAO();
        mealFoodJoinDAO = db.mealFoodJoinDAO();
        allMeals = mealDAO.findAll();
    }

    public LiveData<List<Meal>> getAllMeals(){
        return this.allMeals;
    }

    public void insertMeal(Meal meal){
        new InsertMealAsyncTask(this.mealDAO).execute(meal);
    }

    public void deleteMeal(Meal meal){
        new DeleteMealAsyncTask(this.mealDAO).execute();
    }

    public void updateMeal(Meal meal){ new UpdateMealAsyncTask(this.mealDAO).execute(); }

    public LiveData<Meal> getMealById(int mealId){
        return mealDAO.findMealById(mealId);
    }

    public LiveData<List<MealWithFoods>> getAllMealsWithFoods(){
        return null;
    }

    public static class InsertMealAsyncTask extends AsyncTask<Meal,Void,Void>{

        private MealDAO mealDAO;
        public InsertMealAsyncTask(MealDAO mealDAO){
            this.mealDAO = mealDAO;
        }

        @Override
        protected Void doInBackground(Meal... meals) {
            Log.d("InsertMealAsyncTask::doInBackground()","Inserting " + meals[0] + "in background");
            mealDAO.insert(meals[0]);
            return null;
        }
    }

    public static class DeleteMealAsyncTask extends  AsyncTask<Meal, Void, Void>{

        private MealDAO mealDAO;
        public DeleteMealAsyncTask(MealDAO mealDAO){
            this.mealDAO = mealDAO;
        }
        @Override
        protected Void doInBackground(Meal... meals) {
            Log.d("DeleteMealAsyncTask::doInBackground()","Deleting " + meals[0] + "in background");
            this.mealDAO.delete(meals[0]);
            return null;
        }
    }

    public static class UpdateMealAsyncTask extends  AsyncTask<Meal,Void,Void>{

        private MealDAO mealDAO;
        public UpdateMealAsyncTask(MealDAO mealDAO){
            this.mealDAO = mealDAO;
        }

        @Override
        protected Void doInBackground(Meal... meals) {
            Log.d(this.getClass().getSimpleName(),"Updating Meal in background");
            mealDAO.update(meals[0]);
            return null;
        }
    }
}
