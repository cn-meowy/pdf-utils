package cn.meowy.pdf.factory.manager;

import cn.meowy.pdf.utils.StructUtils;
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
        PageStruct struct = StructUtils.copyPageStruct(globalSetting(), element, doc);
        setCache(PAGE_SETTING, struct); // 重置配置
        newPage(doc);                   // 创建新页
        setCoordinate(element, struct); // 重置x、y坐标
    }
}
