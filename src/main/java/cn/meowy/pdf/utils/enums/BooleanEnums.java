package cn.meowy.pdf.utils.enums;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Boolean 枚举
 *
 * @author: Mr.Zou
 * @date: 2025-03-29
 **/
@AllArgsConstructor
public enum BooleanEnums {

    TRUE(true, CollUtil.newArrayList("true", "yes", "1", "是")),
    FALSE(false, CollUtil.newArrayList("false", "no", "0", "否")),

    ;

    public final Boolean key;
    public final List<String> values;

    public static Boolean get(String value) {
        for (BooleanEnums item : values()) {
            if (item.values.contains(StringUtils.lowerCase(value))) {
                return item.key;
            }
        }
        return null;
    }
}
