// Generated code from Curson. Do not modify!
package sample.app;

import android.database.Cursor;
import android.database.MatrixCursor;
import curson.CursorBinder;
import java.lang.Override;
import java.util.List;

public class Sample$$CursonEntityBinder implements CursorBinder<Sample> {
  @Override
  public Sample bind(Cursor cursor) {
    final int titleIndex = cursor.getColumnIndex("title");
    final int isPrivateIndex = cursor.getColumnIndex("is_private");
    final int dateIndex = cursor.getColumnIndex("date");
    final int idIndex = cursor.getColumnIndex("_id");
    final int descriptionIndex = cursor.getColumnIndex("description");

    Sample sample = new Sample();
    sample.title = cursor.getString(titleIndex);
    sample.isPrivate = cursor.getInt(isPrivateIndex);
    sample.date = cursor.getLong(dateIndex);
    sample.id = cursor.getInt(idIndex);
    sample.description = cursor.getString(descriptionIndex);

    return sample;
  }

  @Override
  public List<Sample> bind(Cursor cursor, List<Sample> bindList) {
    final int titleIndex = cursor.getColumnIndex("title");
    final int isPrivateIndex = cursor.getColumnIndex("is_private");
    final int dateIndex = cursor.getColumnIndex("date");
    final int idIndex = cursor.getColumnIndex("_id");
    final int descriptionIndex = cursor.getColumnIndex("description");

    do {
    Sample sample = new Sample();
    sample.title = cursor.getString(titleIndex);
    sample.isPrivate = cursor.getInt(isPrivateIndex);
    sample.date = cursor.getLong(dateIndex);
    sample.id = cursor.getInt(idIndex);
    sample.description = cursor.getString(descriptionIndex);

    bindList.add(sample);
    } while(cursor.moveToNext()); 
    return bindList;
  }

  @Override
  public Cursor bind(Sample bind) {
    String[] names = {"title","is_private","date","_id","description"};
    Object[] values = {bind.title,bind.isPrivate,bind.date,bind.id,bind.description};
    MatrixCursor cursor = new MatrixCursor(names);
    cursor.addRow(values);
    return cursor;
  }

  @Override
  public Cursor bind(List<Sample> entities) {
    String[] names = {"title","is_private","date","_id","description"};
    MatrixCursor cursor = new MatrixCursor(names);
    for (Sample bind : entities) {
    Object[] values = {bind.title,bind.isPrivate,bind.date,bind.id,bind.description};
    cursor.addRow(values);
    }
    return cursor;
  }
}
