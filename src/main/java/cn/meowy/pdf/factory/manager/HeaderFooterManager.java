package cn.meowy.pdf.factory.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.struct.AlignmentHandler;
import cn.meowy.pdf.utils.*;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.structure.PageStruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.dom4j.Document;
import org.dom4j.Element;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 页眉页脚处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class HeaderFooterManager extends PDFManager {

    protected static final Map<String, Field> STYLE_KEY;         // 样式键

    static {
        STYLE_KEY = Arrays.stream(ReflectUtil.getFields(Style.class)).collect(Collectors.toMap(f -> SET + StrUtil.upperFirst(f.getName()), field -> field));
    }

    /**
     * 节点处理器
     *
     * @param doc     文档
     * @param element 元素节点
     * @param data    数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        int startPage = doc.getNumberOfPages();                                                                                                                                             // 记录当前页
        childHandlers(doc, element, data);                                                                                                                                                  // 子节点处理
        int endPage = doc.getNumberOfPages();                                                                                                                                               // 处理完毕后获取处理完毕的页数
        Map<String, Object> params = new HashMap<>(getParams());                                                                                                                                           // 获取动态参数
        String templatePath = XmlUtils.getStr(element, XmlAttribute.SLOT, params);                                                                                                          // 读取指定模版
        Assert.state(StrUtil.isNotBlank(templatePath), "页眉模版未指定! {}", templatePath);
        Document template = TemplateUtils.loadTemplate(templatePath, data);                                                                                                                 // 加载模板文档,写入动态数据
        Assert.state(XmlUtils.rootElementCheck(template, XmlElement.SLOT), "模板根节点错误!\n{}", templatePath);
        Element rootElement = template.getRootElement();                                                                                                                                    // 根节点
        PageStruct struct = setting();                                                                                                                                                      // 全局设置
        String text = rootElement.getStringValue();                                                                                                                                         // 将内容输出到指定页数
        Style style = loadStyle(STYLE_KEY, Style.builder()
                .color(struct.pdColor).fontSize(struct.fontSize).lineDistance(struct.lineDistance).alignment(struct.alignment).underline(false)
                .build(), rootElement, params);                                                                                                                                             // 初始化节点属性
        Class<? extends AlignmentHandler> alignment = HANDLER.get(style.alignment.name());                                                                                                  // 文档对齐方式
        params.put(XmlAttribute.END_PAGE, endPage - startPage);                                                                                                                             // 计算总页数并设置到动态参数中
        if (StrUtil.isNotBlank(text)) {                                                                                                                                                     // 内容不为空输出到文档中
            for (int i = startPage; i < endPage; i++) {                                                                                                                                     // 从指定起始页开始输出
                PDPage page = doc.getPages().get(i);                                                                                                                                        // 获取当前页
                Map<String, Object> pageParams = getParams(doc, i);                                                                                                                         // 从缓存中获取当前操作页信息
                params.put(XmlAttribute.BEGIN_PAGE, i + 1);                                                                                                                                 // 计算当前页页码并设置到动态参数中
                String format = StringUtils.format(text, params);                                                                                                                           // 使用动态参数格式化文本
                float x = getFloatAttribute(rootElement, XmlAttribute.X, pageParams);                                                                                                       // 获取输出坐标x
                float y = getFloatAttribute(rootElement, XmlAttribute.Y, pageParams);                                                                                                       // 获取输出坐标y
                PDFont font = loadFont(doc, style.font);                                                                                                                                    // 加载字体
                AlignmentHandler handler = ReflectUtil.newInstance(alignment, format, font, style.fontSize, style.lineDistance, x, y, x, 0f, 0f, 0f, 0f, page.getMediaBox(), style.underline);
                PDFWriteUtils.write(doc, i, font, style.fontSize, style.color, handler);                                                                                                    // 写入内容
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Style {
        protected String font;
        protected Color color;
        protected Float fontSize;
        protected Boolean underline;
        protected Alignment alignment;
        protected Float lineDistance;
    }
}
