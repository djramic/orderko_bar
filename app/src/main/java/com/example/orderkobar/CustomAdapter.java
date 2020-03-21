package com.example.orderkobar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<Card> card_order;
    Context context;


    public CustomAdapter(ArrayList<Card> card_order, Context context) {
        this.card_order = card_order;
        this.context = context;
    }

    @Override
    public int getCount() {
        return card_order.size();
    }

    @Override
    public Object getItem(int position) {
        return card_order.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String table_number = card_order.get(position).getTable_number();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.card_view,parent,false);

        TextView table_number_txvw  = convertView.findViewById(R.id.bar_table_number_txtv);
        ListView listView = convertView.findViewById(R.id.list_view);
        Log.d("ajdavidim","punim listu sa" + card_order.get(position).getList_items());
        ArrayAdapter<String> adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,card_order.get(position).getList_items());
        listView.setAdapter(adapter);
        table_number_txvw.setText(table_number);

        return convertView;

    }
}
