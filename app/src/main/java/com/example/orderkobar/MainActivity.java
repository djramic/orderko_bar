package com.example.orderkobar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper myDb;
    private FirebaseAuth mAuth;
    private Button sing_up_but, login_but;
    private EditText email_edtx, password_edtx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        login_but = findViewById(R.id.login_but);

        email_edtx = findViewById(R.id.email_edtx);
        password_edtx = findViewById(R.id.password_edtx);


        sing_up_but = findViewById(R.id.sing_up);

        sing_up_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SingUpActivity.class));
            }
        });

        login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_but.setOnClickListener(new View.OnClickListener() {
                String email = email_edtx.getText().toString();
                String password = password_edtx.getText().toString();
                @Override
                public void onClick(View v) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d("logintest", "login success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    }else {
                                        Log.w("logintest", "error while login", task.getException());
                                        Toast.makeText(MainActivity.this, "Pogresna sifra ili e-mail, pokusajte ponovo",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
                });
            }

        });


    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            startActivity(new Intent(MainActivity.this,BarActivity.class));
        }
    }
}