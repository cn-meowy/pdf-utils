package cn.meowy.pdf.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

/**
 * 字符串工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/23
 **/
public class StringUtils {

    /**
     * 字符串格式化
     *
     * @param str 字符串
     * @param map map
     * @param <V> 类型
     * @return 字符串
     */
    public static <V> String format(String str, Map<String, V> map) {
        return StringSubstitutor.replace(str, map, StrUtil.DELIM_START, StrUtil.DELIM_END);
    }
}
