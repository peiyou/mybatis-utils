package lpy.mybatis.sqlbuilder.builder.base;

import lombok.Getter;
import lombok.Setter;
import lpy.mybatis.sqlbuilder.builder.base.operator.Operator;
import lpy.mybatis.sqlbuilder.builder.base.operator.StringSQL;

import java.util.List;
import java.util.Map;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Column
 * @date 2021/7/26 5:51 PM
 **/
@Getter
@Setter
public class Column {

    private String tableName;

    private String tableAlias;

    private String column;

    private String alias;

    private String sort = " ASC ";

    public Column asc() {
        this.sort = " ASC ";
        return this;
    }

    public Column as(String alias) {
        this.alias = alias;
        return this;
    }

    public Column desc(){
        this.sort = " DESC ";
        return this;
    }

    public IOperator eq(Object param) {
        return Operator.eq(this, param);
    }

    public IOperator eq(Object param, boolean isAdd) {
        return Operator.eq(this, param, isAdd);
    }

    public IOperator eq(Column right) {
        return Operator.eq(this, right);
    }

    public IOperator ge(Object param) {
        return Operator.ge(this, param);
    }

    public IOperator ge(Object param, boolean isAdd) {
        return Operator.ge(this, param, isAdd);
    }

    public IOperator ge(Column right) {
        return Operator.ge(this, right);
    }

    public IOperator gt(Object param) {
        return Operator.gt(this, param);
    }

    public IOperator gt(Object param, boolean isAdd) {
        return Operator.gt(this, param, isAdd);
    }

    public IOperator gt(Column right) {
        return Operator.gt(this, right);
    }

    public IOperator le(Object param) {
        return Operator.le(this, param);
    }

    public IOperator le(Object param, boolean isAdd) {
        return Operator.le(this, param, isAdd);
    }

    public IOperator le(Column right) {
        return Operator.le(this, right);
    }

    public IOperator lt(Object param) {
        return Operator.lt(this, param);
    }

    public IOperator lt(Object param, boolean isAdd) {
        return Operator.lt(this, param, isAdd);
    }

    public IOperator lt(Column right) {
        return Operator.lt(this, right);
    }

    public IOperator ne(Object param) {
        return Operator.ne(this, param);
    }

    public IOperator ne(Object param, boolean isAdd) {
        return Operator.ne(this, param, isAdd);
    }

    public IOperator ne(Column right) {
        return Operator.ne(this, right);
    }

    public <T> IOperator in(List<T> paramList) {
        return Operator.in(this, paramList);
    }

    public IOperator between(Object startParam, Object endParam) {
        return Operator.between(this, startParam, endParam);
    }

    public IOperator between(Object startParam, Object endParam, boolean isAdd) {
        return Operator.between(this, startParam, endParam, isAdd);
    }

    public IOperator isNotNull() {
        return Operator.isNotNull(this);
    }

    public IOperator isNull() {
        return Operator.isNull(this);
    }

    public IOperator like(Object param) {
        return Operator.like(this, param);
    }

    public IOperator like(Object param, boolean isAdd) {
        return Operator.like(this, param, isAdd);
    }

    public IOperator leftLike(Object param) {
        return Operator.leftLike(this, param);
    }

    public IOperator leftLike(Object param, boolean isAdd) {
        return Operator.leftLike(this, param, isAdd);
    }

    public IOperator rightLike(Object param, boolean isAdd) {
        return Operator.rightLike(this, param, isAdd);
    }

    public IOperator rightLike(Object param) {
        return Operator.rightLike(this, param);
    }

    public IOperator whereStringSql(String sql) {
        return new StringSQL(sql);
    }

    public IOperator whereStringSql(String sql, Map<String, Object> params) {
        return new StringSQL(sql, params);
    }

    public Column(String column) {
        this(column, null);
    }

    public Column(String column, String alias) {
        this(null, column, alias);
    }

    public Column(String tableName, String column, String alias) {
        this(tableName, tableName, column, alias);
    }

    public Column(String tableName, String tableAlias, String column, String alias) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.column = column;
        this.alias = alias;
    }
}
