package lpy.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author peiyou
 * @version 2.4.0
 * @className Application
 * @date 2022/1/10 5:20 下午
 **/
@SpringBootApplication
@MapperScan(basePackages = "lpy.mybatis.mapper")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
}
