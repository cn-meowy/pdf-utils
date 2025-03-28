package cn.meowy.pdf.factory.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.struct.AlignmentHandler;
import cn.meowy.pdf.utils.*;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 页眉页脚处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class HeaderFooterManager extends PDFManager {

    /**
     * 节点处理器
     *
     * @param doc     文档
     * @param element 元素节点
     * @param data    数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        // 记录当前页
        int startPage = doc.getNumberOfPages();
        // 子节点处理
        childHandlers(doc, element, data);
        // 处理完毕后获取处理完毕的页数
        int endPage = doc.getNumberOfPages();
        // 读取指定模版
        String templatePath = XmlUtils.getStr(element, XmlAttribute.SLOT, getParams());
        Assert.state(StrUtil.isNotBlank(templatePath), "页眉模版未指定! {}", templatePath);
        Document template = TemplateUtils.loadTemplate(templatePath, data);
        Assert.state(XmlUtils.rootElementCheck(template, XmlElement.SLOT), "模板根节点错误!\n{}", templatePath);
        Element rootElement = template.getRootElement();
        PageStruct struct = setting();
        // 将内容输出到指定页数
        String text = rootElement.getStringValue();
        float fontSize = ObjectUtil.defaultIfNull(getFloatAttribute(rootElement, XmlAttribute.FONT_SIZE), struct.fontSize);
        boolean underlineFlag = Boolean.parseBoolean(XmlUtils.getStr(rootElement, XmlAttribute.UNDERLINE, getParams()));
        Class<? extends AlignmentHandler> alignment = HANDLER.get(StrUtil.nullToDefault(XmlUtils.getStr(rootElement, XmlAttribute.ALIGNMENT, getParams()), struct.alignment.name()));
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>()).put(XmlAttribute.END_PAGE, endPage - startPage).build();
        if (StrUtil.isNotBlank(text)) {
            for (int i = startPage; i < endPage; i++) {
                PDPage page = doc.getPages().get(i);
                Map<String, Object> pageParams = getParams(doc, i);
                params.put(XmlAttribute.BEGIN_PAGE, i + 1);
                String format = StringUtils.format(text, params);
                float x = getFloatAttribute(rootElement, XmlAttribute.X, pageParams);
                float y = getFloatAttribute(rootElement, XmlAttribute.Y, pageParams);
                AlignmentHandler handler = ReflectUtil.newInstance(alignment, format, struct.font, fontSize, struct.lineDistance, x, y, x, 0f, 0f, 0f, 0f, page.getMediaBox(), underlineFlag);
                PDFWriteUtils.write(doc, i, struct.font, fontSize, struct.pdColor, handler);
            }
        }
    }
}
