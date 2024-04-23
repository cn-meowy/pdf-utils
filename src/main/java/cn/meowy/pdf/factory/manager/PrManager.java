package cn.meowy.pdf.factory.manager;

import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.dom4j.Element;

import java.util.Objects;

/**
 * 换页
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class PrManager extends PDFManager {

    /**
     * 节点处理器
     *
     * @param doc     文档
     * @param element 元素节点
     * @param data    数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        // 指定页面不处理
        PageStruct struct = setting();
        newPage(doc);
        setX(struct.margin.left);
        setY(struct.limitY);

    }
}
