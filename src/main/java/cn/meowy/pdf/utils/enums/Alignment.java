package cn.meowy.pdf.utils.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 对齐方式
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public enum Alignment {

    /**
     * 水平
     */
    LEFT, CENTER, RIGHT,
    /**
     * 垂直
     */
    BOTTOM, MIDDLE, TOP,
    ;

    public static Alignment get(String name, Alignment defaultAlignment) {
        for (Alignment item : values()) {
            if (StringUtils.equalsIgnoreCase(item.name(), name)) {
                return item;
            }
        }
        return defaultAlignment;
    }
}
