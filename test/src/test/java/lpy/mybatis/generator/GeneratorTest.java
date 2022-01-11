package lpy.mybatis.generator;

import lpy.mybatis.generator.GeneratorUtils;
import org.junit.Test;

import java.sql.SQLException;

/**
 * @author peiyou
 * @version 1.0
 * @className GeneratorTest
 * @date 2021/10/26 4:08 下午
 **/
public class GeneratorTest {

    /*@Test
    public void testGeneratorAll() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        GeneratorUtils.GeneratorBuild build = GeneratorUtils.GeneratorBuild.custom();
        build.setTableName("inspection_task")
                .setEntityPackageName("lpy.mybatis.generator.entity")
                .setDaoPackageName("lpy.mybatis.generator.dao")
                .setMapperPackageName("lpy.mybatis.generator.mapper")
                .setDatasource("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456")
                .setTest(true);
        GeneratorUtils.Generator generator = build.buildCreateEntityAndMapperAndDao();
        GeneratorUtils.generator(generator);
    }*/


    /**
     * 将表变成实体类。
     * @author Peiyou
     * @date 2022/1/10 4:53 下午
     * @return void
     */
    @Test
    public void generatorTable() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String[] tables = new String[] {
                "department",
                "employee"
        };
        for (String tableName: tables) {
            this.generator(tableName);
        }

    }

    private void generator(String tableName) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        GeneratorUtils.GeneratorBuild build = GeneratorUtils.GeneratorBuild.custom();
        build.setTableName(tableName)
                .setEntityPackageName("lpy.mybatis.entity")
                .setDaoPackageName("lpy.mybatis.dao")
                .setMapperPackageName("lpy.mybatis.mapper")
                .setDatasource("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456")
                .setTest(true); // 生成的代码放在测试目录下
        GeneratorUtils.Generator generator = build.buildCreateEntityAndMapperAndDao();
        GeneratorUtils.generator(generator);
    }
}
