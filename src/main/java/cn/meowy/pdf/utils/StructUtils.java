package cn.meowy.pdf.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.dom4j.Element;
import org.vandeseer.easytable.settings.HorizontalAlignment;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结构工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class StructUtils {

    /**
     * 页面结构
     *
     * @param element  根节点
     * @param document pdf文档
     * @return 结构信息
     */
    public static PageStruct pageStruct(Element element, PDDocument document) {
        return new PageStruct(
                AttributeUtils.parse(XmlUtils.getStr(element, XmlAttribute.RECTANGLE, null), PDRectangle.class, PDRectangle.A4),
                margin(XmlUtils.getStr(element, XmlAttribute.MARGIN, null)),
                FontUtils.loadByName(document, XmlUtils.getStr(element, XmlAttribute.FONT, null)),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.FONT_SIZE, null), "10f")),
                ColorUtils.get(XmlUtils.getStr(element, XmlAttribute.COLOR, null), Color.BLACK),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.LINE_DISTANCE, null), "0f")),
                ObjectUtil.defaultIfNull(TextDirection.valueOf(XmlUtils.getStr(element, XmlAttribute.TEXT_DIRECTION, null)), TextDirection.HORIZONTAL),
                ObjectUtil.defaultIfNull(Alignment.valueOf(XmlUtils.getStr(element, XmlAttribute.ALIGNMENT, null)), Alignment.LEFT),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.FONT_SIZE, null), String.valueOf(PDRectangle.A4.getHeight())))
        );
    }

    /**
     * 边距解析
     *
     * @param marginStr 边距配置
     * @return 边距
     */
    public static PageStruct.Margin margin(String marginStr) {
        if (StrUtil.isNotBlank(marginStr)) {
            List<String> split = StrUtil.split(marginStr, CharSequenceUtil.SPACE);
            Assert.state(CollUtil.size(split) == 4, "边距配置错误!");
            return ExUtils.execute(() -> new PageStruct.Margin(Float.parseFloat(split.get(0)), Float.parseFloat(split.get(1)), Float.parseFloat(split.get(2)), Float.parseFloat(split.get(3))), "边距配置错误!");
        }
        return new PageStruct.Margin(0f, 0f, 0f, 0f);
    }

}
