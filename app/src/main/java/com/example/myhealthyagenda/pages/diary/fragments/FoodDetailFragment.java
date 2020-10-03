package com.example.myhealthyagenda.pages.diary.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.BaseMenuActivity;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.food.Food;
import com.example.myhealthyagenda.food.Macronutrient;
import com.example.myhealthyagenda.food.MealFoodJoin;
import com.example.myhealthyagenda.pages.diary.DiaryActivity;
import com.example.myhealthyagenda.repositories.MealFoodJoinRepository;
import com.example.myhealthyagenda.viewmodel.FoodViewModel;
import com.example.myhealthyagenda.viewmodel.MealFoodJoinViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDetailFragment extends Fragment  implements DialogEditFoodFragment.onFoodServingEditListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FOOD_ID = "param1";
    private static final String MEAL_ID = "param2";
    private static final String IS_ADDABLE = "param3";
    private int foodID;
    private int mealId;
    private Food boundFood;
    private boolean isAddable;

    //UI components
    private View mainView;
    private Activity host;
    private TextView tvFoodName,tvEditServings;
    private ProgressBar pbFromTotal;
    PieChart pieChart;
    private boolean isInstantiated;
    private ArrayList<String> names;
    private ArrayList<PieEntry>pieEntries;
    private static final int[] colors = {Color.BLUE,Color.GREEN,Color.RED};
    public static String [] namesMacro;
    private ActionBar actionBar;

    //Persistence of Data members
    private FoodViewModel foodViewModel;
    private MealFoodJoinViewModel mealFoodJoinViewModel;
    public static final int REQ_DIALOG_Q = 1;
    private OnFoodEditedListener onFoodEditedListener;
    private OnUserBalanceUpdateListener onUserBalanceUpdateListener;
    public FoodDetailFragment() {
        // Required empty public constructor
    }

    public static FoodDetailFragment newInstance(int foodID, int mealId, boolean isAddable) {
        FoodDetailFragment fragment = new FoodDetailFragment();
        Bundle args = new Bundle();
        args.putInt(FOOD_ID, foodID);
        args.putInt(MEAL_ID,mealId);
        args.putBoolean(IS_ADDABLE,isAddable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.host = (Activity)context;
        try{
           // this.onFoodEditedListener = (FoodDetailFragment.OnFoodEditedListener)context;
            this.onUserBalanceUpdateListener = (FoodDetailFragment.OnUserBalanceUpdateListener)context;
        }catch(ClassCastException e){
            Log.e("ESCAPE",e.getLocalizedMessage());
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(FoodViewModel.class);
        mealFoodJoinViewModel = new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(MealFoodJoinViewModel.class);

        if (getArguments() != null) {
            foodID = getArguments().getInt(FoodDetailFragment.FOOD_ID);
            isAddable = getArguments().getBoolean(FoodDetailFragment.IS_ADDABLE,false);
            boundFood = isAddable ? foodViewModel.getAddableClickedFood().getValue() : foodViewModel.getMealAddedClickedFood().getValue();
            Log.d(FoodDetailFragment.class.getSimpleName(),"Bound Food: " + boundFood);
            mealId = getArguments().getInt(FoodDetailFragment.MEAL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView =  inflater.inflate(R.layout.fragment_food_detail, container, false);
        return mainView;
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        namesMacro = getActivity().getResources().getStringArray(R.array.macroNames);
        pieChart = (PieChart)mainView.findViewById(R.id.pie_chart_macros);
        pieEntries = new ArrayList<>(3);
        for(int i=0 ; i< 3; i++){
            pieEntries.add(new PieEntry(0f));
        }
        setHasOptionsMenu(true);
        onBoundFoodLoaded();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(DiaryActivity.MENU_EDIT).setVisible(false);
        menu.findItem(DiaryActivity.MENU_NUTRITION).setVisible(false);
        menu.findItem(R.id.menu_main).setVisible(false);
        if(isAddable) {
            MenuItem item = menu.add(Menu.NONE, DiaryActivity.MENU_ADD_FOOD, Menu.FIRST, R.string.title_food_details_add);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            item.setIcon(R.drawable.ic_add_food);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case DiaryActivity.MENU_ADD_FOOD:{
                /*
                    Communicate Back to The BalanceDisplayer Activity that there has been an Update in Balance Calorie, i.e. food added
                */
                onUserBalanceUpdateListener.onBalanceUpdate(0,boundFood.getTotalKCal());
                Log.d(this.getClass().getSimpleName() + "::onOptionsItemSelected","Adding FOOD to MEAL ");
                MealFoodJoin association = new MealFoodJoin(this.mealId,this.boundFood.getFoodId(),this.boundFood.getQuantityGrams());
                mealFoodJoinViewModel.insertFoodIntoMeal(association);
                getActivity().getSupportFragmentManager().popBackStack();
            }
            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onFoodServingsEdit(double newQuantityGrams) {
        tvEditServings.setText(Double.toString(newQuantityGrams));
        /*
            Communicate Back to The BalanceDisplayer Activity that there has been an Update in Balance Calorie, i.e. food Servings edit
         */
        double oldKcal = boundFood.getTotalKCal();
        /*
            If The FOOD is not addable, then it must be already part of an existing MEAL, so UPDATE MEAL_FOOD_JOIN table
         */
        boundFood.setQuantityGrams(newQuantityGrams);
        if(!isAddable) {
            onUserBalanceUpdateListener.onBalanceUpdate(oldKcal,boundFood.getTotalKCal());
            MealFoodJoin association = new MealFoodJoin(this.mealId, boundFood.getFoodId(), newQuantityGrams);
            Log.d(this.getClass().getSimpleName() + "::onFoodServingsEdit", "Calling MealFoodJoinViewModel to update Food" + boundFood.getFoodId()+
                    "on Meal " + mealId);
            mealFoodJoinViewModel.insertFoodIntoMeal(association);
        }
        /*
            Propagate the changed food to the owner activity, so it is updated in the RecyclerView
         */
        setChartInfo();
        pieChart.getData().notifyDataChanged();
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();


    }

    private void onBoundFoodLoaded(){
            setChartInfo();
            tvFoodName = mainView.findViewById(R.id.tvFoodName);
            tvFoodName.setText(getString(R.string.info_food_name_producer,boundFood.getName(),boundFood.getSource()));
            tvEditServings = mainView.findViewById(R.id.tvEditServings);
            tvEditServings.setText(Double.toString(boundFood.getQuantityGrams()));

            tvEditServings.setOnClickListener(view->{
                DialogEditFoodFragment dialog = new DialogEditFoodFragment();
                dialog.setTargetFragment(FoodDetailFragment.this,FoodDetailFragment.REQ_DIALOG_Q);
                dialog.show(getFragmentManager(),"Dialog");
            });

            pbFromTotal = mainView.findViewById(R.id.pbFromTotal);
            pbFromTotal.setProgress((int)boundFood.getTotalKCal());
            pbFromTotal.setMax(2500);
    }
    private void setChartInfo(){
        if(!isInstantiated) {
            pieChart.setHoleRadius(80f);
            pieChart.setHighlightPerTapEnabled(true);
            pieChart.setMinimumHeight(mainView.getHeight() / 2);
            pieChart.setExtraBottomOffset(-50);
            Description desc = new Description();
            desc.setText(getString(R.string.desc_macro_content));
            pieChart.setDescription(desc);
            pieChart.setDrawEntryLabels(false);
            pieChart.setCenterTextSize(28);
            isInstantiated = true;
        }
        pieChart.setCenterText(String.format("%4.2f \n cal",boundFood.getTotalKCal()));

        pieEntries.get(Macronutrient.CARB).setY((float)boundFood.getMacro(Macronutrient.CARB));
        pieEntries.get(Macronutrient.PROTEIN).setY((float)boundFood.getMacro(Macronutrient.PROTEIN));
        pieEntries.get(Macronutrient.FAT).setY((float)boundFood.getMacro(Macronutrient.FAT));

        PieDataSet dataSet = new PieDataSet(pieEntries,getString(R.string.desc_macro_content));
        dataSet.setSliceSpace(2f);
        dataSet.setDrawValues(false);
        dataSet.setColors(Color.BLUE,Color.GREEN,Color.RED);
        dataSet.setValueTextColor(R.color.colorAccentFront);

        Legend pieLegend = pieChart.getLegend();
        pieLegend.setEnabled(true);
        pieLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        pieLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        pieLegend.setOrientation(Legend.LegendOrientation.VERTICAL);
        pieLegend.setForm(Legend.LegendForm.CIRCLE);

        pieLegend.setTextSize(18f);
        pieLegend.setStackSpace(20f);
        pieLegend.setFormToTextSpace(5f);
        pieLegend.setTextColor(R.color.colorAccent);
        //setting custom labels
        final String[] labels = {
                getString(R.string.info_food_detail_pct_carb,boundFood.getMacro(Macronutrient.CARB),boundFood.getMacroPercent(Macronutrient.CARB)),
                getString(R.string.info_food_detail_pct_protein,boundFood.getMacro(Macronutrient.PROTEIN),boundFood.getMacroPercent(Macronutrient.PROTEIN)),
                getString(R.string.info_food_detail_pct_fat,boundFood.getMacro(Macronutrient.FAT),boundFood.getMacroPercent(Macronutrient.FAT)),
        };
        List<LegendEntry> entries= new ArrayList<>();
        for(int i=0 ; i < colors.length; i++){
            entries.add(new LegendEntry(labels[i], Legend.LegendForm.CIRCLE,12f,12f,null,colors[i]));
        }
        pieLegend.setCustom(entries);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12);
        data.setValueTextColor(R.color.colorTextPrimary);
        pieChart.setData(data);

    }

    public interface OnFoodEditedListener{
        void onFoodEdited(int foodId,int mealId,double newQuantityGrams);
    }

    public interface OnUserBalanceUpdateListener{
        void onBalanceUpdate(double oldQuantityGrams, double newQuantityGrams );
    }

}