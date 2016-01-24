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
     * And, cursor will be closed automatically.<br>
     * if you need not automatically closed cursor, use {@link Curson#bind(Cursor, Class, boolean)} method.
     *
     * @param cursor target
     * @param entity entity class for cursor binding.
     * @param <T>
     */
    public static <T> T bind(Cursor cursor, Class<T> entity) {
        return bind(cursor, entity, true);
    }

    /**
     * Bind annotated field in the specified {@link T}.
     *
     * @param cursor target
     * @param entity entity class for cursor binding.
     * @param autoClose if true, cursor is closed.
     * @param <T>
     */
    public static <T> T bind(Cursor cursor, Class<T> entity, boolean autoClose) {
        return bind(cursor, entity, 0, autoClose);
    }

    public static <T> T bind(Cursor cursor, Class<T> entity, int position) {
        return bind(cursor, entity, position);
    }

    public static <T> T bind(Cursor cursor, Class<T> entity, int position, boolean autoClose) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return null;
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

    public static <T> List<T> bindAll(Cursor cursor, Class<T> entity) {
        return bindAll(cursor, entity, true);
    }

    private static <T> List<T> bindAll(Cursor cursor, Class<T> entity, boolean autoClose) {
        if (cursor == null) {
            return new ArrayList<>();
        }
        CursorBinder<T> binder = findCursorForClass(entity);
        List<T> list = new ArrayList<>();
        try {
            return binder.bind(cursor, list);
        } finally {
            if (autoClose) {
                cursor.close();
            }
        }
    }

    private static <T> CursorBinder<T> findCursorForClass(Class<T> entity) {
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
