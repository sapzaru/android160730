package com.example.c.criminalintent.SQLite;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.c.criminalintent.Data.Crime;
import com.example.c.criminalintent.SQLite.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by c on 2016-08-06.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuid = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuid));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        return crime;
    }
}
