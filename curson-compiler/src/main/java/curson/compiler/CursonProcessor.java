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
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Map<TypeElement, CursonEntityClassGenerator> targetClassMap = findAndParseTargets(env);

        for(Map.Entry<TypeElement, CursonEntityClassGenerator> entry: targetClassMap.entrySet()) {
            CursonEntityClassGenerator cursonEntityClassGenerator = entry.getValue();
            try {
                cursonEntityClassGenerator.brewJava().writeTo(filer);
            } catch (IOException e) {
                ProcessorUtils.error(processingEnv, entry.getKey(), "Unable to write cursor binder for type %s: %s", entry.getKey(), e.getMessage());
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

        if (ProcessorUtils.isInaccessibleViaGeneratedCode(processingEnv, CursorRow.class, "fields", element)) {
            return;
        }

        String value = element.getAnnotation(CursorRow.class).value();
        String name = element.getSimpleName().toString();
        CursonEntityClassGenerator classGenerator =
                getOrCreateTargetClassGenerator(targetClassMap, enclosingElement);

        TypeName typeName = TypeName.get(element.asType());
        CursorRowElement.Type type = CursorRowElement.Type.valueOf(typeName);
        if (type == null) {
            ProcessorUtils.error(processingEnv, element, "@CursorRow %s is not supported.", typeName);
            return;
        }

        classGenerator.addTargetAnnotationField(new CursorRowElement(name, value, type));
        erasedTargetNames.add(enclosingElement.toString());
    }

    private CursonEntityClassGenerator getOrCreateTargetClassGenerator(Map<TypeElement, CursonEntityClassGenerator> targetClassMap, TypeElement enclosingElement) {
        CursonEntityClassGenerator cursonEntityClassGenerator = targetClassMap.get(enclosingElement);
        if (cursonEntityClassGenerator == null) {
            String targetType = enclosingElement.getQualifiedName().toString();
            String classPackage = ProcessorUtils.getPackageName(elementUtils, enclosingElement);
            String className = ProcessorUtils.getClassName(enclosingElement, classPackage);

            cursonEntityClassGenerator = new CursonEntityClassGenerator(classPackage, className, targetType);
            targetClassMap.put(enclosingElement, cursonEntityClassGenerator);
        }
        return cursonEntityClassGenerator;
    }
}
