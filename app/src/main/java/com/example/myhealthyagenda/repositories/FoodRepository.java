package com.example.myhealthyagenda.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;
import androidx.room.Update;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.DAO.FoodDAO;
import com.example.myhealthyagenda.MHADatabase;
import com.example.myhealthyagenda.food.Food;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodRepository{
    private FoodDAO foodDAO;
    private LiveData<List<Food>> allFoods;

    public FoodRepository(Application application){
        MHADatabase mhaDatabase = MHADatabase.getInstance(application);
        this.foodDAO = mhaDatabase.foodDAO();
        Log.d(this.getClass().getSimpleName(),"Requesting findAll() from foodDAO");
        this.allFoods = foodDAO.findAll();
    }

    public void insertFood(Food food){
        new InsertAsyncTask(foodDAO).execute(food);
    }

    public void updateFood(Food food){
        new UpdateAsyncTask(foodDAO).execute(food);
    }

    public void deleteFood(Food food){
        new DeleteAsyncTask(foodDAO).execute(food);
    }

    public LiveData<Food> findById(int foodId) {
        Log.d(FoodRepository.class.getSimpleName(), "Finding By AsyncTask");
        FindByIdAsyncTask fid = new FindByIdAsyncTask(foodDAO);
        try {
            return fid.execute(foodId).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
            return null;
        }
        //return foodDAO.findById(foodId);}
    }
    public void  findAllByName(FindByNameAsyncTask.OnPostExecuteListener listener, String foodName) throws InterruptedException {
            new FindByNameAsyncTask(foodDAO,listener).execute(foodName);
    }

    public LiveData<List<Food>> findAll(){
        return  this.allFoods;
    }

    private static class InsertAsyncTask extends AsyncTask<Food,Void,Void> {
        private FoodDAO foodDAO;

        public InsertAsyncTask(FoodDAO foodDAO){
            this.foodDAO = foodDAO;
        }
        @Override
        protected Void doInBackground(Food... foods) {
            foodDAO.insertFood(foods[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends  AsyncTask<Food,Void,Void>{
        private FoodDAO foodDAO;

        public UpdateAsyncTask(FoodDAO foodDAO){
            this.foodDAO = foodDAO;
        }
        @Override
        protected Void doInBackground(Food... foods) {
            Log.d(this.getClass().getSimpleName(),"Updating Food in Background...");
            foodDAO.updateFood(foods[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends  AsyncTask<Food,Void,Void>{
        private FoodDAO foodDAO;

        public DeleteAsyncTask(FoodDAO foodDAO){
            this.foodDAO = foodDAO;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Food... foods) {
            foodDAO.deleteFood(foods[0]);
            return null;
        }
    }

    private static class FindByNameAsyncTask extends AsyncTask<String,Void,Void>{
        private FoodDAO foodDAO;
        private LiveData<List<Food>> queryResult;
        private OnPostExecuteListener onPostExecuteListener;

        public FindByNameAsyncTask(FoodDAO foodDAO,OnPostExecuteListener onPostExecuteListener){
            this.foodDAO = foodDAO;
            this.onPostExecuteListener = onPostExecuteListener;
        }

        @Override
        protected Void doInBackground(String... strings) {
            queryResult = foodDAO.findAllByName(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onPostExecuteListener.findByNameQuerResponse(this.queryResult);
        }

        public interface OnPostExecuteListener{
            void findByNameQuerResponse(LiveData<List<Food>> queryResult);
        }
    }

    private static class FindByIdAsyncTask extends  AsyncTask<Integer,Void,LiveData<Food>>{
        private FoodDAO foodDAO;
        private LiveData<Food> queryResult;
        public FindByIdAsyncTask(FoodDAO foodDAO){
            this.foodDAO = foodDAO;
        }

        @Override
        protected LiveData<Food> doInBackground(Integer... integers) {
            Log.d(FindByIdAsyncTask.class.getSimpleName()," Executing findById in a separate  thread!");
            queryResult = foodDAO.findById(integers[0]);
            return queryResult;
        }

    }
}
