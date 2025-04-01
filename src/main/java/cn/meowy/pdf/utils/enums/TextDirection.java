package cn.meowy.pdf.utils.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 文本方向
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public enum TextDirection {

    /**
     * VERTICAL-垂直,HORIZONTAL-水平
     */
    VERTICAL, HORIZONTAL,
    ;

    public static TextDirection get(String name, TextDirection defaultValue) {
        for (TextDirection item : TextDirection.values()) {
            if (StringUtils.equalsIgnoreCase(item.name(), name)) {
                return item;
            }
        }
        return defaultValue;
    }

    /**
     * 是否为垂直
     */
    public boolean isVertical() {
        return this == TextDirection.VERTICAL;
    }
}
