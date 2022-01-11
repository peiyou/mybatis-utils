package lpy.mybatis.sqlbuilder.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName TableField
 * @date 2021/7/26 12:38 PM
 **/
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TableField {

    String value();
}
