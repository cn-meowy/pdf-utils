package cn.meowy.pdf.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.text.StringSubstitutor;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * xml处理工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/18
 **/
public class XmlUtils {

    /**
     * 判断根节点是否指定节点
     *
     * @param doc      文档
     * @param rootName 根节点名称
     * @return true-是,false-否
     */
    public static boolean rootElementCheck(Document doc, String rootName) {
        return StrUtil.equals(doc.getRootElement().getName(), rootName);
    }

    /**
     * 获取属性
     *
     * @param element       元素
     * @param attributeName 属性名
     * @param map           替换值
     * @return 值
     */
    public static <T> String getStr(Element element, String attributeName, Map<String, Object> map) {
        Attribute attribute = element.attribute(attributeName);
        if (Objects.nonNull(attribute)) {
            return StringUtils.format(attribute.getValue(), map);
        }
        return null;
    }

}
