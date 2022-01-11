package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.IOperator;
import lpy.mybatis.sqlbuilder.exception.MybatisUtilsException;

import java.util.*;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Or
 * @date 2021/9/17 6:50 下午
 **/
public class Or extends AbstractCommonOperator implements IOperator {
    private final static String OR = " OR ";

    private List<IOperator> list = new ArrayList<>();

    public static IOperator or(IOperator... iOperator) {
        if (iOperator.length == 0)
            throw new MybatisUtilsException("OR 连接时必需要有对应的表达式。");

        Or or = new Or();
        for (IOperator operator : iOperator) {
            if (!operator.isAdd())
                continue;

            or.list.add(operator);
        }
        return or;
    }

    @Override
    public IOperator getNextOperator() {
        return null;
    }

    @Override
    public String build(String keyPrefix, int currentIndex) {
        StringBuilder sb = new StringBuilder();
        if (list != null) {
            sb.append("(");
            Iterator<IOperator> iterator = list.listIterator();
            while (iterator.hasNext()) {
                IOperator iOperator = iterator.next();
                sb.append(iOperator.build(keyPrefix, currentIndex));
                Map<String,Object> param = iOperator.getParams();
                if (param != null) {
                    if (this.params == null)
                        this.params = new HashMap<>();
                    currentIndex += param.size();
                    this.params.putAll(param);
                }
                if (iterator.hasNext()) {
                    sb.append(OR);
                }
            }
            sb.append(")");
        }

        return sb.toString();
    }

    @Override
    public String getOperator() {
        return OR;
    }
}
