package com.example.myhealthyagenda.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myhealthyagenda.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;


public class MenuFragment extends Fragment {

    private onItemClickedListener hostActivity;
    private ArrayList<MenuItem> options;
    private RecyclerView rvOptions;
    private String userName;
    private View parentView;
    private static TypedArray drawIDS;
    private  static String[] titleIDS;
    public static final String ARG_USERNAME = "ARG_USERNAME";
    public MenuFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        hostActivity = onItemClickedListener.class.cast(context);

    }

    public static MenuFragment newInstance(String username) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(MenuFragment.ARG_USERNAME,username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(MenuFragment.ARG_USERNAME);
        }
        options = new ArrayList<>();
        drawIDS = getActivity().getResources().obtainTypedArray(R.array.menuIcons);
        titleIDS = getActivity().getResources().getStringArray(R.array.menuTitles);
        for(int i=0 ; i < titleIDS.length; i++){
            options.add(new MenuItem(titleIDS[i],drawIDS.getResourceId(i,-1)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_menu, container, false);
        return parentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try{
            rvOptions = parentView.findViewById(R.id.rvOptions);
            rvOptions.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvOptions.setAdapter(new MenuItemAdapter(getActivity(),options));
        }catch(NullPointerException e){
            Log.d("ERR","COULD NOT FIND RVOPTIONS");
            this.getActivity().finish();
        }
        ((TextView)parentView.findViewById(R.id.tvUsername)).setText(String.format(getString(R.string.welcome_user),userName));

    }


    public interface onItemClickedListener{
        void onItemClicked(MenuItem item);
    }


}