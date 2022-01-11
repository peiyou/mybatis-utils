package lpy.mybatis.sqlbuilder.util;

import lombok.Getter;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName BaseEntityHelper
 * @date 2021/9/16 1:33 下午
 **/
public class BaseEntityHelper {

    @Getter
    private Class<?> sourceClass;

    @Getter
    private String tableAlias;

    public BaseEntityHelper(Class<?> sourceClass, String tableAlias) {
        this.sourceClass = sourceClass;
        this.tableAlias = tableAlias;
    }
}
