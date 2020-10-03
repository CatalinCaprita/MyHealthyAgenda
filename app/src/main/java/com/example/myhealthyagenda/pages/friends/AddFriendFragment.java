package com.example.myhealthyagenda.pages.friends;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.R;


public class AddFriendFragment extends Fragment {

    EditText etMail,etFb,etAlias;
    Button btnSend;
    View mainView;
    onFriendAddListener context;

    public static AddFriendFragment newInstance() {
        AddFriendFragment fragment = new AddFriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            this.context = (onFriendAddListener)context;
        }catch(ClassCastException e){
            Log.d("ERR",context.getClass().getName() + " must implement onFriendAddListener interface! ");
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
        mainView =  inflater.inflate(R.layout.fragment_add_friend, container, false);
        return mainView;
    }
    @Override
    public void onActivityCreated(Bundle onInstanceSavedState){
        super.onActivityCreated(onInstanceSavedState);
        etMail = mainView.findViewById(R.id.etMail);
        etFb = mainView.findViewById(R.id.etFb);
        etAlias = mainView.findViewById(R.id.etAlias);
        btnSend = mainView.findViewById(R.id.btnFriendSend);

        btnSend.setOnClickListener( (view) -> {
            String mail = etMail.getText().toString();
            String fb = etFb.getText().toString();
            String as = etAlias.getText().toString();

            if(!mail.isEmpty() && !fb.isEmpty()){
                as = as.isEmpty() ? fb : as;
                context.onRequestSent(fb,as);
                /*TO DO
                SET INTENT TO THE USER DB SERVICE SO A USER IS NOTIFIED WHEN RECEIVING A REQUEST.
                 */
            }else{
                ApplicationClass.showErrorToast(getActivity(),(ViewGroup)mainView.findViewById(R.id.lay_add_friend),getString(R.string.friends_err_info_missing));
            }
        });


    }
    public interface onFriendAddListener{
        void onRequestSent(String username, String alias);
    }

}