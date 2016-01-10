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

public class BindingClass {
    String classPackage;
    String className;
    String targetType;
    Set<FiledBinding> mBindingSet = new HashSet<>();

    public BindingClass(String classPackage, String className, String targetType) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetType = targetType;
    }

    public JavaFile brewJava() {
        MethodSpec toBindMethod = crateBindMethod();
        MethodSpec toBindListMethod = createBindListMethod();

        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(ClassName.get(CursorBinder.class), ClassName.get(classPackage, className));
        TypeSpec typeSpec =
                TypeSpec.classBuilder(className + "$$CursonEntityBinder")
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(parameterizedTypeName)
                        .addMethod(toBindMethod)
                        .addMethod(toBindListMethod)
                        .build();

        return JavaFile.builder(classPackage, typeSpec)
                .addFileComment("Generated code from Curson. Do not modify!")
                .build();
    }

    private MethodSpec crateBindMethod() {
        StringBuilder code = new StringBuilder();
        for (FiledBinding binding : mBindingSet) {
            code.append("final int " + binding.getName() + "Index" + " = cursor.getColumnIndex(\"" + binding.getValue() + "\");\n");
        }
        String clazzfiledName = className.toLowerCase();
        code.append(className + " " + clazzfiledName + " = new " + className + "();\n");
        for (FiledBinding binding : mBindingSet) {
            code.append(clazzfiledName + "." + binding.getName() +
                    " = cursor." + binding.getCursorValueMethod() + "("    + binding.getName() + "Index);\n");
        }
        code.append("return " + clazzfiledName + ";\n");

        ParameterSpec param = ParameterSpec.builder(Cursor.class, "cursor").build();
        return MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(param)
                .addAnnotation(Override.class)
                .returns(ClassName.get(classPackage, className))
                .addCode(code.toString())
                .build();
    }

    private MethodSpec createBindListMethod() {
        ParameterSpec param = ParameterSpec.builder(Cursor.class, "cursor").build();
        ParameterSpec param2 = ParameterSpec.builder(
                ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(classPackage, className)), "bindList").build();

        StringBuilder code = new StringBuilder();
        for (FiledBinding binding : mBindingSet) {
            code.append("final int " + binding.getName() + "Index" + " = cursor.getColumnIndex(\"" + binding.getValue() + "\");\n");
        }
        code.append("while($N.moveToNext()) { \n");
        String classfiledName = className.toLowerCase();
        code.append("    " + className + " " + classfiledName + " = new " + className + "();\n");

        for (FiledBinding binding : mBindingSet) {
            code.append("    " + classfiledName + "." + binding.getName() +
                    " = cursor." + binding.getCursorValueMethod() + "("    + binding.getName() + "Index);\n");
        }
        code.append("    " + "$N.add(" + classfiledName + ");\n");
        code.append("}\n");
        code.append("return $N;\n");

        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(classPackage, className));
        return MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(param)
                .addParameter(param2)
                .addAnnotation(Override.class)
                .addCode(code.toString(), param, param2, param2)
                .returns(parameterizedTypeName)
                .build();
    }

    public void addFieldCollection(FiledBinding binding) {
        mBindingSet.add(binding);
    }
}
