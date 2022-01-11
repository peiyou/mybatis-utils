package lpy.mybatis.generator;

import com.squareup.javapoet.*;
import lpy.mybatis.generator.entity.EntityField;
import lpy.mybatis.generator.entity.ReflectEntity;
import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.util.BaseEntityHelper;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName EntityHelperGenerator
 * @date 2021/9/16 2:37 下午
 **/
public class EntityHelperGenerator {

    public static JavaFile generator(ReflectEntity entity) throws IOException {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "tableAlias")
                .addCode("this($T.class, tableAlias);", entity.entity())
                .build();

        MethodSpec privateConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(Class.class, "sourceClass")
                .addParameter(String.class, "tableAlias")
                .addStatement("super(sourceClass, tableAlias)")
                .build();

        String className = entity.getClassName() + "Helper";
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructor)
                .addMethod(privateConstructor);
        // Entity entity = EntityHelper.parseClass(classes);
        FieldSpec fieldAll = FieldSpec.builder(ParameterizedTypeName.get(List.class, Column.class), "fieldAll", Modifier.PRIVATE)
                .addModifiers(Modifier.FINAL)
                .initializer("new $T<>()", ArrayList.class).build();


        CodeBlock.Builder initializerBlock = CodeBlock.builder();

        MethodSpec.Builder all = MethodSpec.methodBuilder("selectAllColumn")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(List.class, Column.class))
                .addStatement("return fieldAll");
        builder.addMethod(all.build());

        for (EntityField field : entity.getFields()) {
            MethodSpec method = MethodSpec.methodBuilder(field.getProperties())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(Column.class)
                    .addStatement("$T column = new $T($S)", Column.class, Column.class, field.getColumn())
                    .addStatement("column.setAlias($S)", field.getProperties())
                    .addStatement("column.setTableAlias(this.getTableAlias())")
                    .addStatement("return column")
                    .build();
            initializerBlock.addStatement("fieldAll.add(this."+ field.getProperties() +"())");
            builder.addMethod(method);

        }

        builder.addField(fieldAll);
        builder.addInitializerBlock(initializerBlock.build());
        builder.superclass(BaseEntityHelper.class);
        TypeSpec help = builder.build();

        JavaFile javaFile = JavaFile.builder(entity.getPackageName() + ".helper", help)
                .build();
        // javaFile.writeTo(System.out);
        return javaFile;
    }
}
