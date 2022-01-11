package lpy.mybatis.generator;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.sun.tools.javac.code.Symbol;
import lombok.Getter;
import lpy.mybatis.generator.annotation.MybatisGeneratorHelper;
import lpy.mybatis.generator.entity.EntityField;
import lpy.mybatis.generator.entity.EntityScanner;
import lpy.mybatis.generator.entity.ReflectEntity;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.NOTE;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName MybatisProcessor
 * @date 2021/9/7 8:54 下午
 **/
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("lpy.mybatis.generator.annotation.MybatisGeneratorHelper")
public class MybatisProcessor extends AbstractProcessor {

    protected Filer filer;

    @Getter
    protected Messager messager;

    private boolean generated = false;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.filer = env.getFiler();
        this.messager = env.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || generated) {
            return true;
        }
        messager.printMessage(NOTE, "mybatis-utils generator entity helper start...");
        for (Element element : roundEnv.getRootElements()) {
            MybatisGeneratorHelper mybatisGeneratorHelper = element.getAnnotation(MybatisGeneratorHelper.class);
            if (mybatisGeneratorHelper == null) {
                continue;
            }
            messager.printMessage(NOTE, element.getSimpleName());
            messager.printMessage(NOTE, element.asType().toString());
            ReflectEntity entity = null;
            Map<String, EntityField> fieldMap = new HashMap<>();
            while (element != null && element instanceof Symbol.ClassSymbol && !element.getSimpleName().toString().equals("Object") && !element.getSimpleName().toString().equals("Enum")) {
                EntityScanner scanner = new EntityScanner();
                scanner.scan(element);

                for (EntityField field: scanner.getEntity().getFields()) {
                    if (fieldMap.containsKey(field.getProperties()))
                        continue;
                    fieldMap.put(field.getProperties(), field);
                    if (entity != null) {
                        entity.addField(field);
                    }
                }

                if (entity == null)
                    entity = scanner.getEntity();


                element = ((Symbol.ClassSymbol)element).getSuperclass().asElement();
            }
            if (entity != null) {
                try {
                    JavaFile javaFile = EntityHelperGenerator.generator(entity);
                    javaFile.writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
