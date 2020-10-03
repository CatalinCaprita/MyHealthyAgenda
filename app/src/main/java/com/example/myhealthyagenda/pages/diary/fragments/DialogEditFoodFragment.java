package com.example.myhealthyagenda.pages.diary.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myhealthyagenda.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogEditFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogEditFoodFragment extends DialogFragment {

    //View components
    private EditText etQuantity;
    private Button btnCancel,btnSave;
    private View mainView;
    public DialogEditFoodFragment() {
        // Required empty public constructor
    }

    public static DialogEditFoodFragment newInstance() {
        DialogEditFoodFragment fragment = new DialogEditFoodFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private onFoodServingEditListener mOnFoodServingEditListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnFoodServingEditListener = (onFoodServingEditListener) getTargetFragment();
        }catch(ClassCastException e){
            Log.e("TAG", "onAttach : Could not cast context to onFoodServingEditListener interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView =  inflater.inflate(R.layout.fragment_dialog_edit_food, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etQuantity = mainView.findViewById(R.id.ntQuantity);
        btnCancel = mainView.findViewById(R.id.btnCancel);
        btnSave = mainView.findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(view->{
            getDialog().dismiss();
        });

        btnSave.setOnClickListener(view->{
           double input = -1;
           try{
               input = Double.parseDouble(etQuantity.getText().toString());
           }catch(NumberFormatException e){
               Log.e("ERROR",e.getLocalizedMessage());
               return;
           }
           if(input >= 0 ){
               mOnFoodServingEditListener.onFoodServingsEdit(input);
               getDialog().dismiss();
           }
        });

    }


    public interface onFoodServingEditListener{
        void onFoodServingsEdit(double newQuantityGrams);
    }
}