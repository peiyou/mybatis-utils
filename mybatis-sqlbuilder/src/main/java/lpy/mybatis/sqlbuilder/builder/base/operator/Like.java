package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;
import lpy.mybatis.sqlbuilder.builder.base.SQLBuilderFactory;
import lpy.mybatis.sqlbuilder.exception.MybatisUtilsException;

import java.util.HashMap;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Like
 * @date 2021/9/27 3:17 下午
 **/
public class Like extends AbstractCommonOperator implements IOperator {
    private static final String LIKE = " LIKE ";
    private boolean isAdd = true;

    private boolean leftLike = false;

    private boolean rightLike = false;

    public static IOperator like(Column column, Object param, boolean leftLike, boolean rightLike, boolean isAdd) {
        Like like = new Like();
        like.left = column;
        like.param = param;
        like.leftLike = leftLike;
        like.rightLike = rightLike;
        like.isAdd = isAdd;
        return like;
    }

    @Override
    public boolean isAdd() {
        return isAdd;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return LIKE;
    }

    @Override
    public String build(String keyPrefix, int currentIndex) {
        if (param == null)
            throw new MybatisUtilsException("like操作时，参数[param]不能为空。[" + left.getTableAlias() + "." + left.getColumn() + " like " + (leftLike ? "%": "") + "#{param}" + (rightLike ? "%": ""));

        StringBuilder stringBuilder = new StringBuilder();
        if (SQLBuilderFactory.getDatabaseName() == SQLBuilderFactory.DatabaseName.MYSQL) {
            String key = keyPrefix + "_" + (currentIndex + 1);
            stringBuilder.append(left.getColumn())
                    .append(this.getOperator())
                    .append(" concat(");
            if (leftLike)
                stringBuilder.append("'%',");
            stringBuilder.append("#{").append(key).append("}");
            if (rightLike)
                stringBuilder.append(",'%'");
            stringBuilder.append(") ");
            if (params == null)
                params = new HashMap<>();

            params.put(key, param);
        } else if (SQLBuilderFactory.getDatabaseName() == SQLBuilderFactory.DatabaseName.ORACLE) {

        } else {

        }
        return stringBuilder.toString();
    }
}
