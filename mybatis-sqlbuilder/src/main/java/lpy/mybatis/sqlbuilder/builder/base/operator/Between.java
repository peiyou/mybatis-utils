package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Between
 * @date 2021/9/18 10:17 上午
 **/
public class Between extends AbstractCommonOperator implements IOperator {

    private final static String BETWEEN = " BETWEEN ";

    private boolean isAdd = true;

    private Object startParam;

    private Object endParam;

    private Between() {}

    public static IOperator between(Column column, Object startParam, Object endParam) {
        Between between = new Between();
        between.left = column;
        between.startParam = startParam;
        between.endParam = endParam;
        return between;
    }

    public static IOperator between(Column column, Object startParam, Object endParam, boolean isAdd) {
        Between between = new Between();
        between.left = column;
        between.startParam = startParam;
        between.endParam = endParam;
        between.isAdd = isAdd;
        return between;
    }

    @Override
    public String build(String keyPrefix, int currentIndex) {
        StringBuilder stringBuilder = new StringBuilder();

        if (left.getTableAlias() != null) {
            stringBuilder.append(left.getTableAlias()).append(".");
        }
        String startKey = keyPrefix + "_" + (currentIndex + 1);
        String endKey = keyPrefix + "_" + (currentIndex + 2);
        stringBuilder.append(left.getColumn())
                .append(this.getOperator())
                .append("#{").append(startKey).append("}")
                .append(" AND ")
                .append("#{").append(endKey).append("}");
        if (params == null)
            params = new HashMap<>();
        params.put(startKey, startParam);
        params.put(endKey, endParam);
        return stringBuilder.toString();
    }

    @Override
    public String getOperator() {
        return BETWEEN;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }
}
