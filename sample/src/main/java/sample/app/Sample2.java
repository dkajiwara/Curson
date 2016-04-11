package sample.app;

import curson.CursorRow;

public class Sample2 {
    @CursorRow("CursorRow1")
    int mInt;
    @CursorRow("CursorRow2")
    String mString;
    @CursorRow("CursorRow3")
    double mDouble;
    @CursorRow("CursorRow4")
    short mShort;
    @CursorRow("CursorRow5")
    byte[] mBytes;
    @CursorRow("CursorRow6")
    float mFloat;
}
