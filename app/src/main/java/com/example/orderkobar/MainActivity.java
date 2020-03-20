package com.example.orderkobar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private List<String> order_list= new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private DatabaseHelper myDb;
    private TextView bar_table_number_txtv;
    private CardView main_card_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        bar_table_number_txtv = findViewById(R.id.bar_table_number_txtv);
        main_card_view = findViewById(R.id.cardView);


        myDb = new DatabaseHelper(this);
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,order_list);
        bar_table_number_txtv.setText("1");



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("bello");
        postRef = database.getReference("bello");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order_list.clear();
                myDb.clearTable();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order_recive = orderSnapshot.getValue(Order.class);
                    String order = order_recive.getQuantity() + " x " + order_recive.getName() + " " + order_recive.getBulk();
                    Log.d("databasetest", order_recive.getId() +"----> " + order);

                    myDb.insertData(order_recive.getName(),order_recive.getCategory(),order_recive.getBulk(),order_recive.getQuantity(),order_recive.getTable(),
                                    order_recive.getId());
                    order_list.add(order);
                }

                StringBuffer buffer = new StringBuffer();
                Cursor res = myDb.getAllData();
                while (res.moveToNext()) {
                    buffer.append("ID :" + res.getString(0) + "\n");
                    buffer.append("Drink_id :" + res.getString(6) + "\n");
                    buffer.append("Drink :" + res.getString(1) + "\n");
                    buffer.append("Category :" + res.getString(2) + "\n");
                    buffer.append("Bulk :" + res.getString(3) + "\n");
                    buffer.append("Quantity :" + res.getString(4) + "\n");
                    buffer.append("Table :" + res.getString(5) + "\n\n");
                }
                Log.d("databasetest" , buffer.toString());

                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("databasetest", "Failed to read value.", error.toException());
            }
        });



        main_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("databasetest", "Brisem vrednost");
                Cursor res = myDb.getAllData();
                while (res.moveToNext()) {
                    Log.d("databasetest", "Pokupio sam id: " + res.getString(6));
                    postRef = FirebaseDatabase.getInstance().getReference().child("bello").child(res.getString(6));
                    postRef.removeValue();
                }
            }
        });



    }
}
