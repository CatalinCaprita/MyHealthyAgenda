package com.example.myhealthyagenda.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myhealthyagenda.R;

public class LogOutDialogFramgent extends DialogFragment {

    private OnLogOutConfirmedListener mOnLogOutConfirmedListener;
    private Button btnConfirm, btnCancel;
    private View mainView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            this.mOnLogOutConfirmedListener = (OnLogOutConfirmedListener) context;
        }catch(ClassCastException e){
            Log.e(this.getClass().getSimpleName()+"::onAttach",e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mainView = inflater.inflate(R.layout.fragment_dialog_log_out,container,false);
        return mainView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnConfirm = mainView.findViewById(R.id.btnConfirm);
        btnCancel = mainView.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(view ->{
            mOnLogOutConfirmedListener.onLogOutConfirmed();
            this.getDialog().dismiss();
        });

        btnCancel.setOnClickListener(view->{
            this.getDialog().dismiss();
        });
    }

    public interface OnLogOutConfirmedListener{
        void onLogOutConfirmed();
    }
}

