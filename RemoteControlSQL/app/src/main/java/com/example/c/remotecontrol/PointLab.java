package com.example.c.remotecontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.c.remotecontrol.SQLite.PointSystemOpenHelper;

import static com.example.c.remotecontrol.SQLite.PointSystemDbSchema.PointSystemTable;

/**
 * Created by c on 2016-08-07.
 */

// Singleton
public class PointLab {
    private static PointLab sPointLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    // private construct
    private PointLab(Context context) {
        mContext = context;
        mDatabase = new PointSystemOpenHelper(mContext).getWritableDatabase();
    }

    // factory method
    public static PointLab get(Context context) {
        // 액티비티의 컨텍스트는 어플리케이션 컨텍스트를 얻어올수 있고,
        // context.getApplicationContext()
        // 액티비티의 컨텍스트는 토스트를 띄울수 있고
        // 서비스의 컨텍스트는 뷰가 없으므로 토스트를 띄울수 없음.

        if (sPointLab == null) {
            sPointLab = new PointLab(context.getApplicationContext());
        }
        return sPointLab;
    }

    public void add(Point point) {
        ContentValues values = getContentValues(point);
        mDatabase.insert(PointSystemTable.NAME, null, values);
    }

    public void update(Point c) {
        ContentValues values = getContentValues(c);
        mDatabase.update(PointSystemTable.NAME, values,
                PointSystemTable.Cols.UUID+" = ?",
                new String[]{ c.getId().toString() });
    }

    private static ContentValues getContentValues(Point point) {
        ContentValues values = new ContentValues();
        values.put(PointSystemTable.Cols.UUID, point.getId().toString());
        values.put(PointSystemTable.Cols.BEVERAGE, point.getBeverage());
        values.put(PointSystemTable.Cols.DATE, point.getDate().getTime());
        values.put(PointSystemTable.Cols.TEL, point.getTel());
        return values;
    }
}
