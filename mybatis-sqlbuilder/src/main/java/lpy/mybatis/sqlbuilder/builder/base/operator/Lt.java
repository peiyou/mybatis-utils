package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Lt
 * @date 2021/7/29 4:09 PM
 **/
public class Lt extends AbstractCommonOperator implements IOperator {

    private static final String LT = " < ";

    private boolean isAdd = true;

    private Lt(){}

    public static IOperator lt(Column column, Object param) {
        Lt lt = new Lt();
        lt.param = param;
        lt.left = column;
        return lt;
    }

    public static IOperator lt(Column column, Object param, boolean isAdd) {
        Lt lt = new Lt();
        lt.param = param;
        lt.left = column;
        lt.isAdd = isAdd;
        return lt;
    }

    public static IOperator lt(Column left, Column right) {
        Lt lt = new Lt();
        lt.left = left;
        lt.right = right;
        return lt;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return LT;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }
}
