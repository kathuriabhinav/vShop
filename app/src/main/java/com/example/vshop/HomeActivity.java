package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.vshop.adapters.ItemAdapter;
import com.example.vshop.fragments.HomeFragment;
import com.example.vshop.helperClasses.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Fragment homeFrag;

    FirebaseAuth mAuth;

    private Toolbar tToolbar;

    private List<Item> itemsList;
    private EditText vSearchText;
    private RecyclerView rView;
    private ItemAdapter itemRecyclerAdapter;

    private FirebaseFirestore firebaseDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFrag = new HomeFragment();
        loadFragment();

        mAuth= FirebaseAuth.getInstance();
        firebaseDb= FirebaseFirestore.getInstance();

        tToolbar= findViewById(R.id.home_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setTitle("vShop");

        itemsList=new ArrayList<>();
        rView=findViewById(R.id.home_search_recycler);
        rView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemRecyclerAdapter= new ItemAdapter(this,itemsList);
        rView.setAdapter(itemRecyclerAdapter);

        vSearchText= findViewById(R.id.home_search_text);
        vSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    itemsList.clear();
                    itemRecyclerAdapter.notifyDataSetChanged();
                }else{
                    itemsList.clear();
                    searchItem(s.toString());
                    itemRecyclerAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void loadFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menu_logOut){
            mAuth.signOut();
            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(item.getItemId()==R.id.cart){
            Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
        }

        if(item.getItemId()==R.id.order){
            Intent intent=new Intent(HomeActivity.this,OrderActivity.class);
            startActivity(intent);
        }

        return true;
    }

    private void searchItem(String text) {
        if(!text.isEmpty()){
            text=text.toLowerCase();
            firebaseDb.collection("All").whereArrayContains("search",text).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && task.getResult()!=null){
                                for(DocumentSnapshot doc:task.getResult().getDocuments()){
                                    Item items=doc.toObject(Item.class);
                                    itemsList.add(items);
                                    itemRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

}