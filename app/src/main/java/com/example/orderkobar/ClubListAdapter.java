package com.example.orderkobar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ClubListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> clubs;

    public ClubListAdapter(Context context, ArrayList<String> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @Override
    public int getCount() {
        return clubs.size();
    }

    @Override
    public Object getItem(int position) {
        return clubs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.list_f_bar_clubs,parent,false);

        TextView name = convertView.findViewById(R.id.list_f_bar_name_txtv);
        name.setText(clubs.get(position));

        return convertView;
    }
}
