package curson.compiler;

import com.google.auto.common.SuperficialValidation;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import curson.CursorRow;

public class CursonProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{
            add(CursorRow.class.getName());
        }};
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.elementUtils = env.getElementUtils();
        this.filer = env.getFiler();
        this.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Map<TypeElement, BindingClass> targetClassMap = findAndParseTargets(env);
        for(Map.Entry<TypeElement, BindingClass> entry: targetClassMap.entrySet()) {
            BindingClass bindingClass = entry.getValue();
            try {
                bindingClass.brewJava().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private Map<TypeElement, BindingClass> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, BindingClass> targetClassMap = new LinkedHashMap<>();
        Set<String> erasedTargetNames = new LinkedHashSet<>();

        // Process each @CursorRow element.
        for (Element element : env.getElementsAnnotatedWith(CursorRow.class)) {
            if (!SuperficialValidation.validateElement(element)) {
                continue;
            }
            parseBind(element, targetClassMap, erasedTargetNames);
        }
        return targetClassMap;
    }

    private void parseBind(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        String value = element.getAnnotation(CursorRow.class).value();
        String name = element.getSimpleName().toString();
        BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);

        FiledBinding binding;
        TypeName typeName = TypeName.get(element.asType());
        FiledBinding.Type type = null;

        if (ArrayTypeName.of(byte.class).equals(typeName)) {
            type = FiledBinding.Type.BLOB;
        } else if (TypeName.DOUBLE.equals(typeName)) {
            type = FiledBinding.Type.DOUBLE;
        } else if (TypeName.INT.equals(typeName)) {
            type = FiledBinding.Type.INT;
        } else if (TypeName.FLOAT.equals(typeName)) {
            type = FiledBinding.Type.FLOAT;
        } else if (TypeName.LONG.equals(typeName)) {
            type = FiledBinding.Type.LONG;
        } else if (ClassName.get(String.class).equals(typeName)) {
            type = FiledBinding.Type.STRING;
        } else if (TypeName.SHORT.equals(typeName)) {
            type = FiledBinding.Type.SHORT;
        }
        binding = new FiledBinding(name, value, type);

        bindingClass.addFieldCollection(binding);

        erasedTargetNames.add(enclosingElement.toString());
    }

    private BindingClass getOrCreateTargetClass(Map<TypeElement, BindingClass> targetClassMap, TypeElement enclosingElement) {
        BindingClass bindingClass = targetClassMap.get(enclosingElement);
        if (bindingClass == null) {
            String targetType = enclosingElement.getQualifiedName().toString();
            String classPackage = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, classPackage);

            bindingClass = new BindingClass(classPackage, className, targetType);
            targetClassMap.put(enclosingElement, bindingClass);
        }
        return bindingClass;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}
