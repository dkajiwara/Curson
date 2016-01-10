package curson;

import android.database.Cursor;

import java.util.List;

public interface CursorBinder<T> {
    T bind(Cursor cursor);

    List<T> bind(Cursor cursor, List<T> bindList);
}
