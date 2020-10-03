package com.example.myhealthyagenda.menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthyagenda.R;

import java.util.ArrayList;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivIcon;
        public TextView tvName;
        public ViewHolder(View template){
            super(template);
            ivIcon = template.findViewById(R.id.ivIcon);
            tvName = template.findViewById(R.id.tvName);
            this.itemView.setOnClickListener(view->{
                Log.d("D","CLLICKED");
                context.onItemClicked((MenuItem)view.getTag());
            });
        }
    }

    private ArrayList<MenuItem> options;

    private MenuFragment.onItemClickedListener context;

    public MenuItemAdapter(Context context,ArrayList<MenuItem> options){
        this.options = options;
        try{
            this.context = (MenuFragment.onItemClickedListener)context;
        }catch(ClassCastException e){
            e.printStackTrace();
        }

    }
    @Override
    public MenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int position){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_option,parent,false);
        itemView.setClickable(true);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,parent.getHeight() / getItemCount()));
        return new MenuItemAdapter.ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MenuItemAdapter.ViewHolder holder, int position){
        holder.itemView.setTag(options.get(position));
        holder.ivIcon.setImageResource(options.get(position).getDrawableRes());
        holder.tvName.setText(options.get(position).getTitle());
    }
    @Override
    public int getItemCount(){
        return options.size();
    }

}
