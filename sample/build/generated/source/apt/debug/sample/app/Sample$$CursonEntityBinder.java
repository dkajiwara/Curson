// Generated code from Curson. Do not modify!
package sample.app;

import android.database.Cursor;
import curson.CursorBinder;
import java.lang.Override;
import java.util.List;

public class Sample$$CursonEntityBinder implements CursorBinder<Sample> {
  @Override
  public Sample bind(Cursor cursor) {
    final int descriptionIndex = cursor.getColumnIndex("description");
    final int dateIndex = cursor.getColumnIndex("date");
    final int isPrivateIndex = cursor.getColumnIndex("is_private");
    final int idIndex = cursor.getColumnIndex("_id");
    final int titleIndex = cursor.getColumnIndex("title");

    Sample sample = new Sample();
    sample.description = cursor.getString(descriptionIndex);
    sample.date = cursor.getLong(dateIndex);
    sample.isPrivate = cursor.getInt(isPrivateIndex);
    sample.id = cursor.getInt(idIndex);
    sample.title = cursor.getString(titleIndex);

    return sample;
  }

  @Override
  public List<Sample> bind(Cursor cursor, List<Sample> bindList) {
    final int descriptionIndex = cursor.getColumnIndex("description");
    final int dateIndex = cursor.getColumnIndex("date");
    final int isPrivateIndex = cursor.getColumnIndex("is_private");
    final int idIndex = cursor.getColumnIndex("_id");
    final int titleIndex = cursor.getColumnIndex("title");

    do {
    Sample sample = new Sample();
    sample.description = cursor.getString(descriptionIndex);
    sample.date = cursor.getLong(dateIndex);
    sample.isPrivate = cursor.getInt(isPrivateIndex);
    sample.id = cursor.getInt(idIndex);
    sample.title = cursor.getString(titleIndex);

    bindList.add(sample);
    } while(cursor.moveToNext()); 
    return bindList;
  }
}
