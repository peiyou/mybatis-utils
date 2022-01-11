package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName IsNotNull
 * @date 2021/7/29 4:30 PM
 **/
public class IsNotNull extends AbstractCommonOperator implements IOperator {
    private final static String IS_NOT_NULL=" is not null ";

    private IsNotNull(){}

    public static IOperator isNotNull(Column column) {
        IsNotNull isNotNull = new IsNotNull();
        isNotNull.left = column;
        return isNotNull;
    }

    @Override
    public String getOperator() {
        return null;
    }

    @Override
    public String build(String keyPrefix,int currentIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        if (left.getTableAlias() != null) {
            stringBuilder.append(left.getTableAlias()).append(".");
        }
        stringBuilder.append(left.getColumn());
        stringBuilder.append(IS_NOT_NULL);
        return stringBuilder.toString();
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }


}
