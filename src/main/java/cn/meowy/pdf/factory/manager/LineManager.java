package cn.meowy.pdf.factory.manager;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.factory.PDFManager;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.dom4j.Element;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 换行
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class LineManager extends PDFManager {

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
        Style style = loadStyle(STYLE_KEY, Style.builder().x(currentX()).y(currentY()).color(struct.pdColor).direction(struct.textDirection).weight(1F).build(), element, getParams());
        if (Objects.equals(TextDirection.HORIZONTAL, style.direction)) {                                                            // 水平
            float toX = style.x + style.getWidth();                                                                                  // x结束位置
            PDFWriteUtils.drawLine(doc, style.color, getLastPageNum(doc), style.x, style.y, toX, style.y, style.weight);            // 划线
            setLocation(toX, style.y);                                                                                              // 设置x,y坐标
        } else {                                                                                                                    // 垂直
            float toY = style.y - style.height;                                                                                     // y结束位置
            PDFWriteUtils.drawLine(doc, style.color, getLastPageNum(doc), style.x, style.y, style.x, toY, style.weight);            // 划线
            setLocation(style.x, toY);                                                                                              // 设置x,y坐标
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Style {
        protected TextDirection direction = TextDirection.HORIZONTAL;
        protected Color color = Color.BLACK;
        protected Float x;
        protected Float y;
        protected Float width;
        protected Float height;
        protected Float weight;
    }
}
