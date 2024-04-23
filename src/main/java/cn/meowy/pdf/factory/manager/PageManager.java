package cn.meowy.pdf.factory.manager;

import cn.meowy.pdf.utils.StructUtils;
import cn.meowy.pdf.utils.XmlAttribute;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.dom4j.Element;

import java.util.Objects;

/**
 * 页面处理
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class PageManager extends PDFManager {


    /**
     * 节点处理器
     *
     * @param doc      文档
     * @param element  元素节点
     * @param data     数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        PageStruct struct = StructUtils.pageStruct(element, doc);
        // 保存页面配置
        setCache(PAGE_SETTING, struct);
        // 设置初始坐标
        setCoordinate(element, struct);
        // 处理子元素
        element.elements().forEach(e -> get(e.getName()).handler(doc, e, data));
    }

    /**
     * 设置坐标
     *
     * @param element 节点
     * @param struct  元素
     */
    public void setCoordinate(Element element, PageStruct struct) {
        Float x = getFloatAttribute(element, XmlAttribute.X);
        Float y = getFloatAttribute(element, XmlAttribute.Y);
        if (Objects.nonNull(x)) {
            setCache(X_COORDINATE, x);
        } else if (Objects.equals(struct.textDirection, TextDirection.HORIZONTAL)) {
            // 文本设置为水平对齐
            if (Objects.equals(struct.alignment, Alignment.RIGHT)) {
                setCache(X_COORDINATE, struct.margin.left);
            } else if (Objects.equals(struct.alignment, Alignment.CENTER)) {
                setCache(X_COORDINATE, struct.centerX);
            } else {
                setCache(X_COORDINATE, struct.limitX);
            }
        }
        if (Objects.nonNull(y)) {
            setCache(Y_COORDINATE, y);
        } else if (Objects.equals(struct.textDirection, TextDirection.VERTICAL)) {
            if (Objects.equals(struct.alignment, Alignment.BOTTOM)) {
                setCache(Y_COORDINATE, struct.margin.bottom);
            } else if (Objects.equals(struct.alignment, Alignment.MIDDLE)) {
                setCache(Y_COORDINATE, struct.centerY);
            } else {
                setCache(Y_COORDINATE, struct.limitY);
            }
        }
    }


}
