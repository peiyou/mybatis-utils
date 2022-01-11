package lpy.mybatis.sqlbuilder.builder.base;

import lpy.mybatis.sqlbuilder.builder.Query;
import lpy.mybatis.sqlbuilder.util.Entity;
import lpy.mybatis.sqlbuilder.util.EntityHelper;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName MysqlSQLBuilder
 * @date 2021/7/29 1:24 PM
 **/
public class MysqlSQLBuilder implements SQLBuilder {

    @Override
    public String build(Query query) {
        // 组装
        StringBuilder sb = new StringBuilder();
        if (query.isCount()) {
            sb.append("SELECT \n").append(" COUNT(*) ");
        } else {
            sb.append("SELECT \n").append(buildSelector(query));
            String joinSelect = this.buildJoinSelector(query);
            if (joinSelect != null) {
                sb.append(", \n").append(joinSelect);
            }
        }
        sb.append("\n FROM ");
        sb.append(this.buildFromTable(query));
        String join = this.buildJoin(query);
        if (join != null)
            sb.append(join);

        String where = this.buildWhere(query);
        if (where != null) {
            sb.append("\n").append(where);
        }
        String groupBy = this.buildGroupBy(query);
        if (groupBy != null)
            sb.append("\n").append(groupBy);

        String having = this.buildHaving(query);
        if (having != null)
            sb.append("\n").append(having);

        if (!query.isCount()) {
            String orderBy = this.buildOrderBy(query);
            if (orderBy != null)
                sb.append("\n").append(orderBy);
        }

        if (!query.isCount()) {
            String page = this.buildPage(query);
            if (page != null)
                sb.append("\n").append(page);
        }
        query.setSql(sb.toString());
        return sb.toString();
    }


    private String buildSelector(Query query) {
        StringBuilder select = new StringBuilder();
        if ( query.getSelector().size() > 0 ) {
            int index = 1;
            for (Column column : query.getSelector()) {
                if (column.getTableAlias() != null)
                    select.append(column.getTableAlias()).append(".");
                select.append(column.getColumn());
                if (column.getAlias() != null)
                    select.append(" AS ").append(column.getAlias());
                if (index < query.getSelector().size())
                    select.append(", ");
                index++;
            }
        } else {
            // 无group by时
            if (query.getGroupBy().size() == 0)
                select.append(buildEntitySelector(query.getTable(), query.getAlias()));
        }
        // 无group by
        if (query.getTable() == null && query.getGroupBy().size() == 0) {
            select.append("*");
            return select.toString();
        }

        if (query.getGroupBy().size() > 0 && query.getSelector().size() == 0) {
            // 有group by 时
            int index = 1;
            for (Column column: query.getGroupBy()) {
                if (column.getTableAlias() != null)
                    select.append(column.getTableAlias()).append(".");
                select.append(column.getColumn());
                if (column.getAlias() != null)
                    select.append(" AS ").append(column.getAlias());
                if (index < query.getGroupBy().size()) {
                    select.append(", ");
                }
                index ++;
            }
        }
        return select.toString();
    }


    private String buildEntitySelector(Class<?> clazz, String alias) {
        StringBuilder select = new StringBuilder();
        Entity entity = EntityHelper.parseClass(clazz);
        String tableName = entity.getTableName();
        String tableAlias = alias == null ? tableName : alias;
        int index = 1;
        Set<String> keySet = entity.getPropertiesAndColumn().keySet();
        for (String properties: keySet) {
            select.append(tableAlias).append(".").append(entity.getPropertiesAndColumn().get(properties)).append(" AS ").append(properties);
            if (index  < keySet.size())
                select.append(", ");
            index++;
        }
        if (!keySet.contains(entity.getPrimaryKey())) {
            select.append(", ")
                    .append(tableAlias).append(".").append(entity.getPrimaryCol()).append(" AS ").append(entity.getPrimaryKey());
        }
        return select.toString();
    }

    private String buildJoinSelector(Query query) {
        if (query.getJoins().size() > 0 && query.getGroupBy().size() == 0 && query.getSelector().size() == 0) {
            StringBuilder select = new StringBuilder();
            int index = 1;
            for (Query.Join join : query.getJoins()) {
                if (join.getTable() == null)
                    select.append(join.getAlias()).append(".").append("*");
                else
                    select.append(this.buildEntitySelector(join.getTable(), join.getAlias()));

                if (index < query.getJoins().size()) {
                    select.append(", \n");
                }
                index++;
            }
            return select.toString();
        }
        return null;
    }

