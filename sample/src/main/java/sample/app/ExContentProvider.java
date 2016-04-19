package sample.app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;


public class ExContentProvider extends ContentProvider {
    public static final String AUTHORITY = "sample.curson";
    public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/");

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(new String[]{
                SampleColumns._ID,
                SampleColumns.TITLE,
                SampleColumns.DESCRIPTION,
                SampleColumns.IS_PRIVATE,
                SampleColumns.DATE
        });
        cursor.addRow(new Object[]{1, "title1", "description1", 1, 100000000L});
        cursor.addRow(new Object[]{1, "title2", "description2", 1, 100000001L});
        cursor.addRow(new Object[]{1, "title3", "description3", 0, 100000002L});
        cursor.addRow(new Object[]{1, "title4", "description4", 1, 100000003L});
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
