package com.example.c.remotecontrol.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.c.remotecontrol.SQLite.PointSystemDbSchema.*;

/**
 * Created by c on 2016-08-07.
 */
public class PointSystemOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "PointSystem.db";

    public PointSystemOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PointSystemTable.NAME + "(" +
                    "_id integer primary key autoincrement, " +
                    PointSystemTable.Cols.UUID + ", " +
                    PointSystemTable.Cols.BEVERAGE + ", " +
                    PointSystemTable.Cols.DATE + ", " +
                    PointSystemTable.Cols.TEL + ")"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
