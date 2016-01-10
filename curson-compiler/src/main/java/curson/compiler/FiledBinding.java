package curson.compiler;

public class FiledBinding {
    private final String name;
    private final String value;
    private final Type kind;

    FiledBinding(String name, String value, Type kind) {
        this.name = name;
        this.value = value;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
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
        STRING
    }
}
