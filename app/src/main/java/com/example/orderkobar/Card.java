package com.example.orderkobar;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private ArrayList<String> list_items;
    private String table_number;

    public Card(ArrayList<String> list_items, String table_number) {
        this.list_items = new ArrayList<>(list_items);
        this.table_number = table_number;
    }

    public List<String> getList_items() {
        return list_items;
    }

    public void setList_items(ArrayList<String> list_items) {
        this.list_items = list_items;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }
}
