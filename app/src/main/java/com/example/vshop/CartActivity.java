package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.vshop.adapters.CartAdapter;
import com.example.vshop.helperClasses.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.ItemRemoved {

    Toolbar tToolbar;
    private List<Item> itemsList;
    private RecyclerView rView;
    private CartAdapter cartItemAdapter;
    private Button bBuy;
    private TextView tTotalAmount;

    double tTotalAmountInDouble;

    FirebaseFirestore firebaseDb;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tToolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Cart");

        itemsList=new ArrayList<>();
        rView =findViewById(R.id.cart_item_container);
        tTotalAmount =findViewById(R.id.total_amount);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true);

        bBuy= findViewById(R.id.cart_item_buy_now);
        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tTotalAmountInDouble!=0.0) {
                    Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                    intent.putExtra("itemList", (Serializable) itemsList);
                    startActivity(intent);
                }else{
                    Toast.makeText(CartActivity.this,"Empty Cart",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cartItemAdapter = new CartAdapter(itemsList,this);
        rView.setAdapter(cartItemAdapter);

        firebaseDb.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult()!=null){
                        for(DocumentChange doc :task.getResult().getDocumentChanges()){
                            String documentId = doc.getDocument().getId();
                            Item item = doc.getDocument().toObject(Item.class);
                            item.setDocId(documentId);
                            itemsList.add(item);
                        }
                        calculateAmount(itemsList);
                        cartItemAdapter.notifyDataSetChanged();
                    }

                }else{
                    Toast.makeText(CartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void calculateAmount(List<Item> itemsList) {
        tTotalAmountInDouble = 0.0;
        for(Item item:itemsList){
            tTotalAmountInDouble += item.getPrice();
        }
        tTotalAmount.setText("Total Amount : "+tTotalAmountInDouble);
    }

    @Override
    public void onItemRemoved(List<Item> itemsList) {
        calculateAmount(itemsList);
    }

}