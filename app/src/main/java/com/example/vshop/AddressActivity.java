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
import android.widget.Toast;

import com.example.vshop.adapters.AddressAdapter;
import com.example.vshop.helperClasses.Address;
import com.example.vshop.helperClasses.BestSell;
import com.example.vshop.helperClasses.Feature;
import com.example.vshop.helperClasses.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {

    private Toolbar tToolbar;
    private AddressAdapter aAddress;
    private RecyclerView rView;
    private Button bPay, bAddress;

    private List<Address> mAddressList;
    String address="";

    private FirebaseFirestore firebaseDb;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        final Object obj=getIntent().getSerializableExtra("item");
        List<Item> itemsList = (ArrayList<Item>) getIntent().getSerializableExtra("itemList");


        tToolbar=findViewById(R.id.address_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mAddressList=new ArrayList<>();
        aAddress=new AddressAdapter(getApplicationContext(),mAddressList,this);
        rView=findViewById(R.id.address_recycler);
        rView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rView.setAdapter(aAddress);

        bPay=findViewById(R.id.payment_btn);
        bAddress=findViewById(R.id.add_address_btn);

        mAuth=FirebaseAuth.getInstance();

        firebaseDb=FirebaseFirestore.getInstance();
        firebaseDb.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Address address=doc.toObject(Address.class);
                        mAddressList.add(address);
                        aAddress.notifyDataSetChanged();
                    }
                }
            }
        });


        bAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddressActivity.this,AddAddressActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount=0.0;
                String url="";
                String name="";
                if(obj instanceof Feature){
                    Feature  f= (Feature) obj;
                    amount=f.getPrice();
                    url=f.getimageUrl();
                    name=f.getName();
                }
                if(obj instanceof BestSell){
                    BestSell  f= (BestSell) obj;
                    amount=f.getPrice();
                    url=f.getimageUrl();
                    name=f.getName();

                }
                if(obj instanceof Item){
                    Item  i= (Item) obj;
                    amount=i.getPrice();
                    url=i.getimageUrl();
                    name=i.getName();

                }
                if(itemsList!=null && itemsList.size()>0){

                    if(address.length()!=0) {

                        Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                        intent.putExtra("itemsList", (Serializable) itemsList);
                        intent.putExtra("address",address);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(AddressActivity.this,"Address Missing",Toast.LENGTH_SHORT).show();
                    }

                }else{

                    if(address.length()!=0) {

                        Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                        intent.putExtra("amount", amount);
                        intent.putExtra("img_url", url);
                        intent.putExtra("name", name);
                        intent.putExtra("address", address);

                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(AddressActivity.this,"Address Missing",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    public void setAddress(String s) {
        address=s;
    }
}