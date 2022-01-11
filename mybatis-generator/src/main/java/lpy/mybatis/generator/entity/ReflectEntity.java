package lpy.mybatis.generator.entity;

import com.squareup.javapoet.ClassName;
import lombok.Getter;
import lombok.Setter;
import lpy.mybatis.sqlbuilder.util.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName ReflectEntity
 * @date 2021/9/16 4:54 下午
 **/
@Getter
public class ReflectEntity {

    private Entity.PropertiesFormat format = Entity.PropertiesFormat.NORMAL;

    private String className;

    private String packageName;

    private String tableName;

    @Setter
    private EntityField primaryKey;

    private List<EntityField> fields = new ArrayList<>();

    public ClassName entity() {
        return ClassName.get(this.packageName, this.className);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setClassName(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    public void addField(EntityField field) {
        this.fields.add(field);
    }
}
