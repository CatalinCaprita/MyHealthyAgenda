package com.example.myhealthyagenda.food;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealFoodJoinWithFoodAdapter extends RecyclerView.Adapter<MealFoodJoinWithFoodAdapter.MealFoodJoinWithFoodViewHolder> {


    public MealFoodJoinWithFoodAdapter(Context context){

    }

    @NonNull
    @Override
    public MealFoodJoinWithFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MealFoodJoinWithFoodViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MealFoodJoinWithFoodViewHolder  extends RecyclerView.ViewHolder{

        public MealFoodJoinWithFoodViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
