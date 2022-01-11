package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Ge
 * @date 2021/7/29 4:03 PM
 **/
public class Ge extends AbstractCommonOperator implements IOperator {

    private static final String GE = " >= ";

    private boolean isAdd = true;

    private Ge(){}

    public static IOperator ge(Column column, Object param) {
        Ge ge = new Ge();
        ge.param = param;
        ge.left = column;
        return ge;
    }

    public static IOperator ge(Column column, Object param, boolean isAdd) {
        Ge ge = new Ge();
        ge.param = param;
        ge.left = column;
        ge.isAdd = isAdd;
        return ge;
    }

    public static IOperator ge(Column left, Column right) {
        Ge ge = new Ge();
        ge.left = left;
        ge.right = right;
        return ge;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return GE;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }
}
