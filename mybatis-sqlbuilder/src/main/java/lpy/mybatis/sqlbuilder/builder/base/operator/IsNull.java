package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName ISNULL
 * @date 2021/7/29 4:26 PM
 **/
public class IsNull extends AbstractCommonOperator implements IOperator {

    private final static String IS_NULL=" is null ";


    private IsNull(){}

    public static IOperator isNull(Column column) {
        IsNull isNull = new IsNull();
        isNull.left = column;
        return isNull;
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
        stringBuilder.append(IS_NULL);
        return stringBuilder.toString();
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

}
