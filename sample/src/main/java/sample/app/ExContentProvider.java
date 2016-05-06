package sample.app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import curson.Curson;


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
        List<Sample> list = new ArrayList<>();
        list.add(new Sample(1, 100000000L, "description1", "title1", 1));
        list.add(new Sample(2, 100000001L, "description2", "title2", 1));
        list.add(new Sample(3, 100000002L, "description3", "title3", 0));
        list.add(new Sample(4, 100000003L, "description4", "title4", 1));
        return Curson.toCursor(list, Sample.class);
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
