package curson.compiler;


import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class CursonBytesTest {
    @Test
    public void bytesTest() throws Exception {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestEntity", Joiner.on('\n').join(
                "package test;",
                "import curson.CursorRow;",
                "public class TestEntity {",
                "  @CursorRow(\"row1\") byte[] one;",
                "}"
        ));

        JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/TestEntity$$CursonEntityBinder", ""
                + "package test;\n"
                + "import android.database.Cursor;\n"
                + "import curson.CursorBinder;\n"
                + "import java.lang.Override;\n"
                + "import java.util.List;\n"
                + "public class TestEntity$$CursonEntityBinder implements CursorBinder<TestEntity> {\n"
                + "  @Override\n"
                + "  public TestEntity bind(Cursor cursor) {\n"
                + "    final int oneIndex = cursor.getColumnIndex(\"row1\");\n"
                + "    TestEntity testentity = new TestEntity();\n"
                + "    testentity.one = cursor.getBlob(oneIndex);\n"
                + "    return testentity;\n"
                + "  }\n"
                + "  @Override\n"
                + "  public List<TestEntity> bind(Cursor cursor, List<TestEntity> bindList) {\n"
                + "    final int oneIndex = cursor.getColumnIndex(\"row1\");\n"
                + "    do {\n"
                + "    TestEntity testentity = new TestEntity();\n"
                + "    testentity.one = cursor.getBlob(oneIndex);\n"
                + "    bindList.add(testentity);\n"
                + "    } while(cursor.moveToNext());\n"
                + "    return bindList;\n"
                + "  }\n"
                + "}");

        assertAbout(javaSource()).that(source)
                .processedWith(new CursonProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expectedSource);
    }
}
