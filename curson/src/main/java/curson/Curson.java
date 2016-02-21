package curson;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Curson.
 */
public class Curson {
    private static final boolean DEBUG = false;
    private static final String ENTITY_BINDER_PREFIX = "$$CursonEntityBinder";
    private static final Map<Class<?>, CursorBinder<?>> BINDERS = new LinkedHashMap<>();

    /**
     * Bind annotated field in the specified {@link T}.<br>
     * And, cursor will be not closed automatically.<br>
     * if you need not automatically closed cursor, use {@link Curson#fromCursor(Cursor, Class, boolean)} method.
     *
     * @param cursor target
     * @param entity entity class for cursor binding.
     * @param <T> entity class
     *
     * @throws IllegalArgumentException when cursor is null.
     * @throws IndexOutOfBoundsException requested move cursor failed.
     */
    public static <T> T fromCursor(Cursor cursor, Class<T> entity) {
        return fromCursor(cursor, entity, false);
    }

    /**
     * Bind annotated field in the specified {@link T}.
     *
     * @param cursor target
     * @param entity entity class for cursor binding.
     * @param autoClose if true, cursor closed automatically.
     * @param <T> entity class
     */
    public static <T> T fromCursor(Cursor cursor, Class<T> entity, boolean autoClose) {
        return fromCursor(cursor, entity, 0, autoClose);
    }

    public static <T> T fromCursor(Cursor cursor, Class<T> entity, int position) {
        return fromCursor(cursor, entity, position, false);
    }

    public static <T> T fromCursor(Cursor cursor, Class<T> entity, int position, boolean autoClose) {
        if (cursor == null) {
            throw new IllegalArgumentException("cursor is null.");
        }

        if (!cursor.moveToPosition(position)) {
            throw new IndexOutOfBoundsException("Can't move the cursor to an absolute position(" + position+ ")");
        }

        CursorBinder<T> binder = findCursorForClass(entity);
        try {
            return binder.bind(cursor);
        } finally {
            if (autoClose) {
                cursor.close();
            }
        }
    }

    public static <T> List<T> fromCursorAll(Cursor cursor, Class<T> entity) {
        return fromCursorAll(cursor, entity, false);
    }

    private static <T> List<T> fromCursorAll(Cursor cursor, Class<T> entity, boolean autoClose) {
        if (cursor == null || !cursor.moveToPosition(0)) {
            throw new IllegalArgumentException("cursor is null.");
        }

        if (!cursor.moveToPosition(0)) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>();
        CursorBinder<T> binder = findCursorForClass(entity);
        try {
            return binder.bind(cursor, list);
        } finally {
            if (autoClose) {
                cursor.close();
            }
        }
    }

    private static <T> CursorBinder<T> findCursorForClass(Class<T> entity) {
        //noinspection unchecked
        CursorBinder<T> cursorBinder = (CursorBinder<T>) BINDERS.get(entity);
        if (cursorBinder != null) {
            return cursorBinder;
        }
        try {
            Class<?> bindingClass = Class.forName(entity.getName() + ENTITY_BINDER_PREFIX);
            //noinspection unchecked
            cursorBinder = (CursorBinder<T>) bindingClass.newInstance();
        } catch (ClassNotFoundException e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        } catch (InstantiationException e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            if (DEBUG) {
                e.printStackTrace();
            }
        }
        BINDERS.put(entity, cursorBinder);
        return cursorBinder;
    }
}
