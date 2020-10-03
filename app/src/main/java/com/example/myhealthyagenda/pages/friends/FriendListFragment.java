package com.example.myhealthyagenda.pages.friends;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myhealthyagenda.ApplicationClass;
import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.user.User;

public class FriendListFragment extends Fragment {

    RecyclerView rvFriendList;
    View mainView;
    Activity host;
    public FriendListFragment() {
    }

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        host = (Activity)context;
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
        mainView =  inflater.inflate(R.layout.fragment_friend_list,container,false);
        return mainView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        rvFriendList = mainView.findViewById(R.id.rvFriendList);
        rvFriendList.setLayoutManager(new LinearLayoutManager(host));
        rvFriendList.setAdapter(new FriendAdapter(host,ApplicationClass.getLoggedUser().getFriends()));

    }

}