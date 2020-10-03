package com.example.myhealthyagenda;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhealthyagenda.article.Article;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.fragments.BalanceFragment;
import com.example.myhealthyagenda.pages.home.UserMainActivity;
import com.example.myhealthyagenda.pages.login.LogActivity;
import com.example.myhealthyagenda.user.User;
import com.example.myhealthyagenda.user.UserNotFoundException;
import com.example.myhealthyagenda.user.util.UserFileException;
import com.example.myhealthyagenda.user.util.UserFileManager;
import com.example.myhealthyagenda.util.Serializer;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ApplicationClass extends Application {
    public static Map<String,String> userDB = new HashMap<String,String>();
    public static ArrayList<Article> articles = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static Map<Integer,Food>foods = new HashMap<>();
    public static User loggedUser;
    private static final String TAG = "ApplicationClass::";
    private static final Random r  = new Random(System.currentTimeMillis());
    @Override
    public void onCreate(){
        super.onCreate();
        for(int i=0 ; i < 200; i++){
            userDB.put("user" + i, "pass" + i);
        }
        articles.add(new Article("RD's approve of this new way to do it!",
                "15 Vegan Dishes With Up to 21 Grams of Protein",
                "https://blog.myfitnesspal.com/wp-content/uploads/2019/11/Vegan-Mushroom-Shepherd%E2%80%99s-Pie-1140x545.jpg",
                "https://blog.myfitnesspal.com/nature-filled-urban-walking-trails-in-the-u-s/" ));
        articles.add(new Article("RD's approve of this new way to do it!",
               "4 Ways to Beat Post-Workout Muscle Soreness",
                "https://edge.curalate.com/v1/img/0Fc_nye3Ec9iWnC4XccqP4FIzbTHrbt4UKz9xBajuRM=/d/l",
                "https://blog.myfitnesspal.com/dinner-worthy-salads-under-455-calories/" ));
        articles.add(new Article("RD's approve of this new way to do it!",
                "Should You Choose a Long Walk Over a Short Run?",
                "https://blog.myfitnesspal.com/wp-content/uploads/2020/08/Should-You-Choose-a-Long-Walk-or-a-Short-Run_-1024x643.jpg",
                "https://blog.myfitnesspal.com/why-stressing-about-food-and-weight-gain-can-backfire/" ));
        articles.add(new Article("RD's approve of this new way to do it!",
                "5 Bedtime Habits to Help You Sleep Better",
                "https://blog.myfitnesspal.com/wp-content/uploads/2020/08/5-Bedtime-Habits-to-Help-You-Sleep-Better-1024x643.jpg",
                "https://blog.myfitnesspal.com/nature-filled-urban-walking-trails-in-the-u-s/" ));
        articles.add(new Article("RD's approve of this new way to do it!",
                "Quick Skillet Peach Cobbler",
                "https://blog.myfitnesspal.com/wp-content/uploads/2020/08/Quick-Skillet-Peach-Cobbler-1024x643.jpg",
                "https://blog.myfitnesspal.com/dinner-worthy-salads-under-455-calories/" ));
        articles.add(new Article("RD's approve of this new way to do it!",
                "Why Spicing Up Your Meals is Healthy, According to Science\n",
                "https://blog.myfitnesspal.com/wp-content/uploads/2020/08/Why-Spicing-Up-Your-Meals-is-Healthy-According-to-Science-1024x643.jpg",
                "https://blog.myfitnesspal.com/why-stressing-about-food-and-weight-gain-can-backfire/" ));
        for(int i=0 ; i < 10; i++){
            User u = new User("User" + i,-1);
            u.setTotalKcal(r.nextInt(3000));
            u.setActualKcal(r.nextInt(1300));
            users.add(u);
        }
        for(int i=0; i< 10; i++) {
            Food food = new Food("Food" + i, r.nextInt(100), r.nextInt(100), r.nextInt(100), r.nextInt(100));
            foods.put(food.ID,food);
        }

    }

    private String assignRandomTitle(){
        switch(new Random().nextInt(3)){
            case 0:
                return "Why Stressing About Food and Weight Gain Can Backfire";
            case 1:
                return "10 Nature-Filled Urban Walking Trails in the U.S.0";
            case 2:
                return "20 Dinner-Worthy Salads Under 455 Calories";
        }
        return "title";
    }

    public static void showErrorToast(Context context, ViewGroup parent, String message){
            View errorView = LayoutInflater.from(context).inflate(R.layout.toast_error, parent,false);
        ((TextView)errorView.findViewById(R.id.tvToastErrorMsg)).setText(message);
        Toast toast = new  Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(errorView);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL,5,-10);
        toast.show();
    }

    public static void logUser(Context context, int userID) throws UserNotFoundException,IOException,UserFileException{
        if(loggedUser == null){
                if(userID > users.size())
                    throw new UserNotFoundException();
                Log.i(TAG+"logUser","Looking for the logged User");
                loggedUser = users.get(userID);
                UserFileManager.getInstance().intiUserFiles(context,ApplicationClass.getLoggedUser());
                loggedUser.addNFriends(new Random().nextInt(users.size()- 1));
                loggedUser.setActualKcal(0);
            }

    }
    public static User getLoggedUser(){
            return loggedUser;
    }
    public static void setLoggedUser(User user){
        loggedUser = user;
    }
    public static Food getFood(int foodID){
        return foods.get(foodID);
    }
    public static void addFood(Food food){
        foods.put(food.ID,food);
    }
}
