package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Le
 * @date 2021/7/29 4:10 PM
 **/
public class Le extends AbstractCommonOperator implements IOperator {
    private static final String LE = " <= ";

    private boolean isAdd = true;

    private Le(){}

    public static IOperator le(Column column, Object param) {
        Le le = new Le();
        le.param = param;
        le.left = column;
        return le;
    }

    public static IOperator le(Column left, Object param, boolean isAdd) {
        Le le = new Le();
        le.left = left;
        le.param = param;
        le.isAdd = isAdd;
        return le;
    }

    public static IOperator le(Column left, Column right) {
        Le le = new Le();
        le.right = right;
        le.left = left;
        return le;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return LE;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }
}
