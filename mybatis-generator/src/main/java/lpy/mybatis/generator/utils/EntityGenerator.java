package lpy.mybatis.generator.utils;

import com.squareup.javapoet.*;
import lombok.Getter;
import lombok.Setter;
import lpy.mybatis.generator.annotation.MybatisGeneratorHelper;
import lpy.mybatis.generator.entity.EntityField;
import lpy.mybatis.generator.entity.ReflectEntity;
import lpy.mybatis.sqlbuilder.annotation.Id;
import lpy.mybatis.sqlbuilder.annotation.TableField;
import lpy.mybatis.sqlbuilder.annotation.TableName;

import javax.lang.model.element.Modifier;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName EntityGenerator
 * @date 2021/9/18 10:50 上午
 **/
public class EntityGenerator extends AbstractGenerator implements BeanGenerator {

    public EntityGenerator(String packageName, ReflectEntity entity, boolean isFullPackage) {
        if (isFullPackage)
            this.fullPackage = packageName;
        else
            this.basePackage = packageName;
        this.entity = entity;
    }

    @Override
    public String getBasePackage() {
        return this.basePackage;
    }

    @Override
    protected JavaFile generator() {
        AnnotationSpec tableName = AnnotationSpec.builder(TableName.class)
                .addMember("value", "$S", entity.getTableName()).build();

        TypeSpec.Builder klassBuild = TypeSpec.classBuilder(ClassName.get(this.getEntityPackage(), entity.getClassName()))
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(MybatisGeneratorHelper.class)
                .addAnnotation(tableName)
                .addAnnotation(ClassName.get(Getter.class))
                .addAnnotation(ClassName.get(Setter.class));
        for (EntityField field: entity.getFields()) {

            AnnotationSpec tableField = AnnotationSpec.builder(TableField.class)
                    .addMember("value", "$S", field.getColumn()).build();

            FieldSpec.Builder fieldSpec = FieldSpec.builder(field.getJavaType(), field.getProperties(), Modifier.PRIVATE)
                    .addAnnotation(tableField);
            if (field.isPrimary()) {
                fieldSpec.addAnnotation(Id.class);
            }
            fieldSpec.addJavadoc(field.getComment());
            klassBuild.addField(fieldSpec.build());
        }
        return JavaFile.builder(getEntityPackage(), klassBuild.build()).build();
    }

    public String getEntityPackage() {
        if (fullPackage != null)
            return this.fullPackage;
        return this.getBasePackage() + "." + getExclusivePackageName();
    }

    @Override
    public String getClassName() {
        return entity.getClassName();
    }

    @Override
    public String getPackage() {
        return this.getEntityPackage();
    }

    @Override
    protected String getExclusivePackageName() {
        return "entity";
    }
}
