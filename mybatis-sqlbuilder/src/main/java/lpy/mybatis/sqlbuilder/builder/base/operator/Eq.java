package lpy.mybatis.sqlbuilder.builder.base.operator;

import lombok.Getter;
import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Eq
 * @date 2021/7/27 5:20 PM
 **/
@Getter
public class Eq extends AbstractCommonOperator implements IOperator {

    private static final String EQ = " = ";

    private boolean isAdd = true;

    private Eq() {

    }

    public static IOperator eq(Column column, Object param) {
        Eq eq = new Eq();
        eq.param = param;
        eq.left = column;
        return eq;
    }

    public static IOperator eq(Column column, Object param, boolean isAdd) {
        Eq eq = new Eq();
        eq.param = param;
        eq.left = column;
        eq.isAdd = isAdd;
        return eq;
    }

    public static IOperator eq(Column left, Column right) {
        Eq eq = new Eq();
        eq.left = left;
        eq.right = right;
        return eq;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }


    @Override
    public String getOperator() {
        return EQ;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }
}
