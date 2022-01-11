package lpy.mybatis.sqlbuilder.builder;

import lombok.Getter;
import lombok.Setter;
import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;
import lpy.mybatis.sqlbuilder.builder.base.SQLBuilderFactory;
import lpy.mybatis.sqlbuilder.util.BaseEntityHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Query
 * @date 2021/7/26 5:50 PM
 **/
@Getter
public class Query {

    public static final String QUERY_PARAM = "QUERY_PARAM";

    public static final String ARGUMENT_PREFIX = "QUERY_ARGUMENT";

    public static final String ARGUMENT_SUB_PREFIX = "SUB";

    @Setter
    private String keyPrefix = ARGUMENT_PREFIX;

    private Class<?> table;

    private String alias;

    private Query query;

    private Page page;

    @Setter
    private Map<String, Object> paramsMap = new HashMap<>();

    @Setter
    private String sql;

    @Getter
    @Setter
    private boolean count = false;

    public void build() {
        if (this.sql == null) {
            SQLBuilderFactory.initSQLBuilder().build(this);
        }
    }

    public String getSql() {
        if (this.sql == null) {
            SQLBuilderFactory.initSQLBuilder().build(this);
        }
        return this.sql;
    }
    /**
     * 条件
     */
    private ConcurrentLinkedQueue<LogicSign> logicSigns = new ConcurrentLinkedQueue<>();

    /**
     * 查询字段
     */
    private CopyOnWriteArrayList<Column> selector = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<Join> joins = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<Column> groupBy = new CopyOnWriteArrayList<>();

    private CopyOnWriteArrayList<Column> orderBy = new CopyOnWriteArrayList<>();

    private ConcurrentLinkedQueue<LogicSign> having = new ConcurrentLinkedQueue<>();

    public <T extends BaseEntityHelper> Query(T entity) {
        this(entity.getSourceClass(), entity.getTableAlias());
    }

    private Query(Class<?> table, String alias) {
        this.table = table;
        this.alias = alias;
    }

    private Query(Class<?> table) {
        this(table, null);
    }

    public Query(Query query, String alias) {
        this.query = query;
        this.alias = alias;
    }

    public Query select(Column... columns) {
        selector.addAll(Arrays.asList(columns));
        return this;
    }

    public Query select(String...columns) {
        if (columns == null)
            throw new NullPointerException("不能存在 'select(null)' ");
        for (String s : columns) {
            if (s == null)
                throw new NullPointerException("不能存在 'select(null)' ");
            else
                selector.add(new Column(s));
        }
        return this;
    }

    public Query select(List<Column> all, Column...columns2) {
        if (all != null && all.size() > 0)
            selector.addAll(all);
        this.select(columns2);
        return this;
    }

    public Join leftJoin(Query query, String alias) {
        Join join = new Join(" LEFT JOIN ", query, alias);
        joins.add(join);
        return join;
    }

    public <T extends BaseEntityHelper> Join leftJoin(T entity) {
        return this.leftJoin(entity.getSourceClass(), entity.getTableAlias());
    }

    public Join leftJoin(Class<?> table) {
        return this.leftJoin(table, null);
    }

    public Join leftJoin(Class<?> table, String alias) {
        Join join = new Join(" LEFT JOIN ", table, alias);
        joins.add(join);
        return join;
    }

    public Join rightJoin(Query query, String alias) {
        Join join = new Join(" RIGHT JOIN ", query, alias);
        joins.add(join);
        return join;
    }

    public Join rightJoin(Class<?> table) {
        return this.rightJoin(table, null);
    }

    public <T extends BaseEntityHelper> Join rightJoin(T entity) {
        return this.rightJoin(entity.getSourceClass(), entity.getTableAlias());
    }

    public Join rightJoin(Class<?> table, String alias) {
        Join join = new Join(" RIGHT JOIN ", table, alias);
        joins.add(join);
        return join;
    }

    public Join innerJoin(Query query, String alias) {
        Join join = new Join(" INNER JOIN ", query, alias);
        joins.add(join);
        return join;
    }

    public Join innerJoin(Class<?> table) {
        return this.innerJoin(table, null);
    }

    public <T extends BaseEntityHelper> Join innerJoin(T entity) {
        return this.innerJoin(entity.getSourceClass(), entity.getTableAlias());
    }

    public Join innerJoin(Class<?> table, String alias) {
        Join join = new Join(" INNER JOIN ", table, alias);
        joins.add(join);
        return join;
    }

    public Query join(Query query, String alias) {
        Join join = new Join(",", query, alias);
        joins.add(join);
        return this;
    }

    public Query join(Class<?> table) {
        return this.join(table,null);
    }

    public <T extends BaseEntityHelper> Query join(T entity) {
        return this.join(entity.getSourceClass(),entity.getTableAlias());
    }

