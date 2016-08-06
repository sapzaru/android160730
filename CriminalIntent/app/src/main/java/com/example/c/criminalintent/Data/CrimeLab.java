package com.example.c.criminalintent.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.c.criminalintent.SQLite.CrimeBaseHelper;
import com.example.c.criminalintent.SQLite.CrimeCursorWrapper;
import com.example.c.criminalintent.SQLite.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by c on 2016-07-30.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private SQLiteDatabase mDatabase;

    private CrimeLab(Context c) {
        mAppContext = c;
        mDatabase = new CrimeBaseHelper(mAppContext).getWritableDatabase();
    }

    public ArrayList<Crime> getCrimes() {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        // 모든 데이터를 가져오게..
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            while (cursor.moveToNext()) {
                Crime crime = cursor.getCrime();
                crimes.add(crime);
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        return null;
    }

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public void add(Crime c) {
        // DB에 저장
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void update(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.update(CrimeTable.NAME, values,
                            CrimeTable.Cols.UUID + " = ?",
                            new String[] { c.getId().toString() });
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME,
                        null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }
}
