package cn.meowy.pdf.factory.manager;

import cn.hutool.core.util.*;
import cn.meowy.pdf.struct.*;
import cn.meowy.pdf.utils.PDFWriteUtils;
import cn.meowy.pdf.utils.XmlAttribute;
import cn.meowy.pdf.utils.XmlUtils;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 段落处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class ParagraphManager extends PDFManager {

    /**
     * 节点处理器
     *
     * @param doc     文档
     * @param element 元素节点
     * @param data    数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        // 记录x,y,浮动属性
        float oriX = getCache(X_COORDINATE);
        float oriY = getCache(Y_COORDINATE);
        boolean floatFlag = Boolean.parseBoolean(XmlUtils.getStr(element, XmlAttribute.FLOAT, getParams()));
        boolean underlineFlag = Boolean.parseBoolean(XmlUtils.getStr(element, XmlAttribute.UNDERLINE, getParams()));
        // 读取段落属性信息
        float x = getX(element);
        float y = getY(element);
        PageStruct struct = setting();
        Float newPosition = getFloatAttribute(element, XmlAttribute.NEW_POSITION);
        Float fontSize = ObjectUtil.defaultIfNull(getFloatAttribute(element, XmlAttribute.FONT_SIZE), struct.fontSize);
        if (Objects.isNull(newPosition)) {
            if (TextDirection.HORIZONTAL.equals(struct.textDirection)) {
                // 水平
                newPosition = Alignment.LEFT.equals(struct.alignment) ? struct.margin.left : x;
            } else {
                // TODO 垂直
                newPosition = struct.limitY;
            }
        }
        String text = element.getText();
        // 是否识别换行符
        boolean lineBreak = Boolean.parseBoolean(XmlUtils.getStr(element, XmlAttribute.LINE_BREAK, getParams()));
        if (!lineBreak) {
            text = StrUtil.removeAll(text, CharUtil.CR, CharUtil.LF);
        }
        Class<? extends AlignmentHandler> alignment = HANDLER.get(StrUtil.nullToDefault(XmlUtils.getStr(element, XmlAttribute.ALIGNMENT, getParams()), struct.alignment.name()));
        if (StrUtil.isNotBlank(text)) {
            AlignmentHandler handler = ReflectUtil.newInstance(alignment, text, struct.font, fontSize, struct.lineDistance, x, y, newPosition, struct.margin.left, struct.margin.right, struct.margin.top, struct.margin.bottom, struct.getRectangle(), underlineFlag);
            PDFWriteUtils.write(doc, getLastPageNum(doc), PDRectangle.A4, struct.font, fontSize, struct.pdColor, handler);
            // set x y
            setX(handler.getX());
            setY(handler.getY());
        }
        // 设置为浮动则还原x,y
        if (floatFlag) {
            if (TextDirection.HORIZONTAL.equals(struct.textDirection)) {
                // 水平
                setY(oriY);
            } else {
                // 垂直
                setX(oriX);
            }
        }
    }

}
