package lpy.mybatis.sqlbuilder.util;



import lpy.mybatis.sqlbuilder.annotation.Id;
import lpy.mybatis.sqlbuilder.annotation.PropertiesFormat;
import lpy.mybatis.sqlbuilder.annotation.TableField;
import lpy.mybatis.sqlbuilder.annotation.TableName;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName EntityHelper
 * @date 2021/7/26 1:21 PM
 **/
public class EntityHelper {

    private static Entity.PropertiesFormat format = Entity.PropertiesFormat.NORMAL;

    private static final ConcurrentHashMap<Class<?>, Entity> entityClass = new ConcurrentHashMap<>();

    public static Entity parseClass(Class<?> cla) {
        if (entityClass.containsKey(cla))
            return entityClass.get(cla);
        return createEntity(cla);
    }

    public static void setFormat(Entity.PropertiesFormat format) {
        EntityHelper.format = format;
    }

    private synchronized static Entity createEntity(Class<?> cla) {
        if (entityClass.containsKey(cla))
            return entityClass.get(cla);
        Entity entity = new Entity();
        PropertiesFormat format = cla.getAnnotation(PropertiesFormat.class);
        entity.setFormat(EntityHelper.format);
        if (format != null)
            entity.setFormat(format.format());
        entity.setAClass(cla);
        String tableName = parseTableName(cla, entity);
        entity.setTableName(tableName);
        parseAnnotation(cla, entity);
        entityClass.put(cla, entity);
        return entity;
    }

    private static List<Field> mergeAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        //排除父级元素,可自定义
        while (clazz != null && !Object.class.getName().equals(clazz.getName())) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static void parseAnnotation(Class<?> cla, Entity entity) {
        List<Field> fields = mergeAllFields(cla);
        boolean existIdAnnotation = false;
        for (Field declaredField : fields) {
            declaredField.setAccessible(true);
            if (Modifier.isFinal(declaredField.getModifiers())) {
                continue;
            }
            PropertyDescriptor pd = null;
            Method getMethod = null;
            try {
                pd = new PropertyDescriptor(declaredField.getName(), cla);
                getMethod = pd.getReadMethod();//获得get方法
            } catch (IntrospectionException e) {
                String field = declaredField.getName();
                if (field.startsWith("is")) {
                    try {
                        pd = new PropertyDescriptor(declaredField.getName().substring(2), cla);
                        getMethod = pd.getReadMethod();//获得get方法
                    } catch (IntrospectionException e1) {
                        throw new RuntimeException(e1);
                    }
                } else if (declaredField.getType().getName().equals("java.lang.Boolean")) {
                    try {
                        cla.getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1));
                    } catch (NoSuchMethodException e1) {
                        throw new RuntimeException(e1);
                    }
                } else
                    throw new RuntimeException(e);
            }

            entity.getPropertiesAndGetMethod().put(declaredField.getName(), getMethod);

            Id id = declaredField.getAnnotation(Id.class);
            if (id != null) {
                if (existIdAnnotation)
                    throw new RuntimeException(cla.getName() + "存在多个@Id注解。");
                entity.setPrimaryKey(declaredField.getName());
                TableField tableField = declaredField.getAnnotation(TableField.class);
                if (tableField != null) {
                    entity.setPrimaryCol(tableField.value());
                } else {
                    String col;
                    if (entity.getFormat() == Entity.PropertiesFormat.NORMAL)
                        col = declaredField.getName();
                    else
                        col = underscoreName(declaredField.getName());
                    entity.setPrimaryCol(col);
                }
                existIdAnnotation = true;
                continue;
            }
            TableField tableField = declaredField.getAnnotation(TableField.class);
            if (tableField != null) {
                entity.getPropertiesAndColumn().put(declaredField.getName(), tableField.value());
                continue;
            }
            String col;
            if (entity.getFormat() == Entity.PropertiesFormat.NORMAL)
                col = declaredField.getName();
            else
                col = underscoreName(declaredField.getName());
            entity.getPropertiesAndColumn().put(declaredField.getName(), col);
        }
    }

    private static String parseTableName(Class<?> clazz, Entity entity) {
        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName != null)
            return tableName.value();

        if (entity.getFormat() == Entity.PropertiesFormat.HUMP) {
            return underscoreName(clazz.getSimpleName());
        }
        return clazz.getSimpleName();
    }

    /**
     * 驼峰转下划线连接
     * @param name
     * @return
     */
    private static String underscoreName(String name) {
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


}
