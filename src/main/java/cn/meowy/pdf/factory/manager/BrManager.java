package cn.meowy.pdf.factory.manager;

import cn.meowy.pdf.utils.FontUtils;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.dom4j.Element;

import java.util.Objects;

/**
 * 换行
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class BrManager extends PDFManager {

    /**
     * 节点处理器
     *
     * @param doc      文档
     * @param element  元素节点
     * @param data     数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        PageStruct struct = setting();
        if (TextDirection.HORIZONTAL.equals(struct.getTextDirection())) {
            // 水平对齐
            float y = getY(element) - FontUtils.height(struct.font, struct.fontSize) - struct.lineDistance;
            if (y < struct.margin.bottom) {
                // 换页,重置y
                newPage(doc);
                setY(struct.limitY);
            } else {
                // 下一行
                setY(y);
            }
            // 重置x
            setX(struct.margin.left);
        } else {
            // 垂直对齐
            float x = getX(element) + FontUtils.height(struct.font, struct.fontSize) + struct.lineDistance;
            if (x < struct.limitX) {
                // 换页,重置x
                setX(struct.margin.left);
                newPage(doc);
            } else {
                // 下一行
                setX(x);
            }
            // 重置y
            setY(struct.limitY);
        }
    }
}
