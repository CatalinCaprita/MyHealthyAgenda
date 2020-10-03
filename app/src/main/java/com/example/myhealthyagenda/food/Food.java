package com.example.myhealthyagenda.food;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@Entity(tableName = "FOOD")
public class Food implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "FOOD_ID")
    private int foodId;
    @ColumnInfo(name = "NAME")
    private String name;
    @ColumnInfo(name = "SOURCE")
    private String source;
    @ColumnInfo(name = "PROTEIN_GRAMS")
    private double protein;
    @ColumnInfo(name = "FAT_GRAMS")
    private double fats;
    @ColumnInfo(name = "CARBS_GRAMS")
    private double carbs;
    @ColumnInfo(name = "QUANTITY_GRAMS")
    private double quantityGrams;

    @Ignore
    private static transient int count;
    @Ignore
    public final int ID = count++;
    @Ignore
    private static final long serialVersionUID = 10000000l;
    @Ignore
    private final Macronutrient[] macros = new Macronutrient[3];
    static{
        count = 0 ;
    }
    public  Food(){
        quantityGrams = 100;
    }

    /**
     * Constructor for a type of Food
     * @param name the name of the food
     * @param carbsGrams the amount of carbohydrates per 100 grams of food
     * @param proteinsGrams the amount of proteins per 100 grams of food
     * @param fatsGrams the amount of fats per 100 grams of food
     * @param quantityGrams the amount of food introduced
     */
    public Food(String name,double carbsGrams,double proteinsGrams,double fatsGrams,double quantityGrams){
        macros[Macronutrient.CARB] = new Macronutrient(carbsGrams / 100 * quantityGrams,Macronutrient.CARB);
        macros[Macronutrient.FAT] = new Macronutrient(fatsGrams / 100 * quantityGrams,Macronutrient.FAT);
        macros[Macronutrient.PROTEIN] = new Macronutrient(proteinsGrams / 100 * quantityGrams,Macronutrient.PROTEIN);
        this.carbs = macros[Macronutrient.CARB].getQuantity();
        this.protein = macros[Macronutrient.PROTEIN].getQuantity();
        this.fats = macros[Macronutrient.FAT].getQuantity();
        this.quantityGrams = quantityGrams;
        this.name = name;

    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
        macros[Macronutrient.CARB] = new Macronutrient(carbs/ 100 * quantityGrams,Macronutrient.CARB);

    }

    public int getID() {
        return ID;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
        macros[Macronutrient.PROTEIN] = new Macronutrient(protein / 100 * quantityGrams,Macronutrient.PROTEIN);

    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
        macros[Macronutrient.FAT] = new Macronutrient(fats / 100 * quantityGrams,Macronutrient.FAT);
        //Log.d("Food::setFats","Quantity:" + macros[Macronutrient.FAT].getQuantity() + "value to pass : " + fats);
    }

    public void setFoodId(int id){
        this.foodId= id;
    }
    public int getFoodId() {
        return foodId;
    }
    public void setQuantityGrams(double quantityGrams) {
        for(int i=0 ; i < macros.length; i++){
            //Log.d("Food::setQuantity","Macro" + i +" : " + macros[i].getQuantity());
            macros[i].setQuantity(macros[i].getQuantity() * quantityGrams / this.quantityGrams);
            //Log.d("Food::setQuantity","Macro" + i +" : " + macros[i].getQuantity());
        }
        this.quantityGrams = quantityGrams;
    }
    public double getQuantityGrams() {
        return quantityGrams;
    }

    public double getTotalKCal(){
        double totalKCal = 0;
        for(int i=0 ; i < macros.length; i++)
            totalKCal += macros[i].getKcal();
        return  totalKCal;
    }

    public double getMacro(int type) throws IndexOutOfBoundsException{
        switch (type){
            case Macronutrient.CARB:{ return macros[Macronutrient.CARB].getQuantity();}
            case Macronutrient.PROTEIN:{ return macros[Macronutrient.PROTEIN].getQuantity();}
            case Macronutrient.FAT:{ return macros[Macronutrient.FAT].getQuantity();}
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public double getMacroPercent(int type) throws IndexOutOfBoundsException{
        double q;
        switch (type){
            case Macronutrient.CARB:{ q = macros[Macronutrient.CARB].getKcal() ; break;}
            case Macronutrient.PROTEIN:{ q =  macros[Macronutrient.PROTEIN].getKcal();  break;}
            case Macronutrient.FAT:{ q = macros[Macronutrient.FAT].getKcal();  break;}
            default:
                throw new IndexOutOfBoundsException();
        }
        return q / getTotalKCal()  * 100 ;
    }

    public ArrayList<Double> getMacros(){
        ArrayList<Double> res = new ArrayList<>();
        res.add(macros[Macronutrient.CARB].getQuantity());
        res.add(macros[Macronutrient.PROTEIN].getQuantity());
        res.add(macros[Macronutrient.FAT].getQuantity());
        return res;
    }
    @Override
    public String toString(){
        String format = String.format("%s : %4.2f Carbohydrates, %4.2f Proteins, %4.2f fats",
                this.name,
                this.macros[Macronutrient.CARB].getQuantity(),
                this.macros[Macronutrient.PROTEIN].getQuantity(),
                this.macros[Macronutrient.FAT].getQuantity());
        return format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return foodId == food.foodId &&
                Double.compare(food.protein, protein) == 0 &&
                Double.compare(food.fats, fats) == 0 &&
                Double.compare(food.carbs, carbs) == 0 &&
                name.equals(food.name) &&
                Objects.equals(source, food.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, name, source, protein, fats, carbs, quantityGrams);
    }
}
