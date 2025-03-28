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
        try {
            PageStruct struct = StructUtils.pageStruct(element, doc);   // 读取配置
            setCache(GLOBAL_PAGE_SETTING, struct);                      // 保存全局性页面配置
            setCache(PAGE_SETTING, struct);                             // 保存页面配置
            setCoordinate(element, struct);                             // 设置初始坐标
            element.elements().forEach(e -> get(e.getName()).handler(doc, e, data)); // 处理子元素
        } finally {
            CACHE.remove();                                             // 清理缓存
            PAGE_SETTING_CACHE.remove();                                // 清理缓存
        }
    }

}
