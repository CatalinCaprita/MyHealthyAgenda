package com.example.myhealthyagenda.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myhealthyagenda.DAO.MealFoodJoinDAO;
import com.example.myhealthyagenda.MHADatabase;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealFoodJoin;
import com.example.myhealthyagenda.food.MealFoodJoinWithFood;
import com.example.myhealthyagenda.food.MealWithFoods;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MealFoodJoinRepository {
    private MealFoodJoinDAO mealFoodJoinDAO;

    public MealFoodJoinRepository(Application application){
        this.mealFoodJoinDAO = MHADatabase.getInstance(application).mealFoodJoinDAO();
    }
    public void insertFoodIntoMeal(MealFoodJoin assoctiation){
            new InsertFoodIntoMealAsyncTask(mealFoodJoinDAO).execute(assoctiation);
    }

    public void deleteFoodFromMeal(MealFoodJoin association){
            new DeleteFoodFromMealAsyncTask(mealFoodJoinDAO).execute(association);
    }
    public void deleteFoodFromMealByFoodId(int mealId, int foodId){
        new DeleteFoodFromMealByIdAsyncTask(mealFoodJoinDAO).execute(mealId,foodId);
    }
    public void updateFoodInMeal(MealFoodJoin association){
            new UpdateFoodFromMealAsyncTask(mealFoodJoinDAO).execute(association);
    }

    public LiveData<List<Food>> findAllFoodsByMealId(int mealId){
        Log.d(this.getClass().getSimpleName(),"Fetching all the foods for mealId" + mealId);
        return mealFoodJoinDAO.getAllFoodsByMealId(mealId);
    }

    public LiveData<List<MealWithFoods>> findAllMealsWithMealFoodJoins(){
        Log.d(this.getClass().getSimpleName(),"Fetching all MEAL, each with ALL MEAL_FOOD_JOIN ");
        return mealFoodJoinDAO.finddAllMealsWithMealFoodJoins();
    }

    public LiveData<List<MealFoodJoin>> findAllJoinsByMealId(int mealId){
        try {
            return new FindMealFoodJoinsByMealIdAsyncTask(mealFoodJoinDAO).execute(mealId).get();
        } catch (ExecutionException e) {
            Log.e(e.getCause().toString(),e.getMessage());
        } catch (InterruptedException e) {
            Log.e(e.getCause().toString(),e.getMessage());
        }
        return null;
    }

    public LiveData<List<MealFoodJoinWithFood>> findAllMealFoodJoinWithFoodsByMealId(int mealId){
        Log.d(this.getClass().getSimpleName() + "::findAllMealFoodJoinWithFoods"," fetching List for MEAL_ID = " + mealId);
        return mealFoodJoinDAO.findAllMealFoodJoinsWithFoodsByMealId(mealId);
    }

    public LiveData<List<MealWithFoods>> findAllMealsWithAllFoods(){
        try {
            return new FindAllMealsWithFoodsAsyncTask(mealFoodJoinDAO).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Meal> findAllMeals(){
        return mealFoodJoinDAO.findAllMeals();
    }
    public static class InsertFoodIntoMealAsyncTask extends AsyncTask<MealFoodJoin,Void,Void>{

        private MealFoodJoinDAO mealFoodJoinDAO;
        public InsertFoodIntoMealAsyncTask(MealFoodJoinDAO mealFoodJoinDAO){
            this.mealFoodJoinDAO = mealFoodJoinDAO;
        }
        @Override
        protected Void doInBackground(MealFoodJoin... mealFoodJoins) {
            Log.d(this.getClass().getSimpleName(),"Inserting FOOD - MEAL association into the MealFoodJoin Table...");
            mealFoodJoinDAO.insertFoodIntoMeal(mealFoodJoins[0]);
            return null;
        }
    }
    public static class DeleteFoodFromMealAsyncTask extends AsyncTask<MealFoodJoin,Void,Void>{

        private MealFoodJoinDAO mealFoodJoinDAO;
        public DeleteFoodFromMealAsyncTask(MealFoodJoinDAO mealFoodJoinDAO){
            this.mealFoodJoinDAO = mealFoodJoinDAO;
        }
        @Override
        protected Void doInBackground(MealFoodJoin... mealFoodJoins) {
            Log.d(this.getClass().getSimpleName(),"Removing MEAL-FOOD association...");
            mealFoodJoinDAO.deleteFoodFromMeal(mealFoodJoins[0]);
            return null;
        }
    }

    public static class DeleteFoodFromMealByIdAsyncTask extends AsyncTask<Integer,Void,Void>{

        private MealFoodJoinDAO mealFoodJoinDAO;
        public DeleteFoodFromMealByIdAsyncTask(MealFoodJoinDAO mealFoodJoinDAO){
            this.mealFoodJoinDAO = mealFoodJoinDAO;
        }
        @Override
        protected Void doInBackground(Integer... ids) {
            Log.d(this.getClass().getSimpleName(),"Removing MEAL-FOOD association by mealId:" + ids[0] + " and foodId:" + ids[1]);
            mealFoodJoinDAO.deleteFoodInMealByFoodId(ids[0],ids[1]);
            return null;
        }
    }
    public static class UpdateFoodFromMealAsyncTask extends AsyncTask<MealFoodJoin,Void,Void>{

        private MealFoodJoinDAO mealFoodJoinDAO;
        public UpdateFoodFromMealAsyncTask(MealFoodJoinDAO mealFoodJoinDAO){
            this.mealFoodJoinDAO = mealFoodJoinDAO;
        }
        @Override
        protected Void doInBackground(MealFoodJoin... mealFoodJoins) {
            Log.d(this.getClass().getSimpleName(),"Updatgin FOOD - MEAL association into the MealFoodJoin Table...");
            mealFoodJoinDAO.updateFoodInMeal(mealFoodJoins[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(this.getClass().getSimpleName(),"MealFoodJoin updated!");

        }
    }

    public static class FindMealFoodJoinsByMealIdAsyncTask extends AsyncTask<Integer,Void,LiveData<List<MealFoodJoin>>>{

        private MealFoodJoinDAO mealFoodJoinDAO;
        public FindMealFoodJoinsByMealIdAsyncTask(MealFoodJoinDAO mealFoodJoinDAO){
            this.mealFoodJoinDAO = mealFoodJoinDAO;
        }
        @Override
        protected LiveData<List<MealFoodJoin>> doInBackground(Integer... integers) {
            Log.d(this.getClass().getSimpleName(),"Fetching different Thread :All MealFoodJoin rows for MEAL_ID: " + integers[0]);
            return mealFoodJoinDAO.findAllJoinsByMealId(integers[0]);
        }
    }

    public static class FindAllMealFoodJoinsWithFoodAsyncTask extends AsyncTask<Integer,Void,LiveData<List<MealFoodJoinWithFood>>>{

        private MealFoodJoinDAO mealFoodJoinDAO;
        public FindAllMealFoodJoinsWithFoodAsyncTask(MealFoodJoinDAO mealFoodJoinDAO){
            this.mealFoodJoinDAO = mealFoodJoinDAO;
        }
        @Override
        protected LiveData<List<MealFoodJoinWithFood>> doInBackground(Integer... integers) {
            Log.d(this.getClass().getSimpleName(),"Fetching different Thread :All MealFoodJoinWithFood rows for MEAL_ID: " + integers[0]);
            return mealFoodJoinDAO.findAllMealFoodJoinsWithFoodsByMealId(integers[0]);
        }
    }
    public static class FindAllMealsWithFoodsAsyncTask extends AsyncTask<Integer,Void,LiveData<List<MealWithFoods>>>{

        private MealFoodJoinDAO mealFoodJoinDAO;
        public FindAllMealsWithFoodsAsyncTask(MealFoodJoinDAO mealFoodJoinDAO){
            this.mealFoodJoinDAO = mealFoodJoinDAO;
        }
        @Override
        protected LiveData<List<MealWithFoods>> doInBackground(Integer... integers) {
            Log.d(this.getClass().getSimpleName(),"Fetching different Thread :All MealsWithFoods... ");
            return mealFoodJoinDAO.findAllMealsWithAllFoods();
        }
    }

}
