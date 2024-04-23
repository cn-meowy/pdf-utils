package cn.meowy.pdf.utils;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.attribute.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.VerticalAlignment;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 属性工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class AttributeUtils {

    /**
     * 解析类型
     */
    private final static Map<Class<?>, AttributeParse> PARSE = new HashMap<>();

    /**
     * 初始化
     */
    static {
        PARSE.put(Color.class, new ColorAttributeParse());
        PARSE.put(Float.class, new FloatAttributeParse());
        PARSE.put(HorizontalAlignment.class, new HorizontalAlignmentAttributeParse());
        PARSE.put(VerticalAlignment.class, new VerticalAlignmentAttributeParse());
        PARSE.put(PDRectangle.class, new PDRectangleAttributeParse());
    }

    /**
     * 解析
     *
     * @param value        值
     * @param clazz        类型
     * @param defaultValue 默认值
     * @param <T>          返回值类型
     * @return 返回值
     */
    public static <T> T parse(String value, Class<T> clazz, T defaultValue) {
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        // 枚举
        if (clazz.isEnum()) {
            for (T enumConstant : clazz.getEnumConstants()) {
                if (StrUtil.equalsIgnoreCase(((Enum<?>) enumConstant).name(), value)) {
                    return enumConstant;
                }
            }
            return null;
        }
        // 获取对应解析器处理
        AttributeParse parse = PARSE.get(clazz);
        return Objects.isNull(parse) ? defaultValue : parse.parse(value);
    }

}
