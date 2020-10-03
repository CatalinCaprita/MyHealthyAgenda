package com.example.myhealthyagenda.food;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.pages.diary.DiaryActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Map<Integer,Integer> foodIndexes;
    private  ArrayList<Food> foods;
    private OnFoodClickListener onFoodClickListener;
    private Activity context;
    private final int mealId;
    private boolean areFoodsAddable;
    private static final String TAG = "FoodAdapter::";

    private AsyncListDiffer<MealFoodJoinWithFood> backgroundDiff = new AsyncListDiffer<MealFoodJoinWithFood>(this,DIFF_CALLBACK);

    private static DiffUtil.ItemCallback<MealFoodJoinWithFood> DIFF_CALLBACK = new DiffUtil.ItemCallback<MealFoodJoinWithFood>() {
        @Override
        public boolean areItemsTheSame(@NonNull MealFoodJoinWithFood oldItem, @NonNull MealFoodJoinWithFood newItem) {
            return oldItem.getMealFoodJoin().equals(newItem.getMealFoodJoin());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MealFoodJoinWithFood oldItem, @NonNull MealFoodJoinWithFood newItem) {
            return oldItem.getFoods().get(0).equals(newItem.getFoods().get(0));
        }
    };


    public FoodAdapter(Context context, Map<Integer,Food> foods,int mealId ,boolean areFoodsAddable){
        this.foods = (foods != null ? new ArrayList<>(foods.values()) : new ArrayList<>());
        this.foodIndexes = new HashMap<>();
        for(int i=0 ; i < this.foods.size(); i++){
                foodIndexes.put(this.foods.get(i).ID,i);
        }
        this.mealId = mealId;
        this.areFoodsAddable = areFoodsAddable;
        try{
            this.onFoodClickListener = (OnFoodClickListener)context;
            this.context = (Activity)context;
        }catch(ClassCastException e){
            Log.e("ERROR",e.toString());
        }
    }

    public FoodAdapter(Context context,int mealId,boolean areFoodsAddable){
        this.foods = new ArrayList<>();
        this.foodIndexes = new HashMap<>();
        this.mealId = mealId;
        this.areFoodsAddable = areFoodsAddable;
        Log.d(TAG + "Constructor without List of Foods","MealId : " + this.mealId + " , areFoodsAddable: " + areFoodsAddable);
        try{
            this.onFoodClickListener = (OnFoodClickListener)context;
            this.context = (Activity)context;
        }catch(ClassCastException e){
            Log.e("ERROR",e.toString());
        }

    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new FoodAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.d(TAG + "onBindViewHolder","Binding a viewHolder to position " + position);
        Food food = backgroundDiff.getCurrentList().get(position).getFoods().get(0);
        holder.itemView.setTag(food);
        holder.tvTotalKcal.setText(String.format("%4.2f",food.getTotalKCal()));
        holder.tvSourceQuantity.setText(context.getString(R.string.label_food_source_quant,food.getSource(),food.getQuantityGrams()));
        holder.tvFoodName.setText(food.getName());

    }

    @Override
    public int getItemCount() {
        return backgroundDiff.getCurrentList().size();
    }

    public void addFood(Food food){
        this.foods.add(food);
        foodIndexes.put(food.ID,foods.size() - 1);
        notifyItemInserted(foods.size() - 1);
    }

    public void removeFood(Food toRemove){
        if(foodIndexes.get(toRemove.getFoodId()) == null){
            Log.d(TAG +"::removeFood","food not present in adapter list");
            return;
        }
        this.foods.remove(foodIndexes.get(toRemove.getFoodId()));
        notifyItemRemoved(foodIndexes.get(toRemove.getFoodId()));
    }
    public void submitList(List<MealFoodJoinWithFood> newList){
        Log.d(this.getClass().getSimpleName() + "::submitList","Submitting list to the AsyncListDiffer...");
        backgroundDiff.submitList(newList);
    }
    public void updateDataSet(List<Food> newFoods){
        this.foods.clear();
        this.foods.addAll(newFoods);
        this.foodIndexes = this.foodIndexes == null ? new HashMap<>() :  this.foodIndexes;
        for(int i=0 ; i < this.foods.size(); i++){
            foodIndexes.put(this.foods.get(i).getFoodId(),i);
        }
        notifyDataSetChanged();
        Log.d(TAG+"onUpdateDataSet","current MealId " + mealId);
    }

    public void updateSingleItem(int foodId){
        Log.d("FOODADAPTER","Updating the food Item quantity. Preying it workds");
        Log.d("FoodAdapter::updateSingleItem","After :" + foods.get(foodIndexes.get(foodId)));
        notifyItemChanged(foodIndexes.get(foodId));
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFoodName, tvSourceQuantity,tvTotalKcal;
        public ViewHolder(View itemView){
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvName);
            tvSourceQuantity = itemView.findViewById(R.id.tvProviderQuantiy);
            tvTotalKcal = itemView.findViewById(R.id.tvTotalKcal);

            itemView.setOnClickListener(view ->{
                try{
                    Log.d(TAG+"onClickListener","current MealId " + mealId);
                    onFoodClickListener.onFoodClick((Food)itemView.getTag(),mealId,areFoodsAddable);

                }catch(ClassCastException e){
                    e.printStackTrace();
                    Log.e("ERROR","COULD NOT CAST tag to Food item");
                }
            });
        }
    }

    public boolean isAreFoodsAddable() {
        return areFoodsAddable;
    }

    public void setAreFoodsAddable(boolean areFoodsAddable) {
        this.areFoodsAddable = areFoodsAddable;
    }

    public interface OnFoodClickListener{
        void onFoodClick(Food food, int mealId, boolean addable);
    }
}
