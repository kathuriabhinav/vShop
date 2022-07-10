package com.example.vshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.vshop.helperClasses.BestSell;
import com.example.vshop.helperClasses.Feature;
import com.example.vshop.helperClasses.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    private Toolbar tToolbar;
    private ImageView vImage;
    private TextView vItemName, vPrice,vItemRatingWord, vItemDes;
    private Button bRating,bCart,bBuy;

    Feature feature = null;
    BestSell bestSell = null;
    Item items=null;

    FirebaseFirestore firestoreDb;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tToolbar=findViewById(R.id.detail_toolbar);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Description");

        vImage=findViewById(R.id.detail_image);
        vItemName=findViewById(R.id.detail_item_name);
        vPrice=findViewById(R.id.detail_price);
        vItemRatingWord=findViewById(R.id.detail_rating_word);
        vItemDes=findViewById(R.id.detail_item_description);
        bRating=findViewById(R.id.detail_rating_point);
        bCart=findViewById(R.id.detail_add_cart);
        bBuy=findViewById(R.id.detail_buy_now);

        firestoreDb =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        final Object obj=  getIntent().getSerializableExtra("detail");
        if(obj instanceof Feature){
            feature= (Feature) obj;
        }else if(obj instanceof BestSell){
            bestSell= (BestSell) obj;
        }else if(obj instanceof Item){
            items= (Item) obj;
        }
        if(feature!=null){
            Glide.with(getApplicationContext()).load(feature.getimageUrl()).into(vImage);
            vImage.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            vItemName.setText(feature.getName());
            vPrice.setText("₹ "+feature.getPrice());
            bRating.setText(feature.getRating()+"");
            vItemRatingWord.setText(ratingInWord(feature.getRating()));
            vItemDes.setText(feature.getDescription());
        }
        if(bestSell!=null){
            Glide.with(getApplicationContext()).load(bestSell.getimageUrl()).into(vImage);
            vImage.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            vItemName.setText(bestSell.getName());
            vPrice.setText("₹ "+bestSell.getPrice());
            bRating.setText(bestSell.getRating()+"");
            vItemRatingWord.setText(ratingInWord(bestSell.getRating()));
            vItemDes.setText(bestSell.getDescription());
        }
        if(items!=null){
            Glide.with(getApplicationContext()).load(items.getimageUrl()).into(vImage);
            vImage.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            vItemName.setText(items.getName());
            vPrice.setText("₹ "+items.getPrice());
            bRating.setText(items.getRating()+"");
            vItemRatingWord.setText(ratingInWord(items.getRating()));
            vItemDes.setText(items.getDescription());
        }


        bCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feature!=null){
                    firestoreDb.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").add(feature).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if(bestSell!=null){
                    firestoreDb.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").add(bestSell).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
                if(items!=null){
                    firestoreDb.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("Cart").add(items).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();

                                }
                            });
                }

            }
        });

        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this,AddressActivity.class);
                if(feature!=null){
                    intent.putExtra( "item" ,feature);
                }
                if(bestSell!=null){
                    intent.putExtra("item", bestSell);
                }
                if(items!=null){
                    intent.putExtra("item", items);
                }
                startActivity(intent);
            }
        });
    }

    private String ratingInWord(int rat){
        String s;
        if(rat>4){
            s="Excellent";
        }else if(rat>3){
            s="Very Good";
        }else if(rat>2){
            s="Average";
        }else{
            s="Subpar";
        }
        return s;
    }
}