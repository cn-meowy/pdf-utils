package cn.meowy.pdf.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.attribute.AttributeParse;
import cn.meowy.pdf.utils.attribute.BooleanAttributeParse;
import cn.meowy.pdf.utils.attribute.ColorAttributeParse;
import cn.meowy.pdf.utils.attribute.PDRectangleAttributeParse;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
    private final static Map<Class<?>, AttributeParse<?>> PARSE = new HashMap<>();

    /**
     * 初始化
     */
    static {
        PARSE.put(Boolean.class, new BooleanAttributeParse());
        PARSE.put(Color.class, new ColorAttributeParse());
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
    @SuppressWarnings("all")
    public static <T> T parse(String value, Class<T> clazz, T defaultValue) {
        if (StrUtil.isNotBlank(value)) {                        // 输入值为null,返回默认
            if (clazz.isAssignableFrom(String.class)) {         // 字符串直接返回
                return (T) value;
            } else if (clazz.isEnum()) {                        // 枚举处理
                return (T) ExUtils.execute(() -> Enum.valueOf((Class<? extends Enum>) clazz, StringUtils.upperCase(value)), "参数[{}]无法格式化为类[{}]", value, clazz.getName());
            }  else if (Number.class.isAssignableFrom(clazz)) {  // number类型处理
                return ExUtils.execute(() -> Convert.convert(clazz, String.valueOf(new ExpressionBuilder(value).build().evaluate())), "参数[{}]无法格式化为类[{}]", value, clazz.getName());
            } else if (PARSE.containsKey(clazz)) {              // 特殊处理,从处理器中获取对应解析器处理
                return (T) ExUtils.execute(() -> PARSE.get(clazz).parse(value), "参数[{}]无法格式化为类[{}]", value, clazz.getName());
            } else {
                throw new RuntimeException(StrUtil.format("参数[{}]无法格式化,[{}]找不到匹配的类处理器", value, clazz.getName()));
            }
        }
        return defaultValue;
    }

}
