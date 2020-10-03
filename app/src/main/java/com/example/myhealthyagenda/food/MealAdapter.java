package com.example.myhealthyagenda.food;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthyagenda.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private ArrayList<Meal> meals;
    private Map<Integer,FoodAdapter> foodAdapters = new HashMap<>();
    private Map<Integer,Integer> mealIndexes = new HashMap<>();
    private Map<Integer,ItemTouchHelper> callbackMap = new HashMap<>();
    private Context context;
    private OnAddFoodFragmentInitListener mOnAddFoodFragmentInitListener;
    private OnFoodItemSwipedListener mOnFoodItemSwipedListener;

    public MealAdapter(Context context,ArrayList<Meal> meals){
        this.meals = meals;
        this.context = context;
        this.foodAdapters = new HashMap<>();
        try{
            this.mOnAddFoodFragmentInitListener = (OnAddFoodFragmentInitListener)context;
        }catch(ClassCastException e){
            Log.e("MealAdapter::<init>()","Could not cast toOnAddFoodFragmentInitListener!");
        }
    }

    public MealAdapter(Context context){
        this.meals = new ArrayList<>();
        this.context = context;
        this.foodAdapters = new HashMap<>();
        try{
            this.mOnAddFoodFragmentInitListener = (OnAddFoodFragmentInitListener)context;
            this.mOnFoodItemSwipedListener = (OnFoodItemSwipedListener)context;
        }catch(ClassCastException e){
            Log.e("MealAdapter::<init>()","Could not cast toOnAddFoodFragmentInitListener!");
        }
    }
    @Override
    public MealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal,parent,false);
        return new MealAdapter.ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MealAdapter.ViewHolder holder, int position){
        holder.itemView.setTag(meals.get(position).getMealId());
        holder.tvTitle.setText(meals.get(position).getName());
        holder.tvTotalKcal.setText(String.format("%4.2f",meals.get(position).getKcal()));
        if(foodAdapters.get(meals.get(position).getMealId()) == null){
            Log.d(MealAdapter.class.getSimpleName(),"Adding a new Food adapter to the position: " + position);
            FoodAdapter mFoodAdapter = new FoodAdapter(context, meals.get(position).getFoods(),meals.get(position).getMealId(),false);
            foodAdapters.put(meals.get(position).getMealId(),mFoodAdapter);
            holder.rvFoods.setAdapter(mFoodAdapter);
            Log.d(MealAdapter.class.getSimpleName(),"Setting a new ItemTouchHelper to: " + position);
            callbackMap.put(meals.get(position).getMealId(),new ItemTouchHelper( new SwipeToDeleteFoodCallback(0,
                    ItemTouchHelper.LEFT,
                    this.mOnFoodItemSwipedListener,
                    meals.get(position).getMealId())));

        }else{
            Log.d(MealAdapter.class.getSimpleName(),"Setting The existing Food Adapter for Position: " + position);
            holder.rvFoods.setAdapter(foodAdapters.get(meals.get(position).getMealId()));
            callbackMap.get(meals.get(position).getMealId()).attachToRecyclerView(holder.rvFoods);
        }

    }
    @Override
    public int getItemCount(){
        return meals.size();
    }

    public void removeFood(Food toRemove, int mealId){
        Log.d("MealAdapter::updateFood","Delegating a FoodAdapter to remove a food internally. MealID: " + mealId);
        foodAdapters.get(mealId).removeFood(toRemove);
        this.meals.get(mealIndexes.get(mealId)).removeFood(toRemove);
        notifyItemChanged(mealIndexes.get(mealId));
    }
    public void setDataSet(List<Meal> meals){
        this.meals.clear();
        this.meals.addAll(meals);
        for(int i=0 ; i< meals.size(); i++){
            mealIndexes.put(this.meals.get(i).getMealId(),i);
        }
        notifyDataSetChanged();
    }

    public void updateDataSetByMealId(Integer mealId, List<MealFoodJoinWithFood> mealFoodJoinWithFoods){
        final List<Food> changedFoods = mealFoodJoinWithFoods.stream().flatMap(mfjf -> mfjf.getFoods().stream())
                .collect(Collectors.toList());

        Log.d(MealAdapter.class.getSimpleName()+ " updatDatasetByMealId"," Delegating The FoodAdapter To update Its Dataset");
        if(foodAdapters.get(mealId) == null){
            Log.d(MealAdapter.class.getSimpleName() + "updateDataSetByMealId","creating A New FoodAdapter to MEAL: " + meals.get(mealIndexes.get(mealId)));
            FoodAdapter foodAdapter = new FoodAdapter(context,mealId,false);
            foodAdapters.put(mealId,foodAdapter);
        }
        foodAdapters.get(mealId).submitList(mealFoodJoinWithFoods);
        Log.d(MealAdapter.class.getSimpleName() + "::updateDataSetByMealId"," Adapter Updated, Updating Foods For Meal");
        meals.get(mealIndexes.get(mealId)).updateFoods(changedFoods);
        notifyItemChanged(mealIndexes.get(mealId));

    }


    //-----Classes And Interfaces
    public interface OnAddFoodFragmentInitListener{
        void onAddFoodFragmentInit(int mealId);
    }

    public interface OnFoodItemSwipedListener{
        void onFoodItemSwiped(Food toRemove,int mealId);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvTotalKcal;
        LinearLayout addFood;
        RecyclerView rvFoods;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTotalKcal = itemView.findViewById(R.id.tvTotalKcal);
            addFood = itemView.findViewById(R.id.linearAddFood);
            rvFoods = itemView.findViewById(R.id.rvFoods);
            rvFoods.setLayoutManager(new LinearLayoutManager(context));
            addFood.setOnClickListener(view->{
                   mOnAddFoodFragmentInitListener.onAddFoodFragmentInit((Integer)itemView.getTag());
            });
        }

    }

    public class SwipeToDeleteFoodCallback extends ItemTouchHelper.SimpleCallback{

        /**
         * Creates a Callback for the given drag and swipe allowance. These values serve as
         * defaults
         * and if you want to customize behavior per ViewHolder, you can override
         * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
         * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
         *
         * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
         *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
         *                  #END},
         *                  {@link #UP} and {@link #DOWN}.
         */
        private OnFoodItemSwipedListener onFoodItemSwipedListener;
        private int mealId;

        public SwipeToDeleteFoodCallback(int dragDirs, int swipeDirs,
                                         OnFoodItemSwipedListener onFoodItemSwipedListener,
                                         int mealId) {
            super(dragDirs, swipeDirs);
            this.onFoodItemSwipedListener = onFoodItemSwipedListener;
            this.mealId = mealId;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d(this.getClass().getSimpleName()+ "::onSwiped","Dispatching Callback to the swipe listener");

                onFoodItemSwipedListener.onFoodItemSwiped((Food)viewHolder.itemView.getTag(),this.mealId);
        }
    }
}
