package curson.compiler;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

public class CursorRowElement {
    private final String fieldName;
    private final String annotationValue;
    private final Type kind;

    CursorRowElement(String fieldName, String annotationValue, Type kind) {
        this.fieldName = fieldName;
        this.annotationValue = annotationValue;
        this.kind = kind;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getAnnotationValue() {
        return annotationValue;
    }

    public String getCursorValueMethod() {
        switch (kind) {
            case INT:
                return "getInt";
            case BLOB:
                return "getBlob";
            case LONG:
                return "getLong";
            case DOUBLE:
                return "getDouble";
            case FLOAT:
                return "getFloat";
            case SHORT:
                return "getShort";
            case STRING:
                return "getString";
        }
        throw new IllegalArgumentException();
    }

    public enum  Type {
        INT,
        BLOB,
        LONG,
        DOUBLE,
        FLOAT,
        SHORT,
        STRING;

        public static Type valueOf(TypeName typeName) {
            Type type = null;
            if (ArrayTypeName.of(byte.class).equals(typeName)) {
                type = CursorRowElement.Type.BLOB;
            } else if (TypeName.DOUBLE.equals(typeName)) {
                type = CursorRowElement.Type.DOUBLE;
            } else if (TypeName.INT.equals(typeName)) {
                type = CursorRowElement.Type.INT;
            } else if (TypeName.FLOAT.equals(typeName)) {
                type = CursorRowElement.Type.FLOAT;
            } else if (TypeName.LONG.equals(typeName)) {
                type = CursorRowElement.Type.LONG;
            } else if (ClassName.get(String.class).equals(typeName)) {
                type = CursorRowElement.Type.STRING;
            } else if (TypeName.SHORT.equals(typeName)) {
                type = CursorRowElement.Type.SHORT;
            }
            return type;
        }
    }
}
