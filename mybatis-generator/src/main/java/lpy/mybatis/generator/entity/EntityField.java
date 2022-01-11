package lpy.mybatis.generator.entity;

import com.squareup.javapoet.TypeName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author peiyou
 * @version 1.0
 * @ClassName EntityField
 * @date 2021/9/16 5:01 下午
 **/
@Getter
@Setter
public class EntityField {

    protected String column;

    private TypeName javaType;

    private String properties;

    private boolean isPrimary;

    private String comment;
}
