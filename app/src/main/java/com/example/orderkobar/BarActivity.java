package com.example.orderkobar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BarActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private TextView acct_txtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        acct_txtv = findViewById(R.id.bar_accaont_txtv);

        mAuth = FirebaseAuth.getInstance();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.bar:
                       // selectedFragment =;
                        break;
                    case R.id.orders:
                        selectedFragment = new OrdersFragment();
                        break;
                    case R.id.drink_list:
                       // selectedFragment = ;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner, selectedFragment)
                        .commit();

                return true;
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            acct_txtv.setText(currentUser.getEmail());
        }else{
            startActivity(new Intent(BarActivity.this, MainActivity.class));
        }
    }



}
