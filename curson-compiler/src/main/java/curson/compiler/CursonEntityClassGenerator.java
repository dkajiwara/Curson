package curson.compiler;


import android.database.Cursor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import curson.CursorBinder;

public class CursonEntityClassGenerator {
    private final String classPackage;
    private final String className;
    private final String targetType;
    private final String generateClassName;
    private Set<CursorRowElement> targetFieldSet = new HashSet<>();

    public CursonEntityClassGenerator(String classPackage, String className, String targetType) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetType = targetType;
        this.generateClassName = className + "$$CursonEntityBinder";
    }

    public void addTargetAnnotationField(CursorRowElement field) {
        targetFieldSet.add(field);
    }

    public JavaFile brewJava() {
        MethodSpec fromCursor = createFromCursorMethod();
        MethodSpec fromCursorAll = createFromCursorAllMethod();

        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(CursorBinder.class), ClassName.get(classPackage, className));

        TypeSpec typeSpec = TypeSpec.classBuilder(generateClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(parameterizedTypeName)
                .addMethod(fromCursor)
                .addMethod(fromCursorAll)
                .build();

        return JavaFile.builder(classPackage, typeSpec)
                .addFileComment("Generated code from Curson. Do not modify!")
                .build();
    }

    private MethodSpec createFromCursorMethod() {
        StringBuilder code = new StringBuilder();
        appendGetColumnIndexCode(code);
        String entityFieldName = className.toLowerCase();
        code.append(className + " " + entityFieldName + " = new " + className + "();\n");
        appendSetFieldCursorValue(code, entityFieldName);
        code.append("return " + entityFieldName + ";\n");

        ParameterSpec param = ParameterSpec.builder(Cursor.class, "cursor").build();
        return MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(param)
                .addAnnotation(Override.class)
                .returns(ClassName.get(classPackage, className))
                .addCode(code.toString())
                .build();
    }

    private MethodSpec createFromCursorAllMethod() {
        ParameterSpec cursorParam = ParameterSpec.builder(Cursor.class, "cursor").build();
        ParameterSpec EntityListParam =
                ParameterSpec.builder(
                        ParameterizedTypeName.get(
                                ClassName.get(List.class),
                                ClassName.get(classPackage, className)),
                        "bindList").build();

        StringBuilder code = new StringBuilder();
        appendGetColumnIndexCode(code);
        code.append("do {\n");
        String entityFieldName = className.toLowerCase();
        code.append(className + " " + entityFieldName + " = new " + className + "();\n");
        appendSetFieldCursorValue(code, entityFieldName);
        code.append("$N.add(" + entityFieldName + ");\n");
        code.append("} while($N.moveToNext()); \n");
        code.append("return $N;\n");

        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(classPackage, className));
        return MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(cursorParam)
                .addParameter(EntityListParam)
                .addAnnotation(Override.class)
                .addCode(code.toString(), EntityListParam, cursorParam, EntityListParam)
                .returns(parameterizedTypeName)
                .build();
    }

    private void appendSetFieldCursorValue(StringBuilder code, String entityFieldName) {
        for (CursorRowElement cursonRow : targetFieldSet) {
            code.append(entityFieldName + "." + cursonRow.getFieldName() +
                    " = cursor." + cursonRow.toGetCursorValueMethod() + "(" + transFormFieldName(cursonRow) + ");\n");
        }
        code.append("\n");
    }

    private void appendGetColumnIndexCode(StringBuilder code) {
        for (CursorRowElement cursonRow : targetFieldSet) {
            code.append("final int " + transFormFieldName(cursonRow) + " = cursor.getColumnIndex(\"" + cursonRow.getAnnotationValue() + "\");\n");
        }
        code.append("\n");
    }


    private String transFormFieldName(CursorRowElement binding) {
        return binding.getFieldName() + "Index";
    }
}
