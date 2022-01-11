package lpy.mybatis.sqlbuilder.annotation;

import lpy.mybatis.sqlbuilder.util.Entity;

import java.lang.annotation.*;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName PropertiesFormat
 * @date 2021/7/26 1:50 PM
 **/
@Inherited
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PropertiesFormat {
    Entity.PropertiesFormat format() default Entity.PropertiesFormat.NORMAL;
}
