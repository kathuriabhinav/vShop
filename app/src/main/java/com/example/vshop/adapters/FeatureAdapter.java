package com.example.vshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vshop.DetailActivity;
import com.example.vshop.R;
import com.example.vshop.helperClasses.Feature;

import java.util.List;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {

    Context context;
    List<Feature> mfeatureList;

    public FeatureAdapter(Context context, List<Feature> mfeatureList) {
        this.context = context;
        this.mfeatureList = mfeatureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_feature_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.featurePrice.setText("₹ "+mfeatureList.get(position).getPrice());
        holder.featureName.setText(mfeatureList.get(position).getName());
        Glide.with(context).load(mfeatureList.get(position).getimageUrl()).into(holder.featureImage);
        holder.featureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", mfeatureList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mfeatureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView featureImage;
        TextView featurePrice;
        TextView featureName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureImage = itemView.findViewById(R.id.feature_image);
            featurePrice = itemView.findViewById(R.id.feature_price);
            featureName = itemView.findViewById(R.id.feature_name);
        }
    }
}