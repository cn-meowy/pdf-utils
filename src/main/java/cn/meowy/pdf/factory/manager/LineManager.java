package cn.meowy.pdf.factory.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.ExUtils;
import cn.meowy.pdf.utils.PDFWriteUtils;
import cn.meowy.pdf.utils.XmlAttribute;
import cn.meowy.pdf.utils.XmlUtils;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.dom4j.Element;

import java.util.Map;

/**
 * 换行
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class LineManager extends PDFManager {


    /**
     * 节点处理器
     *
     * @param doc     文档
     * @param element 元素节点
     * @param data    数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        float x = getX(element);
        float y = getY(element);
        Map<String, Object> params = getParams();
        Float width = getFloatAttribute(element, XmlAttribute.WIDTH);
        Float height = getFloatAttribute(element, XmlAttribute.HEIGHT);
        Assert.state(width > 0f, "线段宽度设置错误! {}", width);
        Assert.state(height > 0f, "线段宽度设置错误! {}", height);
        PageStruct struct = setting();
        if (struct.textDirection.equals(TextDirection.HORIZONTAL)) {
            // 水平
            float toX = x + width;
            PDFWriteUtils.drawLine(doc, getLastPageNum(doc), struct.getRectangle(), x, y, toX, y, height);
            setX(toX);
            setY(y);
        } else {
            // 垂直
            float toY = y - height;
            PDFWriteUtils.drawLine(doc, getLastPageNum(doc), struct.getRectangle(), x, y, x , toY, width);
            setX(x);
            setY(y);
        }
    }
}
