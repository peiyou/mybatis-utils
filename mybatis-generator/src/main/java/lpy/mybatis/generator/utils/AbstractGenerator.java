package lpy.mybatis.generator.utils;

import com.squareup.javapoet.JavaFile;
import lombok.Getter;
import lombok.Setter;
import lpy.mybatis.generator.entity.ReflectEntity;

import java.io.File;
import java.io.IOException;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName AbstractGenerator
 * @date 2021/9/25 12:23 下午
 **/
public abstract class AbstractGenerator implements BeanGenerator {

    protected ReflectEntity entity;

    protected String basePackage;

    protected String fullPackage;

    protected abstract String getExclusivePackageName();

    @Getter
    @Setter
    private boolean generatorTestDir = true;

    @Override
    public String getOutput() {
        return output + "/src/"+ (generatorTestDir ? "test": "main") +"/java/" ;
    }

    private String basePath() {
        String path = "";
        if (fullPackage != null)
            path += fullPackage.replace('.', '/') + "/";
        else
            path += basePackage.replace('.', '/') + "/" +
                    this.getExclusivePackageName().replace('.', '/') + "/";

        return path;
    }

    protected abstract JavaFile generator();

    @Override
    public String getFilePath() {
        return this.getOutput() + basePath() + getClassName() + ".java";
    }

    @Override
    public void build() {
        JavaFile javaFile = this.generator();
        if (new File(this.getFilePath()).exists()) {
            info("文件：" + this.getClassName() + "已存在，请删除后再生成。");
            return;
        }
        try {
            javaFile.writeTo(new File(this.getOutput()));
            info(this.getClassName() + "已写入。path=" + this.getFilePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void info(String message) {
        System.out.println(message);
    }
}
