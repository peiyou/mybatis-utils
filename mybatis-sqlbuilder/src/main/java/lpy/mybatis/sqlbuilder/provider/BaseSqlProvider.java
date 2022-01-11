package lpy.mybatis.sqlbuilder.provider;

import lpy.mybatis.sqlbuilder.builder.Query;
import lpy.mybatis.sqlbuilder.util.Entity;
import lpy.mybatis.sqlbuilder.util.EntityHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * @author peiyou
 * @version 1.0
 * @ClassName BaseSqlProvider
 * @date 2021/7/26 12:10 PM
 **/
public class BaseSqlProvider {

    public <T> String insert(T entity) {
        Entity entityClass = EntityHelper.parseClass(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO ");
        sql.append(entityClass.getTableName());
        sql.append(buildInsertColumns(entity, entityClass));
        sql.append(buildInsertValues(entity, entityClass));
        return sql.toString();
    }

    public <T> String insertBatch(List<T> entityList) {
        Entity entityClass = EntityHelper.parseClass(entityList.get(0).getClass());
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO ");
        sql.append(entityClass.getTableName());
        sql.append(buildInsertColumns(entityList.get(0), entityClass));
        sql.append(buildInsertBatchValues(entityList, entityClass));
        return sql.toString();
    }

    public <T> String updateById(T entity) {
        Entity entityClass = EntityHelper.parseClass(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append(" UPDATE ");
        sql.append(entityClass.getTableName());
        sql.append(" SET ");
        sql.append(buildUpdate(entity, entityClass));
        sql.append(" WHERE ")
                .append(entityClass.getPrimaryCol())
                .append(" = ").append("#{")
                .append(entityClass.getPrimaryKey())
                .append("}");

        return sql.toString();
    }

    public <T> String deleteById(T entity){
        Entity entityClass = EntityHelper.parseClass(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(entityClass.getTableName());
        sql.append(" WHERE ");
        sql.append(entityClass.getPrimaryCol());
        sql.append(" = ");
        sql.append("#{").append(entityClass.getPrimaryKey()).append("}");
        return sql.toString();
    }

    public <T> String deleteByEntity(T entity){
        Entity entityClass = EntityHelper.parseClass(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(entityClass.getTableName());
        sql.append(" WHERE ");
        String where = this.buildWhere(entity, entityClass);
        sql.append(where);
        return sql.toString();
    }

    public <T> String findAll(Class<T> clazz) {
        Entity entityClass = EntityHelper.parseClass(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").
                append(this.buildSelectColumns(entityClass))
                .append(" FROM ");
        sql.append(entityClass.getTableName());
        return sql.toString();
    }

    public <T> String findById(T entity){
        Entity entityClass = EntityHelper.parseClass(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").
                append(this.buildSelectColumns(entityClass))
                .append(" FROM ");
        sql.append(entityClass.getTableName());
        sql.append(" WHERE ");
        sql.append(entityClass.getPrimaryCol());
        sql.append(" = ");
        sql.append("#{").append(entityClass.getPrimaryKey()).append("}");
        return sql.toString();
    }

    public <T> String findByList(Map<String, Object> params) {
        Query query = (Query) params.get(Query.QUERY_PARAM);
        query.build();
        Map<String, Object> p = query.getParamsMap();
        params.putAll(p);
        return query.getSql();
    }

    public <T> String findAllByProviderEntity(T entity) {
        Entity entityClass = EntityHelper.parseClass(entity.getClass());
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").
                append(this.buildSelectColumns(entityClass))
                .append(" FROM ");
        sql.append(entityClass.getTableName());
        sql.append(" WHERE ");

        String where = this.buildWhere(entity, entityClass);
        sql.append(where);
        return sql.toString();
    }

    private <T> String buildWhere(T entity, Entity entityClass) {
        StringBuilder sql = new StringBuilder();
        int count = 0;
        for (String properties: entityClass.getPropertiesAndGetMethod().keySet()) {
            Method method = entityClass.getPropertiesAndGetMethod().get(properties);;
            String column = null;
            if (properties.equals(entityClass.getPrimaryKey())) { // 为主键时
                column = entityClass.getPrimaryCol();
            } else { // 为其它值时
                column = entityClass.getPropertiesAndColumn().get(properties);
            }
            Object value = null;
            try {
                value = method.invoke(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("动态生成SQL失败.");
            } catch (InvocationTargetException e) {
                throw new RuntimeException("动态生成SQL失败.");
            }
            if (value != null && column != null) {
                if (count > 0)
                    sql.append(" AND ");
                sql.append(column).append("=").append("#{").append(properties).append("}").append(" ");
                count++;
            }
        }
        return sql.toString();
    }

    private <T> String buildInsertColumns(T obj, Entity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        int index = 0;
        for (String fieldName: entity.getPropertiesAndColumn().keySet()) {
            if (fieldName.equals(entity.getPrimaryKey()))
                continue;
            Object value = getValue(fieldName, obj, entity);
            if (value == null)
                continue;
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(entity.getPropertiesAndColumn().get(fieldName));
            index++;
        }

        sb.append(")");
        return sb.toString();
    }

    private <T> String buildInsertValues(T obj, Entity entity){
        StringBuilder sb = new StringBuilder();
        sb.append(" values (");
        int index = 0;
        for (String fieldName: entity.getPropertiesAndColumn().keySet()) {
            if (fieldName.equals(entity.getPrimaryKey()))
                continue;
            Object value = getValue(fieldName, obj, entity);
            if (value == null)
            continue;
            if (index > 0) {
                sb.append(", ");
            }
            sb.append("#{").append(fieldName).append("}");
            index++;
        }

        sb.append(")");
        return sb.toString();
    }

    private <T> String buildSelectColumns(Entity entity) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        boolean isPrimary = false;
        for (String fieldName: entity.getPropertiesAndColumn().keySet()) {
            if (fieldName.equals(entity.getPrimaryKey()))
                isPrimary = true;
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(entity.getPropertiesAndColumn().get(fieldName));
            sb.append(" as ").append(fieldName);
            index++;
        }
        if (!isPrimary) {
            if (index > 0)
                sb.append(", ");
            sb.append(entity.getPrimaryCol()).append(" as ").append(entity.getPrimaryKey());
        }
        return sb.toString();
    }

    private <T> String buildInsertBatchValues(List<T> list, Entity entity) {
        T obj = list.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append(" values");
        for (int i = 0; i < list.size(); i++) {
            int index = 0;
            sb.append("(");
            for (String fieldName: entity.getPropertiesAndColumn().keySet()) {
                if (fieldName.equals(entity.getPrimaryKey()))
                    continue;
                Object value = getValue(fieldName, obj, entity);
                if (value == null)
                    continue;
                if (index > 0) {
                    sb.append(", ");
                }
                sb.append("#{list[").append(i).append("].").append(fieldName).append("}");
                index++;
            }
            sb.append(")");
            if (i + 1 < list.size())
                sb.append(",");
        }
        return sb.toString();
    }

    private <T> String buildUpdate(T obj, Entity entity) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String fieldName: entity.getPropertiesAndColumn().keySet()) {
            if (fieldName.equals(entity.getPrimaryKey()))
                continue;
            Object value = getValue(fieldName, obj, entity);
            if (value == null)
                continue;
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(entity.getPropertiesAndColumn().get(fieldName))
                    .append(" = ")
                    .append("#{")
                    .append(fieldName)
                    .append("}");
            index++;
        }
        return sb.toString();
    }

    private <T> Object getValue(String fieldName, T obj, Entity entity) {
        try {
            return entity.getPropertiesAndGetMethod().get(fieldName).invoke(obj);
        } catch (IllegalAccessException|InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
