package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Gt
 * @date 2021/7/29 3:51 PM
 **/
public class Gt extends AbstractCommonOperator implements IOperator {

    private static final String GT = " > ";

    private boolean isAdd = true;

    private Gt(){}

    public static IOperator gt(Column column, Object param) {
        Gt gt = new Gt();
        gt.param = param;
        gt.left = column;
        return gt;
    }

    public static IOperator gt(Column column, Object param, boolean isAdd) {
        Gt gt = new Gt();
        gt.param = param;
        gt.left = column;
        gt.isAdd = isAdd;
        return gt;
    }

    public static IOperator gt(Column left, Column right) {
        Gt gt = new Gt();
        gt.left = left;
        gt.right = right;
        return gt;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return GT;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }
}
