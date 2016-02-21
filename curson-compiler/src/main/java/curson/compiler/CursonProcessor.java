package curson.compiler;

import com.google.auto.common.SuperficialValidation;
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
import javax.tools.Diagnostic;

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
        Map<TypeElement, CursonEntityClassGenerator> targetClassMap = findAndParseTargets(env);
        for(Map.Entry<TypeElement, CursonEntityClassGenerator> entry: targetClassMap.entrySet()) {
            CursonEntityClassGenerator cursonEntityClassGenerator = entry.getValue();
            try {
                cursonEntityClassGenerator.brewJava().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private Map<TypeElement, CursonEntityClassGenerator> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, CursonEntityClassGenerator> targetClassMap = new LinkedHashMap<>();
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

    private void parseBind(Element element, Map<TypeElement, CursonEntityClassGenerator> targetClassMap, Set<String> erasedTargetNames) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        String value = element.getAnnotation(CursorRow.class).value();
        String name = element.getSimpleName().toString();
        CursonEntityClassGenerator classGenerator =
                getOrCreateTargetClassGenerator(targetClassMap, enclosingElement);

        TypeName typeName = TypeName.get(element.asType());
        CursorRowElement.Type type = CursorRowElement.Type.valueOf(typeName);

        classGenerator.addTargetAnnotationField(new CursorRowElement(name, value, type));
        erasedTargetNames.add(enclosingElement.toString());
    }

    private CursonEntityClassGenerator getOrCreateTargetClassGenerator(Map<TypeElement, CursonEntityClassGenerator> targetClassMap, TypeElement enclosingElement) {
        CursonEntityClassGenerator cursonEntityClassGenerator = targetClassMap.get(enclosingElement);
        if (cursonEntityClassGenerator == null) {
            String targetType = enclosingElement.getQualifiedName().toString();
            String classPackage = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, classPackage);

            cursonEntityClassGenerator = new CursonEntityClassGenerator(classPackage, className, targetType);
            targetClassMap.put(enclosingElement, cursonEntityClassGenerator);
        }
        return cursonEntityClassGenerator;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
