package com.example.myhealthyagenda;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myhealthyagenda.DAO.FoodDAO;
import com.example.myhealthyagenda.DAO.MealDAO;
import com.example.myhealthyagenda.DAO.MealFoodJoinDAO;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealFoodJoin;

import java.util.List;
import java.util.Random;

@Database(entities = {Food.class, Meal.class, MealFoodJoin.class}, version = 11)
public abstract class MHADatabase extends RoomDatabase {

    private static final String DB_FILENAME = "my_healthy_agenda.db";
    private static MHADatabase instance;

    /*
        Just like decalring Beans, we delgate the @Database Implementation as the HibernateConfigClass which can produce beans
        Each and Every DAO is actually a bean accessing the d
     */
    public abstract FoodDAO foodDAO();
    public abstract MealDAO mealDAO();
    public abstract MealFoodJoinDAO mealFoodJoinDAO();

    public static synchronized MHADatabase getInstance(Context context){

        if(instance == null){
            Log.d(MHADatabase.class.getSimpleName() +" getInstance","Building the Database");
            instance = Room.databaseBuilder(context,
                    MHADatabase.class,
                    DB_FILENAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback populatorCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
    public static class PopulatorAsyncTask extends AsyncTask<Void,Void,Void>{
        private FoodDAO foodDAO;
        private MealDAO mealDAO;
        private MealFoodJoinDAO mealFoodJoinDAO;
        private OnPopulationCompleteListener onPopulationCompleteListener;
        public PopulatorAsyncTask(MHADatabase db,
                                  OnPopulationCompleteListener onPopulationCompleteListener){
            this.foodDAO = db.foodDAO();
            this.mealDAO = db.mealDAO();
            this.mealFoodJoinDAO = db.mealFoodJoinDAO();
            this.onPopulationCompleteListener = onPopulationCompleteListener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            /*mealFoodJoinDAO.deleteAll();
            Log.d(MHADatabase.class.getSimpleName(),"Deleting all The Rows from FOOD Table ");
            foodDAO.deleteAll();
            Log.d(MHADatabase.class.getSimpleName(),"Populating the FOOD Table");
            final Random r = new Random(System.currentTimeMillis());
            for(int i=0 ; i< 20; i++) {
                foodDAO.insertFood(new Food("Food" + i, r.nextInt(100), r.nextInt(50), r.nextInt(150),100));
            }
            Log.d(MHADatabase.class.getSimpleName(),"Deleting all The Rows for MEAL Table");
            mealDAO.deleteAll();
            Log.d(MHADatabase.class.getSimpleName(),"Populating the MEAL Table");
            mealDAO.insert(new Meal("Breakfast"));
            mealDAO.insert(new Meal("Lunch"));
            mealDAO.insert(new Meal("Dinner"));
            mealDAO.insert(new Meal("Desserts"));*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            onPopulationCompleteListener.onPopulationComplete();
        }
        public interface OnPopulationCompleteListener{
            void onPopulationComplete();
        }
    }
}

