package lpy.mybatis.generator.utils;

import com.squareup.javapoet.*;
import lpy.mybatis.generator.entity.EntityField;
import lpy.mybatis.generator.entity.ReflectEntity;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName DaoImplGenerator
 * @date 2021/9/18 12:56 下午
 **/
public class DaoImplGenerator extends AbstractGenerator implements BeanGenerator {

    private ClassName daoClassName;
    private ClassName mapperClassName;

    private String springRepositoryPackage = "org.springframework.stereotype";
    private String springRepositoryName = "Repository";

    private String springAutowiredPackage =  "org.springframework.beans.factory.annotation";
    private String springAutowiredName = "Autowired";



    public DaoImplGenerator(ClassName mapperClassName, ClassName daoClassName, ReflectEntity entity) {
        this.basePackage = daoClassName.packageName();
        this.entity = entity;
        this.mapperClassName = mapperClassName;
        this.daoClassName = daoClassName;
    }

    @Override
    public String getBasePackage() {
        return this.basePackage;
    }

    @Override
    protected JavaFile generator() {
        FieldSpec mapper = FieldSpec.builder(mapperClassName, "mapper", Modifier.PRIVATE)
                .addAnnotation(ClassName.get(springAutowiredPackage, springAutowiredName))
                .build();

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(this.getClassName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get(springRepositoryPackage, springRepositoryName))
                .addSuperinterface(daoClassName)
                .addField(mapper);

        ClassName entityClassName = ClassName.get(entity.getPackageName(), entity.getClassName());
        ParameterizedTypeName listRawEntity = ParameterizedTypeName.get(ClassName.get(List.class), entityClassName);

        MethodSpec save = MethodSpec.methodBuilder("save")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addParameter(entityClassName, "entity")
                .addStatement("return mapper.save(entity)")
                .build();
        typeSpec.addMethod(save);

        MethodSpec saveBatch = MethodSpec.methodBuilder("saveBatch")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addParameter(listRawEntity, "entityList")
                .addStatement("return mapper.saveBatch(entityList)")
                .build();
        typeSpec.addMethod(saveBatch);

        MethodSpec updateById = MethodSpec.methodBuilder("updateById")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addParameter(entityClassName, "entity")
                .addStatement("return mapper.updateById(entity)")
                .build();
        typeSpec.addMethod(updateById);

        MethodSpec deleteById = MethodSpec.methodBuilder("deleteById")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addParameter(entity.getPrimaryKey().getJavaType(), "id")
                .addStatement("$T entity = new $T()", entityClassName, entityClassName)
                .addStatement("entity."+ getPrimaryKeyOfSetMethod(entity.getPrimaryKey()) + "(id)")
                .addStatement("return mapper.deleteById(entity)")
                .build();
        typeSpec.addMethod(deleteById);

        MethodSpec deleteByEntity = MethodSpec.methodBuilder("deleteByEntity")
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addAnnotation(Override.class)
                .addParameter(entityClassName, "entity")
                .addStatement("return mapper.deleteByEntity(entity)")
                .build();
        typeSpec.addMethod(deleteByEntity);

        MethodSpec findAll = MethodSpec.methodBuilder("findAll")
                .addModifiers(Modifier.PUBLIC)
                .returns(listRawEntity)
                .addAnnotation(Override.class)
                .addStatement("return mapper.findAll($T.class)", entityClassName)
                .build();
        typeSpec.addMethod(findAll);

        MethodSpec findById = MethodSpec.methodBuilder("findById")
                .addModifiers(Modifier.PUBLIC)
                .returns(entityClassName)
                .addAnnotation(Override.class)
                .addParameter(entity.getPrimaryKey().getJavaType(), "id")
                .addStatement("$T entity = new $T()", entityClassName, entityClassName)
                .addStatement("entity."+ getPrimaryKeyOfSetMethod(entity.getPrimaryKey()) + "(id)")
                .addStatement("return mapper.findById(entity)")
                .build();
        typeSpec.addMethod(findById);

        MethodSpec findAllByProviderEntity = MethodSpec.methodBuilder("findAllByProviderEntity")
                .addModifiers(Modifier.PUBLIC)
                .returns(listRawEntity)
                .addAnnotation(Override.class)
                .addParameter(entityClassName, "entity")
                .addStatement("return mapper.findAllByProviderEntity(entity)")
                .build();
        typeSpec.addMethod(findAllByProviderEntity);

        MethodSpec findByOne = MethodSpec.methodBuilder("findByOne")
                .addModifiers(Modifier.PUBLIC)
                .returns(entityClassName)
                .addAnnotation(Override.class)
                .addParameter(entityClassName, "entity")
                .addStatement("return mapper.findByOne(entity)")
                .build();
        typeSpec.addMethod(findByOne);

        return JavaFile.builder(this.getPackage(), typeSpec.build()).build();
    }

    @Override
    public String getClassName() {
        return entity.getClassName()+"DaoImpl";
    }

    @Override
    protected String getExclusivePackageName() {
        return "impl";
    }

    @Override
    public String getPackage() {
        if (fullPackage != null)
            return this.fullPackage;
        return this.getBasePackage()+"." + getExclusivePackageName();
    }

    private String getPrimaryKeyOfSetMethod(EntityField field) {
        String key = field.getProperties();
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
    }
}
