package com.example.orderkobar;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DrinkListFragment extends Fragment {
    private ExpandingList expandingList;
    private ArrayList<Drink> drinks;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button add_drink_button;
    private BasicInfo basicInfo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_drink_list,container,false);

        expandingList = v.findViewById(R.id.expanding_list_main);
        add_drink_button = v.findViewById(R.id.list_f_add_drink_but);
        basicInfo = BasicInfo.getInstance();

        getDrinks();


        add_drink_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(basicInfo.getClub() != null) {
                    AddDrinkDialog cdd = new AddDrinkDialog(getContext(), DrinkListFragment.this);
                    cdd.show();
                }else {
                    Toast.makeText(getContext(),"Morati prvo izabrati lokal",Toast.LENGTH_LONG).show();
                }

            }
        });

        return v;
    }


    private void createDrinkList() {


        ArrayList<String> categorys = new ArrayList<>();

        for (Drink d : drinks) {
            if (!categorys.contains(d.getCategory())) {
                categorys.add(d.getCategory());
            }
        }
        //Log.d("listtest",categorys.toString());
        expandingList.removeAllViews();
        for (String ctg : categorys) {

            ArrayList<Drink> cat_drinks = new ArrayList<>(getDrinksOfCat(ctg));

            ExpandingItem item = expandingList.createNewItem(R.layout.expandring_layout);
            item.createSubItems(cat_drinks.size());
            ((TextView) item.findViewById(R.id.title)).setText(ctg);


            if(ctg.equals("Pivo")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.yellow);
                item.setIndicatorIconRes(R.drawable.beer_drink);
            }

            if(ctg.equals("Vino")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.red);
                item.setIndicatorIconRes(R.drawable.vine_dink);
            }

            if(ctg.equals("Zestina")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.white);
                item.setIndicatorIconRes(R.drawable.alcohol_drink);
            }

            if(ctg.equals("Sok")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.orange);
                item.setIndicatorIconRes(R.drawable.soda_dink);
            }

            int i = 0;
            for (final Drink d : cat_drinks) {
                View v = item.getSubItemView(i);
                ((TextView) v.findViewById(R.id.sub_title)).setText(d.getName());
                ((TextView) v.findViewById(R.id.sub_bulk)).setText(d.getBulk() + "l");
                ((TextView) v.findViewById(R.id.sub_price_txtv)).setText(d.getPrice() + "din");

                ((ImageButton) v.findViewById(R.id.sub_remove_but)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeDrink(d.getId());
                    }
                });

                i++;
            }

        }
    }

    private void removeDrink(String id) {
        if(basicInfo.getClubId() != null) {
            db.collection("Clubs/" + basicInfo.getClubId() + "/Drink")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Pice je uspesno uklonjeno", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "GRESKA, pokusajte ponovo", Toast.LENGTH_LONG).show();
                        }
                    });
            getDrinks();
        }

    }

    private ArrayList<Drink> getDrinksOfCat(String cat) {
        ArrayList<Drink> drks = new ArrayList<>();
        for (Drink d : drinks) {
            if (d.getCategory().equals(cat)) {
                drks.add(d);
            }
        }
    return  drks;
    }

    public void getDrinks() {
        drinks = new ArrayList<>();
        if (basicInfo.getClubId() != null) {
            db.collection("Clubs/" + basicInfo.getClubId() + "/Drink")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("test", document.getId() + " => " + document.getData());
                                    String name = document.getData().get("Name").toString();
                                    String category = document.getData().get("Category").toString();
                                    String bulk = document.getData().get("Bulk").toString();
                                    String price = document.getData().get("Price").toString();
                                    String id = document.getData().get("ID").toString();
                                    Log.w("firestoretest", "Naziv pica: " + name + ", Kategorija pica: " + category + ", Kolicina pica: " + bulk);
                                    Drink drink = new Drink(id, name, category, bulk, "0", price);
                                    drinks.add(drink);

                                }
                                createDrinkList();

                            } else {
                                Log.w("firestoretest", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
    }


}
