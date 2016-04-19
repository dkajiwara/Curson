package sample.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import curson.Curson;

public class MainActivity extends Activity{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getContentResolver().query(ExContentProvider.URI, null, null, null, null);
        List<Sample> samples = Curson.fromCursor(cursor, Sample.class);
        for (Sample sample : samples) {
            Log.d(TAG, "title : " + sample.title + " description " + sample.description);
        }
    }

//    class Test {
//        Test() {
//            MatrixCursor matrixCursor = new MatrixCursor(new String[]{});
//            matrixCursor.addRow(new Object[]{hoge.hoge, hoge.hoge});
//            return matrixCursor;
//        }
//    }
}
