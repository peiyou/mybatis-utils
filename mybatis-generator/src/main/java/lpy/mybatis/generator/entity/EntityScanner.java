package lpy.mybatis.generator.entity;

import com.squareup.javapoet.ClassName;
import lombok.Getter;
import lpy.mybatis.generator.annotation.MybatisGeneratorHelper;
import lpy.mybatis.sqlbuilder.annotation.Id;
import lpy.mybatis.sqlbuilder.annotation.TableField;
import lpy.mybatis.sqlbuilder.annotation.TableName;
import lpy.mybatis.sqlbuilder.util.Entity;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner8;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName EntityScanner
 * @date 2021/9/16 5:11 下午
 **/
public class EntityScanner extends ElementScanner8<Void, Void> {


    public EntityScanner() {
        this.entity = new ReflectEntity();
    }

    @Getter
    private ReflectEntity entity;

    @Override
    public Void visitType(TypeElement e, Void unused) {
        MybatisGeneratorHelper helper = e.getAnnotation(MybatisGeneratorHelper.class);
        if (helper == null)
            return super.visitType(e, unused);
        ClassName className = this.getClassName(e.getQualifiedName().toString());
        this.entity.setClassName(className.packageName(), className.simpleName());
        TableName tableName = e.getAnnotation(TableName.class);
        if (tableName != null)
            entity.setTableName(tableName.value());
        else if (entity.getFormat() == Entity.PropertiesFormat.HUMP) {
            entity.setTableName(this.underscoreName(entity.getClassName()));
        }
        return super.visitType(e, unused);
    }

    @Override
    public Void visitVariable(VariableElement element, Void unused) {
        if (element.getKind() != ElementKind.FIELD ||
                element.getModifiers().contains(Modifier.STATIC) ||
                element.getModifiers().contains(Modifier.TRANSIENT) ||
                element.getModifiers().contains(Modifier.FINAL)) {
            return super.visitVariable(element, unused);
        }
        String fieldName = element.getSimpleName().toString();
        EntityField field = this.parseField(fieldName, element);
        entity.addField(field);
        if (field.isPrimary())
            entity.setPrimaryKey(field);
        return super.visitVariable(element, unused);
    }

    private ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }

    /**
     * 驼峰转下划线连接
     * @param name
     * @return
     */
    private String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成小写
            result.append(name.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                result.append(s.toLowerCase());
            }
        }
        return result.toString();
    }

    private EntityField parseField(String fieldName, VariableElement var) {
        EntityField field = new EntityField();
        TableField tableField = var.getAnnotation(TableField.class);
        field.setProperties(fieldName);
        field.setJavaType(ClassName.get(var.asType()));
        if (tableField == null) {
            String column = entity.getFormat() == Entity.PropertiesFormat.HUMP ? this.underscoreName(fieldName) : fieldName;
            field.setColumn(column);
        } else {
            field.setColumn(tableField.value());
        }
        Id id = var.getAnnotation(Id.class);
        if (id != null)
            field.setPrimary(true);
        return field;
    }
}
