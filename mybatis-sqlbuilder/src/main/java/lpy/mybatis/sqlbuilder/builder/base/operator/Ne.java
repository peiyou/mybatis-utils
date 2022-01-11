package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Ne
 * @date 2021/7/29 4:07 PM
 **/
public class Ne extends AbstractCommonOperator implements IOperator {
    private static final String NE = " <> ";

    private boolean isAdd = true;

    private Ne(){}

    public static IOperator ne(Column column, Object param) {
        Ne ne = new Ne();
        ne.param = param;
        ne.left = column;
        return ne;
    }

    public static IOperator ne(Column column, Object param, boolean isAdd) {
        Ne ne = new Ne();
        ne.param = param;
        ne.left = column;
        ne.isAdd = isAdd;
        return ne;
    }

    public static IOperator ne(Column left, Column right) {
        Ne ne = new Ne();
        ne.left = left;
        ne.right = right;
        return ne;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return NE;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }
}
