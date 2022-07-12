package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vshop.adapters.ItemAdapter;
import com.example.vshop.helperClasses.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


// On clicking on category items

public class ItemsActivity extends AppCompatActivity {
    
    private RecyclerView rView;
    private ItemAdapter itemsRecyclerAdapter;
    private Toolbar tToolbar;

    private FirebaseFirestore firestoreDb;

    List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        tToolbar= findViewById(R.id.item_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setTitle("Products");

        itemList=new ArrayList<>();
        rView=findViewById(R.id.item_recycler);
        rView.setLayoutManager(new GridLayoutManager(this,2));
        itemsRecyclerAdapter=new ItemAdapter(this,itemList);
        rView.setAdapter(itemsRecyclerAdapter);

        firestoreDb=FirebaseFirestore.getInstance();


        String type=getIntent().getStringExtra("type");
        if(type==null || type.isEmpty()){
            firestoreDb.collection("All").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc:task.getResult().getDocuments()){
                            Item items=doc.toObject(Item.class);
                            itemList.add(items);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("men")){
            firestoreDb.collection("All").whereEqualTo("type","men").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc:task.getResult().getDocuments()){
                            Item items=doc.toObject(Item.class);
                            itemList.add(items);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("women")){
            firestoreDb.collection("All").whereEqualTo("type","women").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc:task.getResult().getDocuments()){
                            Item items=doc.toObject(Item.class);
                            itemList.add(items);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
        if(type!=null && type.equalsIgnoreCase("kid")){
            firestoreDb.collection("All").whereEqualTo("type","kid").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot doc:task.getResult().getDocuments()){
                            Item items=doc.toObject(Item.class);
                            itemList.add(items);
                            itemsRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }

}