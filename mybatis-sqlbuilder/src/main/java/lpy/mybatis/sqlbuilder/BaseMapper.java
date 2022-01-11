package lpy.mybatis.sqlbuilder;

import lpy.mybatis.sqlbuilder.builder.Query;
import lpy.mybatis.sqlbuilder.provider.BaseSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

import static lpy.mybatis.sqlbuilder.builder.Query.QUERY_PARAM;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName BaseMapper
 * @date 2021/7/26 11:54 AM
 **/
public interface BaseMapper<T> {

    @InsertProvider(type = BaseSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int save(T entity);

    @InsertProvider(type = BaseSqlProvider.class, method = "insertBatch")
    int saveBatch(@Param("list") List<T> entityList);

    @UpdateProvider(type = BaseSqlProvider.class, method = "updateById")
    int updateById(T entity);

    @DeleteProvider(type = BaseSqlProvider.class, method = "deleteById")
    int deleteById(T entity);

    @DeleteProvider(type = BaseSqlProvider.class, method = "deleteByEntity")
    int deleteByEntity(T entity);

    @SelectProvider(type = BaseSqlProvider.class, method = "findAll")
    List<T> findAll(Class<T> clazz);

    @SelectProvider(type = BaseSqlProvider.class, method = "findById")
    T findById(T entity);

    @SelectProvider(type = BaseSqlProvider.class, method = "findAllByProviderEntity")
    List<T> findAllByProviderEntity(T entity);

    @SelectProvider(type = BaseSqlProvider.class, method = "findAllByProviderEntity")
    T findByOne(T entity);

    @SelectProvider(type = BaseSqlProvider.class, method = "findByList")
    List<T> findByList(@Param(QUERY_PARAM) Query query);

    @SelectProvider(type = BaseSqlProvider.class, method = "findByList")
    int findByCount(@Param(QUERY_PARAM) Query query);

    @SelectProvider(type = BaseSqlProvider.class, method = "findByList")
    Map<String, Object> findByOneToMap(@Param(QUERY_PARAM) Query query);

    @SelectProvider(type = BaseSqlProvider.class, method = "findByList")
    List<Map<String, Object>> findByListToMap(@Param(QUERY_PARAM) Query query);
}
