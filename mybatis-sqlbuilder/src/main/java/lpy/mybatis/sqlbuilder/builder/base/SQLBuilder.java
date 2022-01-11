package lpy.mybatis.sqlbuilder.builder.base;

import lpy.mybatis.sqlbuilder.builder.Query;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName SQLBuilder
 * @date 2021/7/27 5:23 PM
 **/
public interface SQLBuilder {
    String build(Query query);
}
