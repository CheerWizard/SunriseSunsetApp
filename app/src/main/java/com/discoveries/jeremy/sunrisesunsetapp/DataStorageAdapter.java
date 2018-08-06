package com.discoveries.jeremy.sunrisesunsetapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by User on 29.07.2018.
 */

public class DataStorageAdapter extends ArrayAdapter {

    private SQLiteDatabase sqLiteDatabase;
    private String[] date, speed, distance, lifeStatus;

    public DataStorageAdapter(@NonNull Context context, int resource, String[] date, String[] distance, String[] speed, String[] lifeStatus) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
