package cn.meowy.pdf.factory.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.TemplateUtils;
import cn.meowy.pdf.utils.XmlAttribute;
import cn.meowy.pdf.utils.XmlElement;
import cn.meowy.pdf.utils.XmlUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 挂载处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class MountManager extends PDFManager {

    /**
     * 节点处理器
     *
     * @param doc      文档
     * @param element  元素节点
     * @param data     数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        // 获取挂载的模块文件
        String xmlName = XmlUtils.getStr(element, XmlAttribute.SLOT, getParams());
        Assert.state(StrUtil.isNotBlank(xmlName), "未声明挂载插件名称!");
        Document document = TemplateUtils.loadTemplate(xmlName, data);
        Assert.state(XmlUtils.rootElementCheck(document, XmlElement.SLOT), "模板根节点错误!\n{}", xmlName);
        childHandlers(doc, document.getRootElement(), data);
    }
}
