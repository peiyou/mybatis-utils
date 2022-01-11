package lpy.mybatis.sqlbuilder.builder.base;

import java.util.Map;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Operator
 * @date 2021/7/26 6:11 PM
 **/
public interface IOperator {

    String build(String keyPrefix, int currentIndex);

    IOperator getNextOperator();

    Map<String, Object> getParams();

    boolean isAdd();
}
