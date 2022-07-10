package com.example.vshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vshop.R;
import com.example.vshop.helperClasses.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<Order> ordersList;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public OrderAdapter(List<Order> ordersList){
        this.ordersList = ordersList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderName.setText(ordersList.get(position).getName());
        holder.orderPrice.setText("₹ "+ordersList.get(position).getPrice());
        holder.orderTime.setText(ordersList.get(position).getTime());
        holder.orderPaymentID.setText("₹ "+ordersList.get(position).getPaymentId());
        Glide.with(holder.itemView.getContext()).load(ordersList.get(position).getImageUrl()).into(holder.orderImage);
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView orderImage;
        TextView orderName;
        TextView orderPrice;
        TextView orderTime;
        TextView orderPaymentID;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderName = itemView.findViewById(R.id.order_name);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderTime = itemView.findViewById(R.id.order_time);
            orderPaymentID = itemView.findViewById(R.id.order_paymentid);
        }
    }
}