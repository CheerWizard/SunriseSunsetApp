package com.discoveries.jeremy.sunrisesunsetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 29.07.2018.
 */

public class DataStorage extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "UserData";
    private static final String COL1 = "ID";
    private static final String COL2 = "Date";
    private static final String COL3 = "Distance";
    private static final String COL4 = "Speed";
    private static final String COL5 = "LifeStatus";

    public DataStorage(Context context) {
        super(context , TABLE_NAME , null , 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + "INTEGER KEY AUTOINCREMENT, " + COL2 + " DATE" + COL3 + " DOUBLE" + COL4 + " DOUBLE" + COL5 + " TEXT" + ")";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String dropTable = "DROP IF TABLE EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(dropTable);
    }

    public boolean addAllData(String date , String distance , String speed , String lifeStatus , SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2 , date);
        contentValues.put(COL3 , distance);
        contentValues.put(COL4 , speed);
        contentValues.put(COL5 , lifeStatus);

        long result = sqLiteDatabase.insert(TABLE_NAME , null , contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public static Cursor getAllData(SQLiteDatabase sqLiteDatabase) {

        String[] columns = {COL2 , COL3 , COL4 , COL5};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME , columns , null , null , null , null  , null);

        return cursor;
    }

}
