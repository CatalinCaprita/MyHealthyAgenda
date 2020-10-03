package com.example.myhealthyagenda.article;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myhealthyagenda.R;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Article> articles;
    private Context activity;
    public ArticleAdapter(Context context ,ArrayList<Article> articles){
        this.articles = articles;
        activity = context;
    }
    @NonNull
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("CHECK_INFO",Integer.toString(parent.getId()));
        View paramView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article,parent,false);
        return new  ArticleAdapter.ViewHolder(paramView);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.itemView.setTag(position);
            holder.tvHeadline.setText(articles.get(position).getTextHeadLine());
            holder.tvBrief.setText(articles.get(position).getTextBrief());
        Glide.with(this.activity)
                .asDrawable()
                .placeholder(ViewHolder.PLACEHOLDER_DRAWABLE)
                .load(articles.get(position).getHtmlImg())
                .into(holder.ivHeadline);
    }

  
    @Override
    public int getItemCount() {
        return articles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivHeadline;
        private TextView tvHeadline;
        private TextView tvBrief;
        public static final int PLACEHOLDER_DRAWABLE = R.drawable.ic_home;

        public ViewHolder(@NonNull View root){
            super(root);
            ivHeadline = root.findViewById(R.id.ivHeadline);
            tvHeadline = root.findViewById(R.id.tvHeadline);
            tvBrief = root.findViewById(R.id.tvBreif);
            itemView.setOnClickListener(view ->{
                Intent browseIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                try{
                    browseIntent.putExtra(SearchManager.QUERY,articles.get((Integer)view.getTag()).getLink());
                    activity.startActivity(browseIntent);
                }catch(ClassCastException e){
                    Toast.makeText(activity,R.string.error_link,Toast.LENGTH_SHORT).show();
                }

            });

        }
    }
}
