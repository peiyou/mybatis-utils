package lpy.mybatis.generator.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import lpy.mybatis.generator.entity.ReflectEntity;
import lpy.mybatis.sqlbuilder.BaseMapper;

import javax.lang.model.element.Modifier;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName MapperGenerator
 * @date 2021/9/18 10:50 上午
 **/
public class MapperGenerator extends AbstractGenerator implements BeanGenerator {

    public MapperGenerator(ReflectEntity entity) {
        this.basePackage = entity.getPackageName().substring(0, entity.getPackageName().lastIndexOf("."));
        this.entity = entity;
    }

    public MapperGenerator(String mapperPackage ,ReflectEntity entity) {
        this.entity = entity;
        this.fullPackage = mapperPackage;
    }

    @Override
    public String getBasePackage() {
        return this.basePackage;
    }

    @Override
    protected JavaFile generator() {
        ClassName baseMapper = ClassName.get(BaseMapper.class);

        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(this.getClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(baseMapper, ClassName.get(entity.getPackageName(), entity.getClassName())));
        TypeSpec mapper = builder.build();
        JavaFile javaFile = JavaFile.builder(this.getMapperPackage(), mapper)
                .build();
        return javaFile;
    }

    @Override
    public String getClassName() {
        return entity.getClassName() + "Mapper";
    }

    @Override
    protected String getExclusivePackageName() {
        return "mapper";
    }


    @Override
    public String getPackage() {
        return this.getMapperPackage();
    }

    public String getMapperPackage() {
        if (fullPackage != null)
            return fullPackage;
        return this.basePackage + "." + this.getExclusivePackageName();
    }
}
