package com.example.myhealthyagenda.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myhealthyagenda.R;

public class BalanceFragment extends Fragment {

    TextView tvMath;
    ProgressBar pbCals;
    int totalCals,actualCals;
    View mainView;
    public static final String ARG_TOTAL_CALS = "A1";
    public static final String ARG_ACTUAL_CALS = "A2";
    public static final String TAG_FRAG_BALANCE = "TAG_FRAG_BALANCE";
    public BalanceFragment() {
        // Required empty public constructor
    }

    public static BalanceFragment newInstance(int totalCals, int actualCals) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putInt(BalanceFragment.ARG_TOTAL_CALS,totalCals);
        args.putInt(BalanceFragment.ARG_ACTUAL_CALS,actualCals);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalCals = getArguments().getInt(BalanceFragment.ARG_TOTAL_CALS);
            actualCals = getArguments().getInt(BalanceFragment.ARG_ACTUAL_CALS);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_balance,container,false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvMath = mainView.findViewById(R.id.tvMath);
        tvMath.setText(getString(R.string.balance_math,totalCals,actualCals,totalCals - actualCals));
        pbCals = mainView.findViewById(R.id.pbCalories);
        pbCals.setMax(totalCals);
        pbCals.setProgress(actualCals);
    }

    public void addCalories(int KcalAdded){
        actualCals += KcalAdded;
        pbCals.setProgress(actualCals);
        tvMath.setText(getString(R.string.balance_math,totalCals,actualCals,totalCals - actualCals));
    }
    public void setCalories(int kcal){
        actualCals = kcal;
        pbCals.setProgress(actualCals);
        tvMath.setText(getString(R.string.balance_math,totalCals,actualCals,totalCals - actualCals));
    }
}