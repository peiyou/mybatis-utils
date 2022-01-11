package lpy.mybatis.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import lpy.mybatis.generator.entity.EntityField;
import lpy.mybatis.generator.entity.ReflectEntity;
import lpy.mybatis.generator.utils.*;
import lpy.mybatis.sqlbuilder.exception.MybatisUtilsException;
import lpy.mybatis.sqlbuilder.util.Entity;
import lpy.mybatis.sqlbuilder.util.EntityHelper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName GeneratorUtils
 * @date 2021/9/25 4:13 下午
 **/
public class GeneratorUtils {

    /**
     * 通过entity生成mapper与dao
     * @author peiyou
     * @date 2021/10/11 2:54 下午
     * @param generator
     * @return void
     **/
    public static void generator(Generator generator) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ReflectEntity entity = null;
        if (generator.generatorRange == Generator.GeneratorRange.ALL || generator.generatorRange == Generator.GeneratorRange.ENTITY) {
            // 需要生成entity
            entity = GeneratorUtils.getReflectEntity(generator);
            if (entity == null){
                throw new MybatisUtilsException("表["+generator.tableName+"]不存在.");
            }

            EntityGenerator entityGenerator = new EntityGenerator(entity.getPackageName(), entity, true);
            entityGenerator.build();
            System.out.println("========entity已生成完成=========");
        }

