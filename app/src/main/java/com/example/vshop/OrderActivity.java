package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vshop.adapters.OrderAdapter;
import com.example.vshop.helperClasses.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private Toolbar tToolbar;
    private List<Order> ordersList;
    private RecyclerView rView;
    private OrderAdapter orderItemAdapter;

    FirebaseFirestore firebaseDb;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        firebaseDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tToolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Orders");

        ordersList=new ArrayList<>();
        rView =findViewById(R.id.order_item_container);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true);

        orderItemAdapter = new OrderAdapter(ordersList);
        rView.setAdapter(orderItemAdapter);

        firebaseDb.collection("Orders").document(mAuth.getCurrentUser().getUid())
                .collection("allOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult()!=null){
                        for(DocumentChange doc :task.getResult().getDocumentChanges()){
                            Order item = doc.getDocument().toObject(Order.class);
                            ordersList.add(item);
                        }
                        orderItemAdapter.notifyDataSetChanged();
                    }

                }else{
                    Toast.makeText(OrderActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}