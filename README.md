Curson
======
[![Circle CI](https://circleci.com/gh/dkajiwara/Curson.svg?style=shield)](https://circleci.com/gh/dkajiwara/Curson)
[![Current release](https://api.bintray.com/packages/dkajiwara/maven/curson/images/download.svg)](https://dl.bintray.com/dkajiwara/maven/jp/dkajiwara)

Cursor binding which uses annotation processing to generate boilerplate code for you.  

 * Eliminate `getColumnIndex` calls by using `@CursorRow` on fields.

Usage
-----
 
```java
// Create entity class for cursor binding.
class Item {
    @CursorRow("_id")
    int id;
    @CursorRow("MEMO")
    String memo;
    @CursorRow("DATE")
    long date;
}

public Item getItem() {
    Cursor cursor = context.getContentResolver().query(
            ExContentProvider.Contract.MEMO_URI,
            null,
            MemoColumns.MEMO_ID + " = ?",
            new String[]{dataSnapshot.getKey()}, null);
    return Curson.fromCursor(cursor, Item.class, 0);
}

public List<Item> getItems() {
    Cursor cursor = context.getContentResolver().query(
            ExContentProvider.Contract.MEMO_URI,
            null, null, null, null);
    return Curson.fromCursor(cursor, Item.class);
}

public Cursor toCursor() {
    Item item = new Item();
    item.id = 1;
    item.memo = "Hello Curson!!";
    item.date = 1L;
    return Curson.toCursor(item, Item.class);
}

public Cursor toCursor() {
    List<Item> items = new ArrayList<>();
    Item item = new Item();
    item.id = 1;
    item.memo = "Hello Curson!!";
    item.date = 1L;
    items.add(item);
    return Curson.toCursor(items, Item.class);
}
```

Installation
--------
```
apply plugin: 'com.neenbedankt.android-apt'

dependencies {
    compile 'com.github.dkajiwara:curson:0.4.0'
    apt 'com.github.dkajiwara:curson-compiler:0.4.0'
}
```

License
-------
    The MIT License (MIT)
    
    Copyright (c) 2016 daiki kajiwara
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.


