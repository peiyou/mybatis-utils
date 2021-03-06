package lpy.mybatis.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName MybatisGeneratorHelper
 * @date 2021/9/16 11:01 上午
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MybatisGeneratorHelper {
}
