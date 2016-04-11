package curson;

import android.database.Cursor;
import android.database.MatrixCursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CursonTest {
    @Before
    public void setUp() {
        Curson.BINDERS.clear();
    }

    @Test
    public void zeroBindingsBindDoesNotThrowException() {
        //setup
        Cursor mockCursor = MockCursor.newInstance();
        //done
        EmptyEntity actual = Curson.fromCursor(mockCursor, EmptyEntity.class, 0);
        //verify
        assertThat(actual, nullValue());
        assertThat(mockCursor.isClosed(), is(true));
    }

    @Test
    public void zeroBindingsBindDoesNotThrowException_autoCursorCloseFalse() {
        //setup
        Cursor mockCursor = MockCursor.newInstance();
        //done
        EmptyEntity actual = Curson.fromCursor(mockCursor, EmptyEntity.class, 0, false);
        //verify
        assertThat(actual, nullValue());
        assertThat(mockCursor.isClosed(), is(false));
    }

    @Test
    public void zeroBindingsBindDoesNotThrowException_autoCursorClose() {
        //setup
        Cursor mockCursor = new MockCursor();
        //done
        List<EmptyEntity> actual = Curson.fromCursor(mockCursor, EmptyEntity.class);
        //verify
        assertThat(actual.size(), is(0));
        assertThat(mockCursor.isClosed(), is(true));
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
