package lpy.mybatis.generator.utils;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName BeanGenerator
 * @date 2021/9/18 10:56 上午
 **/
public interface BeanGenerator {

    String output = System.getProperty("user.dir");

    String getBasePackage();

    void build();

    String getOutput();

    String getClassName();

    String getPackage();

    String getFilePath();
}
