package lpy.mybatis.sqlbuilder.exception;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName MybatisUtilsException
 * @date 2021/7/29 10:43 AM
 **/
public class MybatisUtilsException extends RuntimeException {

    public MybatisUtilsException() {

    }

    public MybatisUtilsException(String message) {
        super(message);
    }
}
