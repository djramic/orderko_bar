package com.example.orderkobar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef, postRef;
    private ArrayList<String> order_list= new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Card> card_list= new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private DatabaseHelper myDb;
    private TextView bar_table_number_txtv;
    private CardView main_card_view;
    private ArrayList<String> unique_table = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.card_list);
        bar_table_number_txtv = findViewById(R.id.bar_table_number_txtv);
        main_card_view = findViewById(R.id.cardView);

        myDb = new DatabaseHelper(this);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("bello");
        postRef = database.getReference("bello");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order_list.clear();
                myDb.clearTable();
                card_list.clear();
                unique_table.clear();
                orders.clear();

                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order_recive = orderSnapshot.getValue(Order.class);

                    myDb.insertData(order_recive.getName(), order_recive.getCategory(), order_recive.getBulk(), order_recive.getQuantity(), order_recive.getTable(),
                            order_recive.getId());
                    orders.add(order_recive);
                }

                for (Order o : orders) {
                    if (!unique_table.contains(o.getTable())) {
                        unique_table.add(o.getTable());
                    }
                }
                Log.d("listaa","stolovi: " + unique_table.toString());
                for (String ut : unique_table) {
                    ArrayList<String> order_for_table = new ArrayList<>();
                    order_for_table.clear();
                    order_for_table = orderForTable(ut);
                    //Log.d("cardorders","narucion za sto " + ut + " " + order_for_table.toString());
                    Card card = new Card(order_for_table, ut);
                    //Log.d("cardorders" , c.getTable_number() + " narudzbina " + c.getList_items().toString());
                    card_list.add(card);
                }

                CustomAdapter customAdapter = new CustomAdapter(card_list, getApplicationContext());
                listView.setAdapter(customAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("databasetest", "Failed to read value.", error.toException());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String table_number = card_list.get(position).getTable_number();
                Cursor res = myDb.getDrinksOfTable(table_number);
                while(res.moveToNext()) {
                    postRef = FirebaseDatabase.getInstance().getReference().child("bello").child(res.getString(6));
                    postRef.removeValue();
                }
            }
        });


    }

    private ArrayList<String>  orderForTable(String table ) {

        ArrayList<Order> table_orders = new ArrayList<>();
        order_list.clear();

        Cursor res = myDb.getDrinksOfTable(table);
        Log.d("tabletest","Usao sam ovde gledam sto " + table);
        while(res.moveToNext()) {
            Order order = new Order(res.getString(1),res.getString(4),res.getString(3),
                    res.getString(5),res.getString(6),res.getString(2));
                    Log.d("tabletest","Ovo je poruzbina za sto 1 " + order.toString());
                    table_orders.add(order);

        }

        ArrayList<String> unique_orders = new ArrayList<>();
        for(Order o : table_orders) {
            if(!unique_orders.contains(o.getName())) {
                unique_orders.add(o.getName());
            }
        }

        for(String order_name : unique_orders) {
            Cursor ord = myDb.getDrinksOfName(order_name,table);
            int quantity = 0;
            while (ord.moveToNext()){
                quantity =quantity + Integer.parseInt(ord.getString(4));

            }
            //Log.d("unique","Za pice: " + order_name + "imamo narudzbina " + quantity);
            String order = quantity + " x " + order_name;
            order_list.add(order);

        }


        StringBuffer buffer = new StringBuffer();
                /*Cursor res = myDb.getAllData();
                while (res.moveToNext()) {
                    buffer.append("ID :" + res.getString(0) + "\n");
                    buffer.append("Drink_id :" + res.getString(6) + "\n");
                    buffer.append("Drink :" + res.getString(1) + "\n");
                    buffer.append("Category :" + res.getString(2) + "\n");
                    buffer.append("Bulk :" + res.getString(3) + "\n");
                    buffer.append("Quantity :" + res.getString(4) + "\n");
                    buffer.append("Table :" + res.getString(5) + "\n\n");
                }
                Log.d("databasetest" , buffer.toString());*/
        return order_list;
        //listView.setAdapter(arrayAdapter);
    }
}
