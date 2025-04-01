package cn.meowy.pdf.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.img.ColorUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * 颜色工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class ColorUtils {

    /**
     * 获取颜色
     *
     * @param color 颜色
     * @return 颜色
     */
    public static Color get(String color) {
        if (StrUtil.isNotBlank(color)) {
            List<String> split = StrUtil.split(color, CharSequenceUtil.SPACE);
            int size = CollUtil.size(split);
            if (size == 1) {
                return ColorUtil.getColor(color);
            } else if (size == 3) {
                return ExUtils.execute(() -> new Color(NumberUtil.parseInt(split.get(0)), NumberUtil.parseInt(split.get(1)), NumberUtil.parseInt(split.get(2))), "初始化颜色失败!");
            }
            throw new RuntimeException("色彩配置错误!");
        }
        return null;
    }

    /**
     * 获取颜色
     *
     * @param color        颜色
     * @param defaultColor 默认颜色
     * @return 颜色
     */
    public static Color get(String color, Color defaultColor) {
        return ObjectUtil.defaultIfNull(get(color), defaultColor);
    }
}
