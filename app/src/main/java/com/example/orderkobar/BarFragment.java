package com.example.orderkobar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BarFragment extends Fragment {
    private Button add_club;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BasicInfo basicInfo;
    private ClubListAdapter adapter;
    private ArrayList<String> clubs = new ArrayList<>();
    private ArrayList<String> clubs_ids = new ArrayList<>();
    private ListView club_list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bar, container, false);

        add_club = v.findViewById(R.id.bar_f_add_caffe_but);
        club_list = v.findViewById(R.id.bar_f_list_view);


        basicInfo = BasicInfo.getInstance();

        getClubs();


        add_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddClubDialog addClubDialog = new AddClubDialog(getContext(),BarFragment.this );
                addClubDialog.show();
            }

        });

        club_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                basicInfo.setClubListPosition(position);
                basicInfo.setClub(clubs.get(position));
                basicInfo.setClubId(clubs_ids.get(position));
                updateUI();
            }
        });

        return v;
    }

    private void updateUI() {
        if(basicInfo.getClub()!= null) {
           ((BarActivity) getActivity()).updateUbarInfo();
        }

    }

    public void getClubs() {
        clubs.clear();
        clubs_ids.clear();
        db.collection("Users/" + basicInfo.getUserId() + "/Clubs")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        String name = document.get("Name").toString();
                        String id = document.get("ID").toString();
                        clubs.add(name);
                        clubs_ids.add(id);
                    }
                    createList();

                }else{
                    Log.d("addclubtest","ERROR while reading club list");
                }
            }
        });
    }

    private void createList() {
        adapter = new ClubListAdapter(getContext(),clubs);
        club_list.setAdapter(adapter);

        club_list.post(new Runnable() {
            @Override
            public void run() {
                if(basicInfo.getClubListPosition() != -1) {
                    club_list.getChildAt(basicInfo.getClubListPosition()).setSelected(true);
                }
            }
        });
    }


}
