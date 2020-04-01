package com.example.orderkobar;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddClubDialog extends Dialog {
    private Button add_but, cancel_but;
    private EditText club_name;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BasicInfo basicInfo;
    public AddClubDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_club);

        club_name = findViewById(R.id.club_d_name_edtx);
        add_but = findViewById(R.id.club_d_add_but);
        cancel_but = findViewById(R.id.club_d_cancel_but);

        basicInfo = BasicInfo.getInstance();

        cancel_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        add_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClub(club_name.getText().toString());
            }
        });


    }

    private void createClub(String name) {
        Map<String, Object> club = new HashMap<>();
        String id = db.collection("Clubs").document().getId();
        club.put("ID",id);
        club.put("Name",name);
        db.collection("Users").document(basicInfo.getUserId()).collection("Clubs").document(id).set(club)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("createclubtest","Lokal je uspesno dodat u users");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("createclubtest","Lokal nije dodat u users");
                    }
                });
        db.collection("Clubs").document(id).set(club)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("createclubtest","Lokal je uspesno dodat u clubs");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("createclubtest","Lokal nije dodat u clubs");
                    }
                });
        dismiss();
    }


}
