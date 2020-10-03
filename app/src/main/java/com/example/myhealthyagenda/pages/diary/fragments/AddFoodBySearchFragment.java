package com.example.myhealthyagenda.pages.diary.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.FoodAdapter;
import com.example.myhealthyagenda.food.Meal;
import com.example.myhealthyagenda.food.MealFoodJoin;
import com.example.myhealthyagenda.food.MealFoodJoinWithFood;
import com.example.myhealthyagenda.food.MealWithFoods;
import com.example.myhealthyagenda.viewmodel.FoodViewModel;
import com.example.myhealthyagenda.viewmodel.MealFoodJoinViewModel;
import com.example.myhealthyagenda.viewmodel.MealViewModel;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddFoodBySearchFragment extends Fragment {

    private static final String TAG  = "AddFoodBySearchFragment::";
    private View mainView;
    private RecyclerView rvFoods;
    private FoodAdapter foodAdapter;
    private SearchView searchView;
    private Activity context;
    private FoodViewModel foodViewModel;
    private MealViewModel mealViewModel;
    private MealFoodJoinViewModel mealFoodJoinViewModel;
    public AddFoodBySearchFragment() {

    }

    public static AddFoodBySearchFragment newInstance(){
        Bundle args = new Bundle();
        AddFoodBySearchFragment result = new AddFoodBySearchFragment();
        result.setArguments(args);
        return  result;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_add_food_by_search,container,false);
         return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Add Food");
        Log.d(TAG+"onActivityCreated","Assigning FoodViewModel");

        foodViewModel = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(FoodViewModel.class);

        Log.d(TAG+"onActivityCreated","Setting RecyclerView");
        rvFoods = mainView.findViewById(R.id.rvFoods);
        rvFoods.setLayoutManager(new LinearLayoutManager(getActivity()));

        mealViewModel = new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
        .get(MealViewModel.class);


        foodAdapter = new FoodAdapter(getActivity(),mealViewModel.getMealToAddFood().getValue().getMealId(),true);
        rvFoods.setAdapter(foodAdapter);

        mealFoodJoinViewModel = new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(MealFoodJoinViewModel.class);


        searchView = mainView.findViewById(R.id.searchView);

        foodViewModel.getAllFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                if(foods != null) {
                    final List<MealFoodJoinWithFood> result = foods.stream()
                            .map( food -> {
                                MealFoodJoinWithFood mfjf = new  MealFoodJoinWithFood();
                                mfjf.setMealFoodJoin(new MealFoodJoin(0,0,100));
                                mfjf.setFoods(Arrays.asList(food));
                                return mfjf;
                            }).collect(Collectors.toList());
                    foodAdapter.submitList(result);
                }
            }
        });
    }
}
