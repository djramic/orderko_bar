package com.example.orderkobar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "order.db";
    public static final String TABLE_NAME = "order_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DRINK";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "BULK";
    public static final String COL_5 = "QUANTITY";
    public static final String COL_6 = "BAR_TABLE";
    public static final String COL_7 = "DRINK_ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,DRINK TEXT,CATEGORY TEXT," +
                "BULK INTEGER,QUANTITY TEXT,BAR_TABLE TEXT,DRINK_ID TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String drink, String category, String bulk, String quantity, String table,String drink_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, drink);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, bulk);
        contentValues.put(COL_5, quantity);
        contentValues.put(COL_6, table);
        contentValues.put(COL_7, drink_id);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getDrinksOf(String categoy) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where CATEGORY = '" + categoy + "'", null);
        return res;
    }

    public Cursor getDrinksOfName(String name, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where DRINK = '" + name + "' AND BAR_TABLE = '"+ table + "'", null);
        return res;
    }

    public Cursor getDrinksOfTable(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where BAR_TABLE = '" + table + "'", null);
        return res;
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
