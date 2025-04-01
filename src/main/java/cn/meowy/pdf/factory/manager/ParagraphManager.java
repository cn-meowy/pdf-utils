package cn.meowy.pdf.factory.manager;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.struct.AlignmentHandler;
import cn.meowy.pdf.utils.XmlAttribute;
import cn.meowy.pdf.utils.XmlUtils;
import cn.meowy.pdf.utils.annotation.StyleProperty;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.dom4j.Element;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 段落处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class ParagraphManager extends PDFManager {

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
        PageStruct struct = setting();
        float oriX = currentX();                                                                                            // 记录x浮动属性
        float oriY = currentY();                                                                                            // 记录y浮动属性
        Style style = loadStyle(STYLE_KEY, Style.builder()
                .x(oriX)
                .y(oriY)
                .floatFlag(false)
                .underline(false)
                .lineBreak(false)
                .color(struct.pdColor)
                .lineDistance(struct.lineDistance)
                .fontSize(struct.fontSize)
                .direction(struct.textDirection)
                .alignment(struct.alignment)
                .build(), element, getParams());
        Float newPosition = getNewPosition(style, struct);
        String text = element.getText();                                                                                    // 文本
        if (!style.lineBreak) {                                                                                             // 是否识别换行符
            text = StrUtil.removeAll(text, CharUtil.CR, CharUtil.LF);
        }
        Class<? extends AlignmentHandler> alignment = HANDLER.get(StrUtil.nullToDefault(XmlUtils.getStr(element, XmlAttribute.ALIGNMENT, getParams()), struct.alignment.name()));
        PDFont font = loadFont(doc, style.font);
        if (StrUtil.isNotBlank(text)) {                                                                                     // 文本不为空则输出
            AlignmentHandler handler = ReflectUtil.newInstance(alignment, text, font, style.fontSize, style.lineDistance, style.x, style.y, newPosition, struct.margin.left, struct.margin.right, struct.margin.top, struct.margin.bottom, struct.getRectangle(), style.underline);
            PDFWriteUtils.write(doc, getLastPageNum(doc), font, style.fontSize, style.color, handler);                      // 写入文档
            setLocation(handler.getX(), handler.getY());                                                                    // x,y坐标移动至结束位置
        }
        if (style.floatFlag) {                                                                                              // 设置为浮动则还原x,y
            if (TextDirection.HORIZONTAL.equals(style.direction)) {                                                         // 文字方向水平
                setY(oriY);                                                                                                 // 重置y坐标
            } else {                                                                                                        // 垂直
                setX(oriX);                                                                                                 // 重置x坐标
            }
        }
    }

    /**
     * 获取换行后新文本起始坐标位置
     *
     * @param style  样式
     * @param struct 当前页配置属性
     * @return 新行起始坐标
     */
    private Float getNewPosition(Style style, PageStruct struct) {
        Float newPosition = style.newPosition;
        if (Objects.isNull(newPosition)) {                                                                                  // 未指定新行位置
            if (TextDirection.HORIZONTAL.equals(style.direction)) {                                                         // 水平
                newPosition = Alignment.LEFT.equals(style.alignment) ? struct.margin.left : style.x;
            } else {                                                                                                        // TODO 垂直
                newPosition = struct.limitY;
            }
        }
        return newPosition;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Style {
        protected Color color;
        protected String font;
        protected Float fontSize;
        protected Float x;
        protected Float y;
        @StyleProperty("line-distance")
        protected Float lineDistance;
        @StyleProperty("new-position")
        protected Float newPosition;
        @StyleProperty("float")
        protected Boolean floatFlag;            // 浮动标志
        protected Boolean underline;            // 下划线标志
        protected Boolean lineBreak;            // 是否识别换行符
        protected TextDirection direction;
        protected Alignment alignment;
    }

}
