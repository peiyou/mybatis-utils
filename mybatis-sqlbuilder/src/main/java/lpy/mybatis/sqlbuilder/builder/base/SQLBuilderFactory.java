package lpy.mybatis.sqlbuilder.builder.base;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName SQLBuilderFactory
 * @date 2021/7/29 1:22 PM
 **/
public class SQLBuilderFactory {

    private static volatile MysqlSQLBuilder mysqlSqlBuild = new MysqlSQLBuilder();

    private static volatile DatabaseName databaseName = DatabaseName.MYSQL;

    public static SQLBuilder initSQLBuilder() {
        if (DatabaseName.MYSQL == databaseName) {

        } else {

        }
        return mysqlSqlBuild;
    }

    public static void setDatabaseName(DatabaseName databaseName) {
        SQLBuilderFactory.databaseName = databaseName;
    }

    public static DatabaseName getDatabaseName() {
        return SQLBuilderFactory.databaseName;
    }

    public enum DatabaseName {
        MYSQL, ORACLE
    }
}
