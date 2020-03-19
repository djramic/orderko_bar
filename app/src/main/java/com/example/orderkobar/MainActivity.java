package com.example.orderkobar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("bello");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order_recive = orderSnapshot.getValue(Order.class);
                    // TO-DO : MAKE ORDER LIST
                    Log.d("databasetest", order_recive.getId() +"---> Naziv: " + order_recive.getName() + " "+ order_recive.getBulk() + ". Kolicina: " + order_recive.getQuantity());
                }
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("databasetest", "Failed to read value.", error.toException());
            }
        });



    }
}