    public Query join(Class<?> table, String alias){
        Join join = new Join(",", table, alias);
        joins.add(join);
        return this;
    }

    public Query where() {
        return this;
    }

    public Query where(String sql) {
        logicSigns.add(new StringSQL(new lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL(sql)));
        return this;
    }

    public Query where(String sql, Map<String,Object> params) {
        logicSigns.add(new StringSQL(new lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL(sql, params)));
        return this;
    }

    public Query and(IOperator op) {
        return this.and(op, true);
    }

    public Query and(IOperator op, boolean isAppend) {
        if (isAppend)
            logicSigns.add(new And(op));
        return this;
    }

    public Query and(String sql, Map<String, Object> params) {
        this.and(sql, params, true);
        return this;
    }

    public Query and(String sql, Map<String, Object> params, boolean isAppend) {
        if (isAppend)
            logicSigns.add(new And(new lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL(sql, params)));
        return this;
    }

    public Query or(IOperator op) {
        return this.or(op, true);
    }

    public Query or(IOperator op, boolean isAppend) {
        if (isAppend)
            logicSigns.add(new Or(op));
        return this;
    }

    public Query or(String sql, Map<String, Object> params) {
        this.or(sql, params, true);
        return this;
    }

    public Query or(String sql, Map<String, Object> params, boolean isAppend) {
        if (isAppend)
            logicSigns.add(new Or(new lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL(sql, params)));
        return this;
    }

    public Query groupBy(String sql) {
        this.getGroupBy().add(new Column(sql));
        return this;
    }

    public Query groupBy(Column... columns) {
        if (columns == null)
            throw new RuntimeException("groupBy中的列不能为空.");
        this.getGroupBy().addAll(Arrays.asList(columns));
        return this;
    }

    public Query orderBy(String sql) {
        this.getOrderBy().add(new Column(sql));
        return this;
    }

    public Query orderBy(Column... columns) {
        if (columns == null)
            throw new RuntimeException("orderBy中的列不能为空.");
        this.getOrderBy().addAll(Arrays.asList(columns));
        return this;
    }

    public Query having(LogicSign sign) {
        return this.having(sign, true);
    }

    public Query having(LogicSign sign, boolean isAppend) {
        this.having.add(sign);
        return this;
    }

    public Query having(String sql) {
        this.having.add(new StringSQL(new lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL(sql)));
        return this;
    }

    public Query having(String sql, Map<String, Object> param) {
        this.having.add(new StringSQL(new lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL(sql, param)));
        return this;
    }

    public Query page(Integer start, Integer size) {
        this.page = new Page(start, size);
        return this;
    }

    public Query count() {
        this.count = true;
        return this;
    }

    public String toString() {
        if (this.sql == null) {
            SQLBuilderFactory.initSQLBuilder().build(this);
        }
        return "Query("+ sql +")";
    }

    @Getter
    @Setter
    public class Join {

        private String joinType;

        private Class<?> table;

        private String alias;

        private IOperator operator;

        private Query query;

        public Join(String joinType, Class<?> table, String alias) {
            this.joinType = joinType;
            this.table = table;
            this.alias = alias;
        }

        public Join(String joinType, Query query, String alias) {
            this.joinType = joinType;
            this.query = query;
            this.alias = alias;
        }

        public Query on(IOperator op) {
            this.operator = op;
            return Query.this;
        }

        public Query on(String sql) {
            lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL stringSQL = new lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL(sql);
            this.operator = stringSQL;
            return Query.this;
        }
    }


    public interface LogicSign {

        IOperator getOperator();

        String getSign();
    }

    public class And implements LogicSign {

        private IOperator operator;

        private final static String SIGN = " AND ";

        public And(IOperator operator) {
            this.operator = operator;
        }

        @Override
        public IOperator getOperator() {
            return operator;
        }

        @Override
        public String getSign() {
            return SIGN;
        }
    }

    public class Or implements LogicSign {

        private IOperator operator;

        private final static String SIGN = " OR ";

        public Or(IOperator operator) {
            this.operator = operator;
        }

        @Override
        public IOperator getOperator() {
            return operator;
        }

        @Override
        public String getSign() {
            return SIGN;
        }
    }

    private class StringSQL implements LogicSign {

        private IOperator operator; /*lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL*/

        public StringSQL(IOperator operator) {
            this.operator = operator;
        }

        @Override
        public IOperator getOperator() {
            return operator;
        }

        @Override
        public String getSign() {
            return "";
        }
    }

    @Getter
    @Setter
    public class Page {
        private Integer start;

        private Integer size;

        public Page(Integer start, Integer size) {
            this.start = start;
            this.size = size;
        }
    }
}
