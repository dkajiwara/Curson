package curson;

import android.database.Cursor;
import android.database.MatrixCursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CursonTest {
    @Before
    public void setUp() {
        Curson.BINDERS.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroBindingsBindThrowException() {
        //setup
        Cursor mockCursor = MockCursor.newInstance();
        //done
        Curson.fromCursor(mockCursor, EmptyEntity.class, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroBindingsBindThrowException_autoCursorCloseFalse() {
        //setup
        Cursor mockCursor = MockCursor.newInstance();
        //done
        Curson.fromCursor(mockCursor, EmptyEntity.class, 0, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroBindingsBindThrowException_autoCursorClose() {
        //setup
        Cursor mockCursor = new MockCursor();
        //done
        Curson.fromCursor(mockCursor, EmptyEntity.class);
    }

    public static class EmptyEntity {}

    public static class MockCursor extends MatrixCursor {
        public MockCursor() {
            super(new String[]{"columnName1", "columnName2"});
        }

        static Cursor newInstance() {
            MockCursor mockCursor = new MockCursor();
            mockCursor.addRow(new Object[]{"string", true});
            return mockCursor;
        }
    }
}
