package sample.app;

import curson.CursorRow;

public class Sample {
    @CursorRow(SampleColumns._ID)
    int id;
    @CursorRow(SampleColumns.DATE)
    long date;
    @CursorRow(SampleColumns.DESCRIPTION)
    String description;
    @CursorRow(SampleColumns.TITLE)
    String title;
    @CursorRow(SampleColumns.IS_PRIVATE)
    int isPrivate;

    Sample() {}

    Sample(int id, long date, String description, String title, int isPrivate) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.title = title;
        this.isPrivate = isPrivate;
    }
}
