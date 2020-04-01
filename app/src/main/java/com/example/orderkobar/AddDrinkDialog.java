package com.example.orderkobar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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

        basicInfo = BasicInfo.getInstance();


        add_drink_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText drink_name = findViewById(R.id.add_d_drink);
                EditText category = findViewById(R.id.add_d_category);
                EditText bulk = findViewById(R.id.add_d_bulk);
                EditText price = findViewById(R.id.club_d_name_edtx);

                Map<String, Object> drink = new HashMap<>();
                
                drink.put("Name",drink_name.getText().toString());
                drink.put("Category",category.getText().toString());
                drink.put("Bulk",bulk.getText().toString());
                drink.put("Price",price.getText().toString());
                
                addDrink(drink);



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
