// Generated code from Curson. Do not modify!
package sample.app;

import android.database.Cursor;
import curson.CursorBinder;
import java.lang.Override;
import java.util.List;

public class Sample$$CursonEntityBinder implements CursorBinder<Sample> {
  @Override
  public Sample bind(Cursor cursor) {
    final int mBytesIndex = cursor.getColumnIndex("CursorRow5");
    final int mStringIndex = cursor.getColumnIndex("CursorRow2");
    final int mDoubleIndex = cursor.getColumnIndex("CursorRow3");
    final int mFloatIndex = cursor.getColumnIndex("CursorRow6");
    final int mIntIndex = cursor.getColumnIndex("CursorRow1");
    final int mShortIndex = cursor.getColumnIndex("CursorRow4");

    Sample sample = new Sample();
    sample.mBytes = cursor.getBlob(mBytesIndex);
    sample.mString = cursor.getString(mStringIndex);
    sample.mDouble = cursor.getDouble(mDoubleIndex);
    sample.mFloat = cursor.getFloat(mFloatIndex);
    sample.mInt = cursor.getInt(mIntIndex);
    sample.mShort = cursor.getShort(mShortIndex);

    return sample;
  }

  @Override
  public List<Sample> bind(Cursor cursor, List<Sample> bindList) {
    final int mBytesIndex = cursor.getColumnIndex("CursorRow5");
    final int mStringIndex = cursor.getColumnIndex("CursorRow2");
    final int mDoubleIndex = cursor.getColumnIndex("CursorRow3");
    final int mFloatIndex = cursor.getColumnIndex("CursorRow6");
    final int mIntIndex = cursor.getColumnIndex("CursorRow1");
    final int mShortIndex = cursor.getColumnIndex("CursorRow4");

    do {
    Sample sample = new Sample();
    sample.mBytes = cursor.getBlob(mBytesIndex);
    sample.mString = cursor.getString(mStringIndex);
    sample.mDouble = cursor.getDouble(mDoubleIndex);
    sample.mFloat = cursor.getFloat(mFloatIndex);
    sample.mInt = cursor.getInt(mIntIndex);
    sample.mShort = cursor.getShort(mShortIndex);

    bindList.add(sample);
    } while(cursor.moveToNext()); 
    return bindList;
  }
}
