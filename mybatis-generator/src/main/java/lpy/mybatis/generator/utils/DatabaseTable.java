package lpy.mybatis.generator.utils;

import java.util.List;

/**
 * @author peiyou
 * @version 1.0
 * @className DatabaseTable
 * @date 2021/10/26 3:12 下午
 **/
public class DatabaseTable {
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DatabaseColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DatabaseColumn> columns) {
        this.columns = columns;
    }

    private List<DatabaseColumn> columns;

}
