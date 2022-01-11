package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName In
 * @date 2021/8/9 10:57 AM
 **/
public class In extends AbstractCommonOperator implements IOperator {

    private static final String IN = " IN ";

    private List<Object> paramList;

    public static <T> IOperator in(Column column, List<T> param) {
        if (param == null)
            return new In();
        In in = new In();
        in.paramList = new ArrayList<>(param.size());
        in.paramList.addAll(param);
        in.left = column;
        return in;
    }

    @Override
    public String build(String keyPrefix, int currentIndex) {
        if (paramList == null || paramList.size() == 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        if (left.getTableAlias() != null) {
            stringBuilder.append(left.getTableAlias()).append(".");
        }
        stringBuilder.append(left.getColumn())
                .append(this.getOperator());
        stringBuilder.append("(");
        int len = this.paramList.size();
        for (Object obj: paramList) {
            String key = keyPrefix + "_IN_" + (currentIndex + 1);
            currentIndex++;
            if (params == null)
                params = new HashMap<>();
            params.put(key, obj);
            stringBuilder.append("#{").append(key).append("}");
            if (len > 1)
                stringBuilder.append(",");
            len --;
        }
        stringBuilder.append(") ");
        return stringBuilder.toString();
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String getOperator() {
        return IN;
    }
}
