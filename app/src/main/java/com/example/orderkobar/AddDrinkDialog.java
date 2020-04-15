package com.example.orderkobar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDrinkDialog extends Dialog {
    private Button add_drink_but;
    private Button cancel_but;
    private DrinkListFragment fragment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BasicInfo basicInfo;
    private EditText drink_name;
    private EditText category;
    private EditText bulk;
    private EditText price;


    public AddDrinkDialog(@NonNull Context context, DrinkListFragment df) {
        super(context);
        fragment = df;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_drink);

        add_drink_but = findViewById(R.id.club_d_add_but);
        cancel_but = findViewById(R.id.club_d_cancel_but);
        drink_name = findViewById(R.id.add_d_drink);
        category = findViewById(R.id.add_d_category);
        bulk = findViewById(R.id.add_d_bulk);
        price = findViewById(R.id.club_d_name_edtx);

        basicInfo = BasicInfo.getInstance();


        add_drink_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object> drink = new HashMap<>();
                
                drink.put("Name",drink_name.getText().toString());
                drink.put("Category",category.getText().toString());
                drink.put("Bulk",bulk.getText().toString());
                drink.put("Price",price.getText().toString());
                
                addDrink(drink);

            }
        });
        drink_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("adddrinktest", "Klikno si drink name");
                drink_name.setText("");
                return true;
            }
        });
        bulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bulk.setText("");
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setText("");
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price.setText("");

            }
        });


        cancel_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.getDrinks();
                dismiss();
            }
        });




    }

    private void addDrink(Map<String, Object> drink) {
        String id =  db.collection("Clubs/" + basicInfo.getClubId() + "/Drink").document().getId();
        drink.put("ID",id);
        db.collection("Clubs/" + basicInfo.getClubId() + "/Drink")
                .document(id).set(drink)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("adddrinktest", "DocumentSnapshot successfully written!");
                        Toast.makeText(getContext(),"Piće je uspešno dodato",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("adddrinktest", "Error writing document", e);
                    }
                });
    }

}
