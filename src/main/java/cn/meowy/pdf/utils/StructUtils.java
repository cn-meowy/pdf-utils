package cn.meowy.pdf.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.dom4j.Element;

import java.awt.*;
import java.util.List;
import java.util.Objects;

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
        PDRectangle rectangle = Objects.requireNonNull(AttributeUtils.parse(XmlUtils.getStr(element, XmlAttribute.RECTANGLE, null), PDRectangle.class, PDRectangle.A4));
        return new PageStruct(
                rectangle,
                margin(XmlUtils.getStr(element, XmlAttribute.MARGIN, null), new PageStruct.Margin(0f, 0f, 0f, 0f)),
                FontUtils.loadByName(document, XmlUtils.getStr(element, XmlAttribute.FONT, null)),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.FONT_SIZE, null), "10f")),
                ColorUtils.get(XmlUtils.getStr(element, XmlAttribute.COLOR, null), Color.BLACK),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.LINE_DISTANCE, null), "0f")),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.LINE_SPACE, null), "0f")),
                TextDirection.get(XmlUtils.getStr(element, XmlAttribute.TEXT_DIRECTION, null), TextDirection.HORIZONTAL),
                Alignment.get(XmlUtils.getStr(element, XmlAttribute.ALIGNMENT, null), Alignment.LEFT),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.FONT_SIZE, null), String.valueOf(rectangle.getHeight())))
        );
    }

    /**
     * 页面结构
     *
     * @param struct   结构
     * @param element  根节点
     * @param document pdf文档
     * @return 结构信息
     */
    public static PageStruct copyPageStruct(PageStruct struct, Element element, PDDocument document) {
        return new PageStruct(
                Objects.requireNonNull(AttributeUtils.parse(XmlUtils.getStr(element, XmlAttribute.RECTANGLE, null), PDRectangle.class, struct.rectangle)),
                margin(XmlUtils.getStr(element, XmlAttribute.MARGIN, null), struct.margin),
                StringUtils.isNotBlank(XmlUtils.getStr(element, XmlAttribute.FONT, null)) ? FontUtils.loadByName(document, XmlUtils.getStr(element, XmlAttribute.FONT, null)) : struct.getFont(),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.FONT_SIZE, null), String.valueOf(struct.fontSize))),
                ColorUtils.get(XmlUtils.getStr(element, XmlAttribute.COLOR, null), struct.pdColor),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.LINE_DISTANCE, null), String.valueOf(struct.lineDistance))),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.LINE_SPACE, null), String.valueOf(struct.lineSpace))),
                TextDirection.get(XmlUtils.getStr(element, XmlAttribute.TEXT_DIRECTION, null), struct.textDirection),
                Alignment.get(XmlUtils.getStr(element, XmlAttribute.ALIGNMENT, null), struct.alignment),
                Float.parseFloat(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.FONT_SIZE, null), String.valueOf(struct.newPosition)))
        );
    }

    /**
     * 边距解析
     *
     * @param defaultMargin 默认边距
     * @param marginStr     边距配置
     * @return 边距
     */
    public static PageStruct.Margin margin(String marginStr, PageStruct.Margin defaultMargin) {
        if (StrUtil.isNotBlank(marginStr)) {
            List<String> split = StrUtil.split(marginStr, CharSequenceUtil.SPACE);
            Assert.state(CollUtil.size(split) == 4, "边距配置错误!");
            return ExUtils.execute(() -> new PageStruct.Margin(Float.parseFloat(split.get(0)), Float.parseFloat(split.get(1)), Float.parseFloat(split.get(2)), Float.parseFloat(split.get(3))), "边距配置错误!");
        }
        return defaultMargin;
    }

}
