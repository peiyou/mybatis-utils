package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName AbstractCommonOperator
 * @date 2021/7/29 3:58 PM
 **/
public abstract class AbstractCommonOperator implements IOperator {

    protected Column left;

    protected Column right;

    protected Object param;

    protected Map<String, Object> params;

    protected IOperator nextOperator;

    @Override
    public String build(String keyPrefix,int currentIndex) {
        // column = ?
        StringBuilder stringBuilder = new StringBuilder();
        if (right == null) {
            if (left.getTableAlias() != null) {
                stringBuilder.append(left.getTableAlias()).append(".");
            }
            String key = keyPrefix + "_" + (currentIndex + 1);
            stringBuilder.append(left.getColumn())
                    .append(this.getOperator())
                    .append("#{").append(key).append("}");
            if (params == null)
                params = new HashMap<>();
            params.put(key, param);
        } else {
            // t1.id = t2.id
            if (left.getTableAlias() != null) {
                stringBuilder.append(left.getTableAlias()).append(".");
            }
            stringBuilder.append(left.getColumn())
                    .append(this.getOperator());
            if (right.getTableAlias() != null) {
                stringBuilder.append(right.getTableAlias()).append(".");
            }
            stringBuilder.append(right.getColumn());

        }
        return stringBuilder.toString();
    }

    public abstract String getOperator();

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public boolean isAdd() {
        return true;
    }
}
