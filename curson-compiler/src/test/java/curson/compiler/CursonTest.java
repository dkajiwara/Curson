package curson.compiler;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class CursonTest {

    @Test
    public void notSupportedCursorType() throws Exception {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestEntity", Joiner.on('\n').join(
                "package test;",
                "import curson.CursorRow;",
                "public class TestEntity {",
                "  @CursorRow(\"row1\") Object one;",
                "}"
        ));

        assertAbout(javaSource()).that(source)
                .processedWith(new CursonProcessor())
                .failsToCompile()
                .withErrorContaining("@CursorRow java.lang.Object is not supported.")
                .in(source)
                .onLine(4);
    }

    @Test
    public void failsIfStatic() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestEntity", Joiner.on('\n').join(
                "package test;",
                "import curson.CursorRow;",
                "public class TestEntity {",
                "  @CursorRow(\"row1\") static String one;",
                "}"
        ));

        assertAbout(javaSource()).that(source)
                .processedWith(new CursonProcessor())
                .failsToCompile()
                .withErrorContaining("@CursorRow fields must not be private or static.")
                .in(source).onLine(4);
    }

    @Test
    public void failsIfPrivate() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestEntity", Joiner.on('\n').join(
                "package test;",
                "import curson.CursorRow;",
                "public class TestEntity {",
                "  @CursorRow(\"row1\") private String one;",
                "}"
        ));

        assertAbout(javaSource()).that(source)
                .processedWith(new CursonProcessor())
                .failsToCompile()
                .withErrorContaining("@CursorRow fields must not be private or static.")
                .in(source).onLine(4);
    }

    @Test
    public void failsIfInPrivateClass() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.TestEntity", Joiner.on('\n').join(
                "package test;",
                "import curson.CursorRow;",
                "public class TestEntity {",
                "  private static class Inner {",
                "    @CursorRow(\"row1\") String one;",
                "  }",
                "}"
        ));

        assertAbout(javaSource()).that(source)
                .processedWith(new CursonProcessor())
                .failsToCompile()
                .withErrorContaining(
                        "@CursorRow fields may not be contained in private classes. (test.TestEntity.Inner.one)")
                .in(source).onLine(4);
    }
}