    private String buildFromTable(Query query) {
        StringBuilder from = new StringBuilder();
        if (query.getTable() == null) {
            from.append(" (");
            from.append(this.build(query.getQuery()));
            from.append(" ) AS ").append(query.getAlias());
        } else {
            Entity entity = EntityHelper.parseClass(query.getTable());
            from.append(entity.getTableName());
            if (query.getAlias() != null)
                from.append(" AS ").append(query.getAlias());
        }
        return from.toString();
    }

    /**
     * @param query
     * @return
     */
    private String buildJoin(Query query) {
        if (query.getJoins().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            int index = 1;
            for (Query.Join join : query.getJoins()) {
                stringBuilder.append(join.getJoinType());
                // join 子查询的情况
                if (join.getQuery() != null) {
                    stringBuilder.append("(");
                    join.getQuery().setKeyPrefix(query.getKeyPrefix() + Query.ARGUMENT_SUB_PREFIX);
                    stringBuilder.append(this.build(join.getQuery()));
                    stringBuilder.append(") AS ").append(join.getAlias());
                    this.setParams(query, join.getQuery().getParamsMap());
                } else {
                    // join 表的情况
                    Entity entity = EntityHelper.parseClass(join.getTable());
                    stringBuilder.append(entity.getTableName());
                    if (join.getAlias() != null) {
                        stringBuilder.append(" AS ").append(join.getAlias());
                    }
                }
                // 有ON时
                if  (join.getOperator() != null && join.getOperator().isAdd()) {
                    stringBuilder.append(" ON ");
                    stringBuilder.append(join.getOperator().build(query.getKeyPrefix(), query.getParamsMap().size()));
                    this.setParams(query, join.getOperator().getParams());
                }
                if (index < query.getJoins().size()) {
                    stringBuilder.append("\n");
                }
                index++;
            }
            return stringBuilder.toString();
        }
        return null;

    }

    private String buildWhere(Query query) {
        if (query.getLogicSigns().size() > 0) {
            StringBuilder where = new StringBuilder();
            String whereStr = this.buildCondition(query, query.getLogicSigns(), where);
            if (whereStr.length() > 0)
                return " WHERE " + whereStr;
        }
        return null;
    }

    private String buildGroupBy(Query query) {
        if (query.getGroupBy().size() > 0) {
            StringBuilder groupBy = new StringBuilder();
            groupBy.append(" GROUP BY ");
            int index = 1;
            for (Column column: query.getGroupBy()) {
                if (column.getTableAlias() != null)
                    groupBy.append(column.getTableAlias()).append(".");
                groupBy.append(column.getColumn());
                if (index < query.getGroupBy().size()) {
                    groupBy.append(", ");
                }
                index++;
            }
            return groupBy.toString();
        }

        return null;
    }

    private String buildOrderBy(Query query) {
        if (query.getOrderBy().size() > 0) {
            StringBuilder orderBy = new StringBuilder();
            orderBy.append(" ORDER BY ");
            int index = 1;
            for (Column column: query.getOrderBy()) {
                if (column.getTableAlias() != null)
                    orderBy.append(column.getTableAlias()).append(".");
                orderBy.append(column.getColumn());
                orderBy.append(column.getSort());
                if (index < query.getOrderBy().size()) {
                    orderBy.append(", ");
                }
                index++;
            }
            return orderBy.toString();
        }

        return null;
    }

    private String buildHaving(Query query) {
        if (query.getHaving().size() > 0) {
            StringBuilder where = new StringBuilder();
            String whereStr = this.buildCondition(query, query.getHaving(), where);
            if (whereStr.length() > 0)
                return "HAVING " + whereStr;
        }
        return null;
    }

    private String buildPage(Query query) {
        if (query.getPage() != null && query.getPage().getSize() > 0) {
            return " limit " + ((query.getPage().getStart() - 1) * query.getPage().getSize()) + ", " + query.getPage().getSize();
        }
        return null;
    }

    private String buildCondition(Query query, Queue<Query.LogicSign> queue, StringBuilder where) {
            int index = 1;
            for (Query.LogicSign sign : queue) {
                if (!sign.getOperator().isAdd())
                    continue;
                if (index > 1)
                    where.append(sign.getSign());
                where.append(sign.getOperator().build(query.getKeyPrefix(), query.getParamsMap().size()));
                this.setParams(query, sign.getOperator().getParams());
                where.append(" ");
                index ++;
            }
            return where.toString();
    }

    private void setParams(Query query, Map<String, Object> params) {
        if (params!= null && params.size() > 0) {
            query.getParamsMap().putAll(params);
        }
    }
}
