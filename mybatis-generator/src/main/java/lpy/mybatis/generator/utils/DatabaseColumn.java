package lpy.mybatis.generator.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author peiyou
 * @version 1.0
 * @className DatabaseColumn
 * @date 2021/10/26 3:13 下午
 **/
@Getter
@Setter
public class DatabaseColumn {
    private String columnName;

    private String columnType;

    private String reference;

    private int nullAble;

    private boolean primaryKey;
}
