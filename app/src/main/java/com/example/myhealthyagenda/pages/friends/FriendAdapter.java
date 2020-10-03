package com.example.myhealthyagenda.pages.friends;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthyagenda.R;
import com.example.myhealthyagenda.user.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private ArrayList<User> data;
    private ArrayList<String> aliases;
    private Activity context;
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfile;
        TextView tvName, tvActive, tvProgress;
        public ViewHolder(View iView){
            super(iView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvActive = itemView.findViewById(R.id.tvActive);
            tvProgress = itemView.findViewById(R.id.tvProgress);
            this.itemView.setOnClickListener((view->{

            }));
        }
    }
    public FriendAdapter(Context context, Map<User,String> userFriends){
        this.data = new ArrayList<User>(userFriends.keySet());
        this.context = (Activity) context;
        this.aliases = new ArrayList<String>(userFriends.values());
        Log.d("DATA",data.toString());


    }

    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position ){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
        return new FriendAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.itemView.setTag(position);
        holder.ivProfile.setImageResource(data.get(position).getProileDrawable());
        holder.tvName.setText(aliases.get(position));
        Duration interval = Duration.between(data.get(position).getLastActive().atStartOfDay(),LocalDate.now().atStartOfDay());
        holder.tvActive.setText(context.getString(R.string.friends_msg_activity,interval.toDays()));
        holder.tvProgress.setText(Float.toString(data.get(position).getProgress()));
    }
    @Override
    public int getItemCount(){
        return data.size();
    }

}
