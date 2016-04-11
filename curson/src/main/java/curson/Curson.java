package curson;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Curson.
 */
public class Curson {
    private static final String TAG = Curson.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static final String ENTITY_BINDER_PREFIX = "$$CursonEntityBinder";
    static final Map<Class<?>, CursorBinder<?>> BINDERS = new LinkedHashMap<>();

    /**
     * Bind annotated fields in the specified {@link T}.<br>
     * And, cursor will be closed automatically.<br>
     * if you must not automatically closed cursor, use {@link Curson#fromCursor(Cursor, Class, int, boolean)} method.
     *
     * @param cursor cursor
     * @param entity target entity class for cursor binding.
     * @param position absolute position.
     * @param <T> entity class
     * @return {@link T} instance.
     */
    @Nullable
    public static <T> T fromCursor(@NonNull Cursor cursor, @NonNull Class<T> entity, int position) {
        return fromCursor(cursor, entity, position, true);
    }

    /**
     * Bind annotated field in the specified {@link T}.<br>
     *
     * @param cursor target
     * @param entity entity class for cursor binding.
     * @param position absolute position.
     * @param autoClose if true, cursor closed automatically.
     * @param <T> entity class
     * @return {@link T} instance.
     */
    @Nullable
    public static <T> T fromCursor(@NonNull Cursor cursor, @NonNull Class<T> entity, int position, boolean autoClose) {
        try {
            if (!cursor.moveToPosition(position)) {
                if (DEBUG) Log.d(TAG, "Can't move the cursor to an absolute position(" + position+ ")");
                return null;
            }

            CursorBinder<T> binder = findCursorForClass(entity);
            if (binder == null) {
                return null;
            }
            return binder.bind(cursor);
        } finally {
            if (autoClose) {
                cursor.close();
            }
        }
    }

    /**
     * Bind annotated field in the specified {@link T}.<br>
     * And, cursor will be not closed automatically.<br>
     * if you need not automatically closed cursor, use {@link Curson#fromCursor(Cursor, Class, boolean)} method.
     *
     * @param cursor target
     * @param entity entity class for cursor binding.
     * @param <T> entity class
     */
    @NonNull
    public static <T> List<T> fromCursor(@NonNull Cursor cursor, @NonNull Class<T> entity) {
        return fromCursor(cursor, entity, true);
    }

    /**
     * Bind annotated field in the specified {@link T}.
     *
     * @param cursor target
     * @param entity entity class for cursor binding.
     * @param autoClose if true, cursor closed automatically.
     * @param <T> entity class
     */
    @NonNull
    public static <T> List<T> fromCursor(@NonNull Cursor cursor, @NonNull Class<T> entity, boolean autoClose) {
        try {
            if (!cursor.moveToPosition(0)) {
                if (DEBUG) Log.d(TAG, "Can't move the cursor to an absolute position(0)");
                return new ArrayList<>(0);
            }
            CursorBinder<T> binder = findCursorForClass(entity);
            if (binder == null) {
                return new ArrayList<>(0);
            }
            List<T> list = new ArrayList<>();
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
            if (DEBUG) e.printStackTrace();
        } catch (InstantiationException e) {
            if (DEBUG) e.printStackTrace();
        } catch (IllegalAccessException e) {
            if (DEBUG) e.printStackTrace();
        }
        BINDERS.put(entity, cursorBinder);
        return cursorBinder;
    }
}
