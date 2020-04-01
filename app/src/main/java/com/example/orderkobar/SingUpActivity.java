package com.example.orderkobar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.*;

public class SingUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button sing_up_but;
    private EditText name_edtx, email_edtx, password_edtx;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();

        name_edtx = findViewById(R.id.sing_up_name_edtx);
        email_edtx = findViewById(R.id.sing_up_email_edtx);
        password_edtx = findViewById(R.id.sing_up_password);
        sing_up_but = findViewById(R.id.sing_up_sing_up_button);


        sing_up_but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_edtx.getText().toString();
                String password = password_edtx.getText().toString();
                Log.d("singup", "email: " + email + " password: " + password);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("singUp test", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    createUserDatabase(user);
                                    //updateUI(user);
                                } else {
                                    Log.w("singUp test", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SingUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        });
    }



    private void updateUI(FirebaseUser user) {
        if(user!= null){
            startActivity(new Intent(SingUpActivity.this,BarActivity.class));
        }else {

        }
    }


    private void createUserDatabase(FirebaseUser user) {
        Map<String, Object> u = new HashMap<>();
        u.put("email", user.getEmail());
        u.put("ID", user.getUid());

        db.collection("Users").document(user.getUid()).set(u)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("singUp test", "user data create successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("singUp test", "user data create failure");
                    }
                });
    }
}
