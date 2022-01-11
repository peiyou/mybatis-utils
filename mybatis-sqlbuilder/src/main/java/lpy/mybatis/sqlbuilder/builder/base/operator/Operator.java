package lpy.mybatis.sqlbuilder.builder.base.operator;

import lpy.mybatis.sqlbuilder.builder.base.Column;
import lpy.mybatis.sqlbuilder.builder.base.IOperator;

import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName Operator
 * @date 2021/7/29 4:13 PM
 **/
public class Operator {

    public static IOperator eq(Column column, Object param) {
        return Eq.eq(column, param);
    }

    public static IOperator eq(Column column, Object param, boolean isAdd) {
        return Eq.eq(column, param, isAdd);
    }

    public static IOperator eq(Column left, Column right) {
        return Eq.eq(left, right);
    }

    public static IOperator ge(Column column, Object param) {
        return Ge.ge(column, param);
    }

    public static IOperator ge(Column column, Object param, boolean isAdd) {
        return Ge.ge(column, param, isAdd);
    }

    public static IOperator ge(Column left, Column right) {
        return Ge.ge(left, right);
    }

    public static IOperator gt(Column column, Object param) {
        return Gt.gt(column, param);
    }

    public static IOperator gt(Column column, Object param, boolean isAdd) {
        return Gt.gt(column, param, isAdd);
    }

    public static IOperator gt(Column left, Column right) {
        return Gt.gt(left, right);
    }

    public static IOperator le(Column column, Object param) {
        return Le.le(column, param);
    }

    public static IOperator le(Column column, Object param, boolean isAdd) {
        return Le.le(column, param, isAdd);
    }

    public static IOperator le(Column left, Column right) {
        return Le.le(left, right);
    }

    public static IOperator lt(Column column, Object param) {
        return Lt.lt(column, param);
    }

    public static IOperator lt(Column column, Object param, boolean isAdd) {
        return Lt.lt(column, param, isAdd);
    }

    public static IOperator lt(Column left, Column right) {
        return Lt.lt(left, right);
    }

    public static IOperator ne(Column column, Object param) {
        return Ne.ne(column, param);
    }

    public static IOperator ne(Column column, Object param, boolean isAdd) {
        return Ne.ne(column, param, isAdd);
    }

    public static IOperator ne(Column left, Column right) {
        return Ne.ne(left, right);
    }

    public static IOperator isNull(Column column) {
        return IsNull.isNull(column);
    }

    public static IOperator isNotNull(Column column) {
        return IsNotNull.isNotNull(column);
    }

    public static <T> IOperator in(Column column, List<T> paramList) {
        return In.in(column, paramList);
    }

    public static IOperator and(IOperator... iOperators) {
        return And.and(iOperators);
    }

    public static IOperator or(IOperator... iOperators) {
        return Or.or(iOperators);
    }

    public static IOperator between(Column column, Object startParam, Object endParam) {
        return Between.between(column, startParam, endParam);
    }

    public static IOperator between(Column column, Object startParam, Object endParam, boolean isAdd) {
        return Between.between(column, startParam, endParam, isAdd);
    }

    public static IOperator leftLike(Column column, Object param) {
        return Like.like(column, param, true, false, true);
    }

    public static IOperator rightLike(Column column, Object param) {
        return Like.like(column, param, false, true, true);
    }

    public static IOperator leftLike(Column column, Object param, boolean isAdd) {
        return Like.like(column, param, true, false, isAdd);
    }

    public static IOperator rightLike(Column column, Object param, boolean isAdd) {
        return Like.like(column, param, false, true, isAdd);
    }

    public static IOperator like(Column column, Object param) {
        return Like.like(column, param, true, true, true);
    }

    public static IOperator like(Column column, Object param, boolean isAdd) {
        return Like.like(column, param, true, true, isAdd);
    }
}
