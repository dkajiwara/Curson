// Generated code from Curson. Do not modify!
package sample.app;

import android.database.Cursor;
import android.database.MatrixCursor;

import java.util.List;

import curson.CursorBinder;

public class Sample$$CursonEntityBinder implements CursorBinder<Sample> {
  @Override
  public Sample bind(Cursor cursor) {
    final int idIndex = cursor.getColumnIndex("_id");
    final int isPrivateIndex = cursor.getColumnIndex("is_private");
    final int descriptionIndex = cursor.getColumnIndex("description");
    final int dateIndex = cursor.getColumnIndex("date");
    final int titleIndex = cursor.getColumnIndex("title");

    Sample sample = new Sample();
    sample.id = cursor.getInt(idIndex);
    sample.isPrivate = cursor.getInt(isPrivateIndex);
    sample.description = cursor.getString(descriptionIndex);
    sample.date = cursor.getLong(dateIndex);
    sample.title = cursor.getString(titleIndex);

    return sample;
  }

  @Override
  public List<Sample> bind(Cursor cursor, List<Sample> bindList) {
    final int idIndex = cursor.getColumnIndex("_id");
    final int isPrivateIndex = cursor.getColumnIndex("is_private");
    final int descriptionIndex = cursor.getColumnIndex("description");
    final int dateIndex = cursor.getColumnIndex("date");
    final int titleIndex = cursor.getColumnIndex("title");

    do {
    Sample sample = new Sample();
    sample.id = cursor.getInt(idIndex);
    sample.isPrivate = cursor.getInt(isPrivateIndex);
    sample.description = cursor.getString(descriptionIndex);
    sample.date = cursor.getLong(dateIndex);
    sample.title = cursor.getString(titleIndex);

    bindList.add(sample);
    } while(cursor.moveToNext()); 
    return bindList;
  }

  @Override
  public Cursor bind(Sample bind) {
    String[] names = {"_id","is_private","description","date","title"};
    Object[] values = {bind.id,bind.isPrivate,bind.description,bind.date,bind.title};
    MatrixCursor cursor = new MatrixCursor(names);
    cursor.addRow(values);
    return cursor;
  }

  @Override
  public Cursor bind(List<Sample> entities) {
    String[] names = {"_id","is_private","description","date","title"};
    MatrixCursor cursor = new MatrixCursor(names);
    for (Sample bind : entities) {
    Object[] values = {bind.id,bind.isPrivate,bind.description,bind.date,bind.title};
    cursor.addRow(values);
    }
    return cursor;
  }
}
