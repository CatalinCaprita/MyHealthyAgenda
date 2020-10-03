package com.example.myhealthyagenda.food;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.util.Serializer;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "MEAL")
public class Meal  implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MEAL_ID")
    private int mealId;
    @ColumnInfo(name = "NAME")
    private String name;

    @Ignore
    private static final long serialVersionUID = 99999999912l;
    @Ignore
    private Map<Integer,Food> foods;
    @Ignore
    private double Kcal;
    @Ignore
    private static transient int count;
    @Ignore
    private final int ID = count++;
    @Ignore
    private final Macronutrient[] macros = new Macronutrient[3];
    static{
        count = 0;
    }
    public Meal(String name,ArrayList<Food> foods){
            this.foods = new  HashMap<>();
            for(Food food : foods){
                this.foods.put(food.ID,food);
            }
            this.name = name;
        macros[Macronutrient.CARB] = new Macronutrient(0,Macronutrient.CARB);
        macros[Macronutrient.FAT] = new Macronutrient(0,Macronutrient.FAT);
        macros[Macronutrient.PROTEIN] = new Macronutrient(0,Macronutrient.PROTEIN);
            Kcal = 0;
            foods.stream().
                    forEach(food ->{
                        Kcal += food.getTotalKCal();
                        macros[Macronutrient.CARB].addQuantity(food.getMacro(Macronutrient.CARB));
                        macros[Macronutrient.FAT].addQuantity(food.getMacro(Macronutrient.FAT));
                        macros[Macronutrient.PROTEIN].addQuantity(food.getMacro(Macronutrient.PROTEIN));

                    });
    }
    public Meal(String name){
        foods = new HashMap<>();
        Kcal = 0;
        macros[Macronutrient.CARB] = new Macronutrient(0,Macronutrient.CARB);
        macros[Macronutrient.FAT] = new Macronutrient(0,Macronutrient.FAT);
        macros[Macronutrient.PROTEIN] = new Macronutrient(0,Macronutrient.PROTEIN);
        this.name = name;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public void addFood(Food food) throws IndexOutOfBoundsException{
        foods.put(food.getFoodId(),food);
        Kcal += food.getTotalKCal();
        try {
            macros[Macronutrient.CARB].addQuantity(food.getMacro(Macronutrient.CARB));
            macros[Macronutrient.PROTEIN].addQuantity(food.getMacro(Macronutrient.PROTEIN));
            macros[Macronutrient.FAT].addQuantity(food.getMacro(Macronutrient.FAT));
        }catch (IndexOutOfBoundsException e){
            throw  new IndexOutOfBoundsException();
        }
    }

    public void updateFood(int foodId, double newQuantityGrams){

        Food toRemove = foods.get(foodId);
        Log.d("Meal::updateFood","toRemove refference"+ toRemove);
        Kcal -= toRemove.getTotalKCal();
        macros[Macronutrient.CARB].substractQuantity(toRemove.getMacro(Macronutrient.CARB));
        macros[Macronutrient.PROTEIN].substractQuantity(toRemove.getMacro(Macronutrient.PROTEIN));
        macros[Macronutrient.FAT].substractQuantity(toRemove.getMacro(Macronutrient.FAT));
        toRemove.setQuantityGrams(newQuantityGrams);
        try {
            Kcal += toRemove.getTotalKCal();
            macros[Macronutrient.CARB].addQuantity(toRemove.getMacro(Macronutrient.CARB));
            macros[Macronutrient.PROTEIN].addQuantity(toRemove.getMacro(Macronutrient.PROTEIN));
            macros[Macronutrient.FAT].addQuantity(toRemove.getMacro(Macronutrient.FAT));
        }catch (IndexOutOfBoundsException e){
            throw  new IndexOutOfBoundsException();
        }
    }

    public void removeFood(Food food){
        Kcal -= food.getTotalKCal();
        try {
            macros[Macronutrient.CARB].substractQuantity(food.getMacro(Macronutrient.CARB));
            macros[Macronutrient.PROTEIN].substractQuantity(food.getMacro(Macronutrient.PROTEIN));
            macros[Macronutrient.FAT].substractQuantity(food.getMacro(Macronutrient.FAT));
        }catch (IndexOutOfBoundsException e){
            throw  new IndexOutOfBoundsException();
        }
    }
    public void updateFoods(List<Food> newFoods){
        for(Food food : newFoods){
            if(foods.get(food.getFoodId()) == null){
                addFood(food);
            }else{
                Log.d(Meal.class.getSimpleName()+ "::updateFoods","Food already present in meal.Updating Food quantity...");
                updateFood(food.getFoodId(),food.getQuantityGrams());
            }
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKcal() {
        return Kcal;
    }

    public Map<Integer,Food> getFoods() {
        return this.foods;
    }

    public void setFoods(ArrayList<Food> foods) {

    }

    @Override
    public String toString() {
        return "Meal{" +
                "mealId=" + mealId +
                ", name='" + name + '\'' +
                '}';
    }
}
