package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.IOperator;

import java.util.Map;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName StringSQL
 * @date 2021/7/29 10:50 AM
 **/
public class StringSQL extends AbstractCommonOperator implements IOperator {

    private String sql;


    public StringSQL(String sql) {
        this.sql = sql;
    }

    public StringSQL(String sql, Map<String, Object> params) {
        this.sql = sql;
        this.params = params;
    }

    @Override
    public String build(String keyPrefix,int currentIndex) {
        return sql;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return "";
    }
}
