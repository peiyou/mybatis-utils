package lpy.mybatis.generator.utils;

import com.squareup.javapoet.TypeName;
import lpy.mybatis.generator.entity.EntityField;
import lpy.mybatis.generator.entity.ReflectEntity;
import lpy.mybatis.sqlbuilder.util.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName EntityGeneratorUtils
 * @date 2021/9/18 2:24 下午
 **/
public class EntityGeneratorUtils {

    public static ReflectEntity transfer(Entity entity) {
        ReflectEntity reflectEntity = new ReflectEntity();
        List<EntityField> entityFields = new ArrayList<>();
        for (String properties: entity.getPropertiesAndColumn().keySet()) {
            EntityField entityField = new EntityField();
            entityField.setProperties(properties);
            entityField.setColumn(entity.getPropertiesAndColumn().get(properties));
            entityField.setJavaType(TypeName.get(entity.getPropertiesAndGetMethod().get(properties).getReturnType()));
            entityFields.add(entityField);
        }

        if (entity.getPrimaryKey() != null) {
            EntityField id = new EntityField();
            id.setPrimary(true);
            id.setProperties(entity.getPrimaryKey());
            id.setColumn(entity.getPrimaryKey());
            id.setJavaType(TypeName.get(entity.getPropertiesAndGetMethod().get(entity.getPrimaryKey()).getReturnType()));
            reflectEntity.setPrimaryKey(id);
            entityFields.add(id);
        }

        reflectEntity.setTableName(entity.getTableName());
        reflectEntity.setClassName(entity.getAClass().getPackage().getName(), entity.getAClass().getSimpleName());
        return reflectEntity;
    }
}
