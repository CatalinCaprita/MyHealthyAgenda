package com.example.myhealthyagenda.food;

import java.io.Serializable;

public class Macronutrient implements Serializable {
    private static final long serialVersionUID = 10000001l;
    private  double quantity;
    private double kcalPerGram;
    public static final int PROTEIN_KCAL = 4;
    public static final int CARB_KCAL = 4;
    public static final int FAT_KCAL = 9;
    public static final int PROTEIN  = 0;
    public static final int CARB = 1;
    public static final int FAT = 2;

    public Macronutrient(double quantityGrams,int type)
    {
        this.quantity = quantityGrams;
        switch (type){
            case PROTEIN: {kcalPerGram = PROTEIN_KCAL;break;}
            case FAT: {kcalPerGram = FAT_KCAL;break;}
            case CARB: {kcalPerGram = CARB_KCAL;break;}
            default: kcalPerGram = 4;
        }
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantityGrams) {
        this.quantity = quantityGrams;
    }

    public void addQuantity(double extraQuantityGrams){
        this.quantity += extraQuantityGrams;
    }

    public void substractQuantity(double toSubstractGrams){
        this.quantity -= toSubstractGrams;
    }
    public double getKcal(){
        return quantity * kcalPerGram;
    }
    @Override
    public String toString(){
        return Double.toString(this.quantity);
    }
}