        if (generator.generatorRange == Generator.GeneratorRange.ALL || generator.generatorRange == Generator.GeneratorRange.MAPPER_DAO) {
            // 需要生成mapper与dao
            if (entity == null){
                Entity tempEntity = EntityHelper.parseClass(generator.clazz);
                entity = EntityGeneratorUtils.transfer(tempEntity);
            }
            MapperGenerator mapperGenerator = null;
            if (generator.mapperPackageName == null) {
                mapperGenerator = new MapperGenerator(entity);
            } else {
                mapperGenerator = new MapperGenerator(generator.mapperPackageName, entity);
            }
            mapperGenerator.build();
            DaoGenerator daoGenerator = null;
            if (generator.daoPackageName == null) {
                daoGenerator = new DaoGenerator(entity);
            } else {
                daoGenerator = new DaoGenerator(generator.daoPackageName, entity);
            }
            daoGenerator.build();

            DaoImplGenerator daoImplGenerator = new DaoImplGenerator(ClassName.get(mapperGenerator.getPackage(), mapperGenerator.getClassName()),
                    ClassName.get(daoGenerator.getPackage(), daoGenerator.getClassName()), entity);
            daoImplGenerator.build();
            System.out.println("========mapper与dao已生成完成=========");
        }
        System.out.println("========已生成完成=========");
    }


    public static class GeneratorBuild {

        private String tableName;

        private String entityPackageName;

        private String mapperPackageName;

        private String daoPackageName;

        private boolean test;

        private Class<?> clazz;

        private String url;

        private String user;

        private String password;

        private GeneratorBuild() {}

        public static GeneratorBuild custom() {
            return new GeneratorBuild();
        }

        public GeneratorBuild setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public GeneratorBuild setEntityPackageName(String entityPackageName) {
            this.entityPackageName = entityPackageName;
            return this;
        }

        public GeneratorBuild setMapperPackageName(String mapperPackageName) {
            this.mapperPackageName = mapperPackageName;
            return this;
        }

        public GeneratorBuild setDaoPackageName(String daoPackageName) {
            this.daoPackageName = daoPackageName;
            return this;
        }

        public GeneratorBuild setTest(boolean test) {
            this.test = test;
            return this;
        }

        public GeneratorBuild setClazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public GeneratorBuild setDatasource(String url, String user, String password) {
            this.url = url;
            this.user = user;
            this.password = password;
            return this;
        }

        /**
         * 只生成entity
         * @author peiyou
         * @date 2021/10/11 2:16 下午
         * @return
         **/
        public Generator buildOnlyCreateEntity() {
            if (tableName == null)
                throw new RuntimeException("生成实体类时，tableName不能为空。");
            if (entityPackageName == null)
                throw new RuntimeException("生成实体类时，entityPackageName不能为空。");
            if (this.test)
                System.out.println("代码将生成在[src/test]资源中。");
            if (user == null || password == null || url == null)
                throw new RuntimeException("生成实体类时，数据库的连接信息（user、password、url）不能为空。");
            Generator generator = new Generator(tableName, entityPackageName, mapperPackageName, daoPackageName, this.test, clazz);
            generator.url = url;
            generator.user = user;
            generator.password = password;
            generator.generatorRange = Generator.GeneratorRange.ENTITY;
            return generator;
        }

        /**
         * 生成实体类，mapper，dao层
         * @author peiyou
         * @date 2021/10/11 2:21 下午
         * @return lpy.mybatis.generator.GeneratorUtils.Generator
         **/
        public Generator buildCreateEntityAndMapperAndDao() {
            if (tableName == null)
                throw new RuntimeException("生成实体类时，tableName不能为空。");
            if (entityPackageName == null)
                throw new RuntimeException("生成实体类时，entityPackageName不能为空。");
            if (mapperPackageName == null)
                System.out.println("未填写mapperPackageName，mapper层将生成在[" + entityPackageName.substring(0, entityPackageName.lastIndexOf(".")) + ".mapper]。如果不是在该位置，则请在build前设置。");
            if (daoPackageName == null)
                System.out.println("未填写daoPackageName，dao层将生成在[" + entityPackageName.substring(0, entityPackageName.lastIndexOf(".")) + ".dao]。如果不是在该位置，则请在build前设置。");
            if (this.test)
                System.out.println("代码将生成在[src/test]资源中。");
            if (user == null || password == null || url == null)
                throw new RuntimeException("生成实体类时，数据库的连接信息（user、password、url）不能为空。");
            Generator generator = new Generator(tableName, entityPackageName, mapperPackageName, daoPackageName, this.test, clazz);
            generator.generatorRange = Generator.GeneratorRange.ALL;
            generator.url = url;
            generator.user = user;
            generator.password = password;
            return generator;
        }

        /**
         * 通过实体类生成 mapper和dao层
         * @author peiyou
         * @date 2021/10/11 2:44 下午
         * @return lpy.mybatis.generator.GeneratorUtils.Generator
         **/
        public Generator buildCreateMapperAndDao() {
            if (clazz == null)
                throw new RuntimeException("通过实体类生成mapper与dao层时，clazz不能为空（entity.class）。");

            entityPackageName = clazz.getPackage().getName();

            if (mapperPackageName == null)
                System.out.println("未填写mapperPackageName，mapper层将生成在[" + entityPackageName.substring(0, entityPackageName.lastIndexOf(".")) + ".mapper]。如果不是在该位置，则请在build前设置。");
            if (daoPackageName == null)
                System.out.println("未填写daoPackageName，dao层将生成在[" + entityPackageName.substring(0, entityPackageName.lastIndexOf(".")) + ".dao]。如果不是在该位置，则请在build前设置。");
            if (this.test)
                System.out.println("代码将生成在[src/test]资源中。");

            Generator generator = new Generator(tableName, entityPackageName, mapperPackageName, daoPackageName, this.test, clazz);
            generator.generatorRange = (Generator.GeneratorRange.MAPPER_DAO);
            return generator;
        }
    }

    public static class Generator {
        private String tableName;

        private String entityPackageName;

        private String mapperPackageName;

        private String daoPackageName;

        private boolean test;

        private Class<?> clazz;

        private String url;

        private String user;

        private String password;

        private GeneratorRange generatorRange = GeneratorRange.ENTITY;

        private Generator(String tableName, String entityPackageName, String mapperPackageName, String daoPackageName, boolean isTest, Class<?> clazz) {
            this.tableName = tableName;
            this.entityPackageName = entityPackageName;
            this.mapperPackageName = mapperPackageName;
            this.daoPackageName = daoPackageName;
            this.test = isTest;
            this.clazz = clazz;
        }

        public enum GeneratorRange {
            ALL, ENTITY, MAPPER_DAO
        }
    }

    private static ReflectEntity getReflectEntity(Generator generator) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(generator.url, generator.user, generator.password);
        DatabaseTable table = GeneratorUtils.getTableColumns(connection, generator.tableName);
        if (table == null) {
            System.out.println("table " + generator.tableName + " has not exist");
            return null;
        }
        return GeneratorUtils.convertTableToJavaModel(generator, table);
    }

    /**
     * 获取表结构信息并存在table里
     * @param conn
     * @param tableName
     * @return
     * @throws SQLException
     */
    private static DatabaseTable getTableColumns(Connection conn, String tableName){
        DatabaseMetaData metaData;
        DatabaseTable table = null;
        try {
            metaData = conn.getMetaData();
            // 查询主键
            String primaryKeyColumnName = null;
            ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, "%", tableName);
            while(primaryKeyResultSet.next()){
                primaryKeyColumnName = primaryKeyResultSet.getString("COLUMN_NAME");
            }
            if (primaryKeyColumnName == null)
                throw new MybatisUtilsException("表[" + tableName + "]无主键，无法生成对应的实体类，因在使用按主键查询时，会出现异常情况。请添加一个主键后再生成。");
            ResultSet rs = metaData.getColumns(null, "%", tableName, "%");
            table = new DatabaseTable();
            table.setTableName(tableName);
            List<DatabaseColumn> columns = new ArrayList<>();
            while(rs.next()) {
                DatabaseColumn column = new DatabaseColumn();
                column.setColumnName(rs.getString("COLUMN_NAME"));
                column.setColumnType(rs.getString("TYPE_NAME"));
                column.setReference(rs.getString("REMARKS"));
                if (primaryKeyColumnName.equals(column.getColumnName())) {
                    column.setPrimaryKey(true);
                }
                columns.add(column);
            }
            table.setColumns(columns);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return table;
    }

    private static ReflectEntity convertTableToJavaModel(Generator generator, DatabaseTable table) {
        if (table == null) {
            return null;
        }
        ReflectEntity model = new ReflectEntity();
        model.setClassName(generator.entityPackageName ,firstWordToUp(convertSnakeToCamel(table.getTableName())));
        model.setTableName(table.getTableName());

        for(DatabaseColumn c : table.getColumns()) {
            EntityField prop = new EntityField();
            prop.setColumn(c.getColumnName());
            prop.setProperties(convertSnakeToCamel(c.getColumnName()));
            prop.setJavaType(TypeName.get(convertDatabaseTypeToJavaType(c.getColumnType())));
            prop.setPrimary(c.isPrimaryKey());
            prop.setComment(c.getReference());
            model.addField(prop);
            if (c.isPrimaryKey()) {
                model.setPrimaryKey(prop);
            }
        }
        return model;
    }

    /**
     * 首字母转大写
     * @author Peiyou
     * @date 2021/11/1 4:42 下午
     * @param clazzName
     * @return java.lang.String
     */
    private static String firstWordToUp(String clazzName) {
        return clazzName.substring(0, 1).toUpperCase() + clazzName.substring(1);
    }

    /**
     * 由下划线(蛇型）命名法，转化成驼峰命名法
     *
     * @param name
     */
    private static String convertSnakeToCamel(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }

        char[] nameChars = name.toCharArray();
        boolean previousLine = false;
        for (int i=0; i<nameChars.length; i++) {
            if (previousLine) {
                nameChars[i] -= 32;
                previousLine = false;
            }
            if (nameChars[i] == "_".charAt(0)) {
                previousLine = true;
            }
        }
        return String.valueOf(nameChars).replaceAll("_", "");
    }

    private static Class<?> convertDatabaseTypeToJavaType(String dbType) {
        List<String> integerTypes = Arrays.asList("TINYINT", "SMALLINT", "MEDIUMINT", "INT", "INTEGER");
        List<String> longTypes = Arrays.asList("TIMESTAMP", "BIGINT", "INT UNSIGNED");
        List<String> floatTypes = Arrays.asList("FLOAT");
        List<String> bigDecimal = Arrays.asList("DECIMAL");
        List<String> stringTypes = Arrays.asList("VARCHAR", "TINYBLOB", "CHAR", "TINYTEXT", "BLOB", "TEXT", "MEDIUMBLOB", "MEDIUMTEXT", "LONGBLOB", "LONGTEXT");
        List<String> dateTypes = Arrays.asList("DATETIME", "DATE");
        List<String> booleanTypes = Arrays.asList("BIT");

        if(integerTypes.contains(dbType)) {
            return Integer.class;
        }

        if(longTypes.contains(dbType)) {
            return Long.class;
        }

        if(floatTypes.contains(dbType)) {
            return Float.class;
        }

        if(bigDecimal.contains(dbType)) {
            return BigDecimal.class;
        }

        if(stringTypes.contains(dbType)) {
            return String.class;
        }

        if(dateTypes.contains(dbType)) {
            return java.util.Date.class;
        }

        if (booleanTypes.contains(dbType)) {
            return Boolean.class;
        }

        throw new MybatisUtilsException("未匹配到Java类型, databaseType: " + dbType);
    }

}
