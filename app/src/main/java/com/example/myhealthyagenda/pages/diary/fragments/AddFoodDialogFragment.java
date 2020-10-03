package com.example.myhealthyagenda.pages.diary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.food.Food;


public class AddFoodDialogFragment extends DialogFragment {

    private static final String MEAL_ID = "param1";
    private static final String TAG = "AddFoodDialogFragment::";
    private View mainView;
    private OnFoodAddedListener mOnFoodAdded;
    private int mealId;
    private EditText etName, etProvider;
    private EditText etnCarbs,etnProtein,etnFat,etnQuantity;
    private Button btnSave, btnCancel;
    public AddFoodDialogFragment(){

    }

    public static AddFoodDialogFragment newInstance(int mealId) {
        AddFoodDialogFragment fragment = new AddFoodDialogFragment();
        Bundle args = new Bundle();
        args.putInt(MEAL_ID,mealId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
                mOnFoodAdded = (OnFoodAddedListener)context;
        }catch(ClassCastException e ){
            Log.e(TAG + "onAttach()",e.toString());
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            mealId = getArguments().getInt(AddFoodDialogFragment.MEAL_ID);
        }catch (NullPointerException e ){
            Log.e(TAG + "onCreate",e.getMessage());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        mainView = inflater.inflate(R.layout.fragment_dialog_add_food,parent,false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etName = mainView.findViewById(R.id.etFoodName);
        etProvider = mainView.findViewById(R.id.etProvider);
        etnCarbs = mainView.findViewById(R.id.etnCarbs);
        etnProtein = mainView.findViewById(R.id.etnProtein);
        etnFat = mainView.findViewById(R.id.etnFat);
        etnQuantity = mainView.findViewById(R.id.etnQuantity);
        btnSave = mainView.findViewById(R.id.btnSave);
        btnCancel = mainView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(view->{
            String foodName = etName.getText().toString();
            if(foodName.isEmpty()){
                ApplicationClass.showErrorToast(AddFoodDialogFragment.this.getContext(),
                        (ViewGroup) mainView,getString(R.string.info_food_name_producer));
                return;
            }

            String foodProvider = etProvider.getText().toString();
            double carbsGrams = 0f,proteinGrams = 0f,fatGrams = 0f,quantity = 0f;
            try{
                carbsGrams = Double.parseDouble(etnCarbs.getText().toString());
                proteinGrams = Double.parseDouble(etnProtein.getText().toString());
                fatGrams = Double.parseDouble(etnFat.getText().toString());
                quantity = Double.parseDouble(etnQuantity.getText().toString());

                Food newFood = new Food(foodName,carbsGrams,proteinGrams,fatGrams,quantity);
                newFood.setSource(foodProvider.isEmpty() ? "(no source)" : foodProvider);
                mOnFoodAdded.onFoodAdded(newFood,mealId);
                getDialog().dismiss();
            }catch(NumberFormatException e){
                Log.e(TAG + " onClickListener btnSave",e.getMessage());
            }
        });

        btnCancel.setOnClickListener(view->{
            getDialog().dismiss();
        });
    }

    public interface  OnFoodAddedListener{
        void onFoodAdded(Food food, int mealId);
    }
}
