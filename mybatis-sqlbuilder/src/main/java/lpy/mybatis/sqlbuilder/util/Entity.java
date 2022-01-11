package lpy.mybatis.sqlbuilder.util;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Entity
 * @date 2021/7/26 1:29 PM
 **/
@Getter
@Setter
public class Entity {

    private PropertiesFormat format = PropertiesFormat.NORMAL;

    private Class<?> aClass;

    private String primaryKey;

    private String primaryCol;

    private String tableName;

    private ConcurrentHashMap<String, String> propertiesAndColumn = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String,Method> propertiesAndGetMethod = new ConcurrentHashMap<>();

    public enum PropertiesFormat {
        HUMP, NORMAL
    }
}
