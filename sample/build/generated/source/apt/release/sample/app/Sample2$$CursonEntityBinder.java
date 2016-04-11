// Generated code from Curson. Do not modify!
package sample.app;

import android.database.Cursor;
import curson.CursorBinder;
import java.lang.Override;
import java.util.List;

public class Sample2$$CursonEntityBinder implements CursorBinder<Sample2> {
  @Override
  public Sample2 bind(Cursor cursor) {
    final int mFloatIndex = cursor.getColumnIndex("CursorRow6");
    final int mDoubleIndex = cursor.getColumnIndex("CursorRow3");
    final int mShortIndex = cursor.getColumnIndex("CursorRow4");
    final int mBytesIndex = cursor.getColumnIndex("CursorRow5");
    final int mStringIndex = cursor.getColumnIndex("CursorRow2");
    final int mIntIndex = cursor.getColumnIndex("CursorRow1");

    Sample2 sample2 = new Sample2();
    sample2.mFloat = cursor.getFloat(mFloatIndex);
    sample2.mDouble = cursor.getDouble(mDoubleIndex);
    sample2.mShort = cursor.getShort(mShortIndex);
    sample2.mBytes = cursor.getBlob(mBytesIndex);
    sample2.mString = cursor.getString(mStringIndex);
    sample2.mInt = cursor.getInt(mIntIndex);

    return sample2;
  }

  @Override
  public List<Sample2> bind(Cursor cursor, List<Sample2> bindList) {
    final int mFloatIndex = cursor.getColumnIndex("CursorRow6");
    final int mDoubleIndex = cursor.getColumnIndex("CursorRow3");
    final int mShortIndex = cursor.getColumnIndex("CursorRow4");
    final int mBytesIndex = cursor.getColumnIndex("CursorRow5");
    final int mStringIndex = cursor.getColumnIndex("CursorRow2");
    final int mIntIndex = cursor.getColumnIndex("CursorRow1");

    do {
    Sample2 sample2 = new Sample2();
    sample2.mFloat = cursor.getFloat(mFloatIndex);
    sample2.mDouble = cursor.getDouble(mDoubleIndex);
    sample2.mShort = cursor.getShort(mShortIndex);
    sample2.mBytes = cursor.getBlob(mBytesIndex);
    sample2.mString = cursor.getString(mStringIndex);
    sample2.mInt = cursor.getInt(mIntIndex);

    bindList.add(sample2);
    } while(cursor.moveToNext()); 
    return bindList;
  }
}
