package lpy.mybatis.entity;

import java.lang.Integer;
import java.lang.String;
import lombok.Getter;
import lombok.Setter;
import lpy.mybatis.generator.annotation.MybatisGeneratorHelper;
import lpy.mybatis.sqlbuilder.annotation.Id;
import lpy.mybatis.sqlbuilder.annotation.TableField;
import lpy.mybatis.sqlbuilder.annotation.TableName;

@MybatisGeneratorHelper
@TableName("department")
@Getter
@Setter
public class Department {
  @TableField("id")
  @Id
  private Integer id;

  @TableField("name")
  private String name;
}
