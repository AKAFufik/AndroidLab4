package com.example.myclientserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<LikesItem> mExampleList;

    public LikesAdapter(Context context, ArrayList<LikesItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.likes_intem, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        LikesItem currentItem = mExampleList.get(position);
        String imageUrl = currentItem.getImageUrl();
        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.unnamed)
                .fitCenter()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_views);
        }
    }
}