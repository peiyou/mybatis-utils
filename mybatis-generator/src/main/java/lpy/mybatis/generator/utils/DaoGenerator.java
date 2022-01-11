package lpy.mybatis.generator.utils;

import com.squareup.javapoet.*;
import lpy.mybatis.generator.entity.ReflectEntity;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName DaoGenerator
 * @date 2021/9/18 10:50 上午
 **/
public class DaoGenerator extends AbstractGenerator implements BeanGenerator {

    public DaoGenerator(ReflectEntity entity) {
        this.basePackage = entity.getPackageName().substring(0, entity.getPackageName().lastIndexOf("."));
        this.entity = entity;
    }

    public DaoGenerator(String daoPackage, ReflectEntity entity) {
        this.fullPackage = daoPackage;
        this.entity = entity;
    }

    @Override
    public String getBasePackage() {
        return this.basePackage;
    }

    @Override
    protected JavaFile generator() {
        TypeSpec.Builder typeSpec = TypeSpec.interfaceBuilder(this.getClassName())
                .addModifiers(Modifier.PUBLIC);
        ClassName entityClassName = ClassName.get(entity.getPackageName(), entity.getClassName());
        ParameterizedTypeName listRawEntity = ParameterizedTypeName.get(ClassName.get(List.class), entityClassName);

        MethodSpec save = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int.class)
                .addParameter(ClassName.get(entity.getPackageName(), entity.getClassName()), "entity")
                .build();
        typeSpec.addMethod(save);

        MethodSpec saveBatch = MethodSpec.methodBuilder("saveBatch")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int.class)
                .addParameter(listRawEntity, "entityList")
                .build();
        typeSpec.addMethod(saveBatch);

        MethodSpec updateById = MethodSpec.methodBuilder("updateById")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int.class)
                .addParameter(entityClassName, "entity")
                .build();
        typeSpec.addMethod(updateById);

        MethodSpec deleteById = MethodSpec.methodBuilder("deleteById")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int.class)
                .addParameter(entity.getPrimaryKey().getJavaType(), "id")
                .build();
        typeSpec.addMethod(deleteById);

        MethodSpec deleteByEntity = MethodSpec.methodBuilder("deleteByEntity")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(int.class)
                .addParameter(ClassName.get(entity.getPackageName(), entity.getClassName()), "entity")
                .build();
        typeSpec.addMethod(deleteByEntity);


        MethodSpec findAll = MethodSpec.methodBuilder("findAll")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(listRawEntity)
                .build();
        typeSpec.addMethod(findAll);

        MethodSpec findById = MethodSpec.methodBuilder("findById")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(entityClassName)
                .addParameter(entity.getPrimaryKey().getJavaType(), "id")
                .build();
        typeSpec.addMethod(findById);


        MethodSpec findAllByProviderEntity = MethodSpec.methodBuilder("findAllByProviderEntity")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(listRawEntity)
                .addParameter(entityClassName, "entity")
                .build();
        typeSpec.addMethod(findAllByProviderEntity);

        MethodSpec findByOne = MethodSpec.methodBuilder("findByOne")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(entityClassName)
                .addParameter(entityClassName, "entity")
                .build();
        typeSpec.addMethod(findByOne);
        return JavaFile.builder(this.getPackage(), typeSpec.build()).build();
    }

    @Override
    public String getClassName() {
        return entity.getClassName()+"Dao";
    }

    @Override
    protected String getExclusivePackageName() {
        return "dao";
    }

    @Override
    public String getPackage() {
        if (fullPackage != null)
            return this.fullPackage;
        return this.getBasePackage()+"." + this.getExclusivePackageName();
    }
}
