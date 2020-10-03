package com.example.myhealthyagenda.pages.diary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.BaseMenuActivity;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.FoodAdapter;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealAdapter;
import com.example.myhealthyagenda.food.MealFoodJoin;
import com.example.myhealthyagenda.food.MealFoodJoinWithFood;
import com.example.myhealthyagenda.food.MealWithFoods;
import com.example.myhealthyagenda.fragments.BalanceFragment;
import com.example.myhealthyagenda.pages.diary.fragments.AddFoodBySearchFragment;
import com.example.myhealthyagenda.pages.diary.fragments.AddFoodDialogFragment;
import com.example.myhealthyagenda.pages.diary.fragments.FoodDetailFragment;
import com.example.myhealthyagenda.util.Serializer;
import com.example.myhealthyagenda.viewmodel.FoodViewModel;
import com.example.myhealthyagenda.viewmodel.MealFoodJoinViewModel;
import com.example.myhealthyagenda.viewmodel.MealViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiaryActivity extends BaseMenuActivity implements com.example.myhealthyagenda.menu.MenuFragment.onItemClickedListener,
        FoodAdapter.OnFoodClickListener,
        FoodDetailFragment.OnUserBalanceUpdateListener,
        MealAdapter.OnAddFoodFragmentInitListener,
        MealAdapter.OnFoodItemSwipedListener{

   //UI Logic
    BalanceFragment fragBalance;
    FrameLayout frameBalance,frameFoodDetail;
    RecyclerView rvMeals;
    ActionBar actionBar;
    FragmentManager manager;

    //Constant Values
    public static final String FRAGMENT_BALANCE = "FRAG_BALANCE";
    public static final String FRAGMENT_FOOD_DETAIL = "FRAG_FOOD_DETAIL";
    public static final String FRAGMENT_ADD_FOOD = "FRAG_FOOD_ADD";
    private static final String TAG = "DiaryActivity::";
    public static final int MENU_EDIT  = 0;
    public static final int MENU_NUTRITION = 1;
    public static final int MENU_ADD_FOOD = 2;

    //Persistence Logic
    private FoodViewModel foodViewModel;
    private MealViewModel mealViewModel;
    private MealFoodJoinViewModel mealFoodJoinViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Random r = new Random(System.currentTimeMillis());
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar_diary));
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.title_diary);
        actionBar.show();
        frameFoodDetail = findViewById(R.id.frame_food_detail_frag);

        foodViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(FoodViewModel.class);
        foodViewModel.getMealAddedClickedFood().observe(this, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                if (food != null) {

                    Log.d(TAG + "::onClickedFoodChanged", "Food from meal to display is NOT NULL!");
                    manager.beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right,
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right
                            )
                            .replace(R.id.frame_food_detail_frag, FoodDetailFragment.newInstance(food.getFoodId(),
                                    mealViewModel.getMealToEditFoodIdValue(),
                                    false),
                                    DiaryActivity.FRAGMENT_FOOD_DETAIL)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        foodViewModel.getAddableClickedFood().observe(this, new Observer<Food>(){
            @Override
            public void onChanged(Food food) {
                if(food != null){
                    Log.d(TAG + "::onAddableFoodChanged","Food from Addable List is NOT NULL!");
                    manager.beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right,
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right
                            )
                            .replace(R.id.frame_food_detail_frag,FoodDetailFragment.newInstance(food.getFoodId(),
                                    mealViewModel.getMealToAddFoodIdValue(),
                                    true),
                                    DiaryActivity.FRAGMENT_FOOD_DETAIL)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.frame_balance,BalanceFragment.newInstance(user.getTotalKcal(),
                        user.getActualKcal()),BalanceFragment.TAG_FRAG_BALANCE)
                .commit();
        /*
            Meals not Serialized yet, We have to create empty meals list
        */
        mealViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(MealViewModel.class);

        rvMeals = findViewById(R.id.rvMeals);
        rvMeals.setLayoutManager(new LinearLayoutManager(this));
        rvMeals.setAdapter(new MealAdapter(this));
        mealViewModel.getMealToAddFood().observe(this,new Observer<Meal>(){
            @Override
            public void onChanged(Meal meal) {
                if(meal != null){
                    Log.d(TAG + "onMealToAddFoodUpdated","MEAL to add food to is " + meal.getMealId());
                    manager.beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right,
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right
                            )
                            .replace(R.id.frame_food_detail_frag, AddFoodBySearchFragment.newInstance(),DiaryActivity.FRAGMENT_FOOD_DETAIL)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        mealFoodJoinViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(MealFoodJoinViewModel.class);
        mealViewModel.getAllMeals().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                if (meals != null) {
                    Log.d(TAG + "onMealsLoaded","One-Time-Only subscription ended.");
                    ((MealAdapter) rvMeals.getAdapter()).setDataSet(meals);
                    mealViewModel.getAllMeals().removeObserver(this);
                    for (Meal meal : meals) {
                        mealFoodJoinViewModel.getMealFoodJoinWithFoodByMealId(meal.getMealId()).observe(DiaryActivity.this,
                                new Observer<List<MealFoodJoinWithFood>>() {
                                    @Override
                                    public void onChanged(List<MealFoodJoinWithFood> mealFoodJoinWithFoods) {
                                        if (mealFoodJoinWithFoods != null) {
                                            Log.d(TAG + "mealFoodJoinWithFoods List Changed", "Updating Foods For MEAL_ID" + meal.getMealId());
                                            ((MealAdapter) rvMeals.getAdapter()).updateDataSetByMealId(meal.getMealId(), mealFoodJoinWithFoods);
                                        }
                                    }
                                });
                    }
                }
            }
        });



    }

    @Override
    public void loadMenuRes() {
        rootLayout = findViewById(R.id.layout_root);
        frameMenu = findViewById(R.id.frame_menu);
    }

    @Override
    public int getRootWidth() {
        return 3 * rootLayout.getWidth() / 4;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        android.view.MenuItem item = menu.add(Menu.NONE,DiaryActivity.MENU_EDIT,Menu.NONE,getString(R.string.label_menu_diary_edit));
        item.setIcon(R.drawable.edit);
        item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);

         item = menu.add(Menu.NONE,DiaryActivity.MENU_NUTRITION,Menu.NONE,getString(R.string.label_menu_diary_nutrition));
        item.setIcon(R.drawable.nutrition);
        item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item){
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onFoodClick(Food food,int mealId, boolean areFoodsAddable) {
        Log.d(TAG + " onFoodClick",food.getFoodId()+ " on the mealId: " + mealId );
        if(areFoodsAddable)
            /*
                Adding a food is guaranteed to initialize MealToAddFood in MealViewModel.
                Flow is: MealAdapter -> addFoodOnClick -> DiaryActivity::onInitAddFragment -> setMealToAddFood
             */
            foodViewModel.setAddableFoodId(food.getFoodId());
        else {
            if(mealViewModel.getMealToEditFood().getValue() == null)
                mealViewModel.setMealToEditFoodId(mealId);
            foodViewModel.setAddedFoodId(food.getFoodId());
        }

    }
    @Override
    public void onAddFoodFragmentInit(int mealId) {
        Log.d(TAG + "::onAddFoodFragmentInit"," Opening Fragment for meal " + mealId);
        mealViewModel.setMealToAddFoodId(mealId);
    }

    @Override
    public void onBalanceUpdate(double oldKcal, double newKcal) {
        user.increaseActualKcal((int)(newKcal - oldKcal));
        fragBalance = (BalanceFragment)manager.findFragmentByTag(BalanceFragment.TAG_FRAG_BALANCE);
        fragBalance.addCalories((int)(newKcal - oldKcal));
    }

    @Override
    public void onFoodItemSwiped(Food toRemove,int mealId) {
        Log.d(TAG + "onFoodItemSwiped","Caught Event For the Food Item Swiped");
        user.increaseActualKcal(-(int)toRemove.getTotalKCal());
        fragBalance = (BalanceFragment)manager.findFragmentByTag(BalanceFragment.TAG_FRAG_BALANCE);
        fragBalance.setCalories(user.getActualKcal());
        ((MealAdapter) rvMeals.getAdapter()).removeFood(toRemove,mealId);
        mealFoodJoinViewModel.deleteFoodFromMealByFoodId(mealId,toRemove.getFoodId());
    }
}