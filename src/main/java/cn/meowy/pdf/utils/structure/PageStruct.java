package cn.meowy.pdf.utils.structure;

import cn.hutool.core.map.MapUtil;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.TextDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 页面结构
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
@Data
@AllArgsConstructor
public class PageStruct {

    /**
     * 大小
     */
    public final PDRectangle rectangle;

    /**
     * 边距
     */
    public final Margin margin;

    /**
     * x限制坐标 最大x坐标
     */
    public final float limitX;

    /**
     * y限制坐标 最大y坐标
     */
    public final float limitY;

    /**
     * x中心坐标
     */
    public float centerX;

    /**
     * y中心坐标
     */
    public float centerY;

    /**
     * 字体
     */
    public final PDFont font;

    /**
     * 字体
     */
    public final float fontSize;

    /**
     * 颜色
     */
    public final Color pdColor;

    /**
     * 行间距
     */
    public final float lineDistance;

    /**
     * 文本方向
     */
    public final TextDirection textDirection;

    /**
     * 对齐方式
     */
    public final Alignment alignment;

    /**
     * 新位置
     */
    public final float newPosition;

    /**
     * 转map
     */
    public final Map<String, Object> map;

    public PageStruct(PDRectangle rectangle, Margin margin, PDFont font, float fontSize, Color pdColor, float lineDistance, TextDirection textDirection, Alignment alignment, float newPosition) {
        this.rectangle = rectangle;
        this.margin = margin;
        this.newPosition = newPosition;
        this.limitX = rectangle.getWidth() - margin.right;
        this.limitY = rectangle.getHeight() - margin.top;
        this.centerX = rectangle.getWidth() / 2;
        this.centerY = rectangle.getHeight() / 2;
        this.font = font;
        this.fontSize = fontSize;
        this.pdColor = pdColor;
        this.lineDistance = lineDistance;
        this.textDirection = textDirection;
        this.alignment = alignment;
        this.map = MapUtil.builder(new HashMap<String, Object>())
                .put("margin.left", this.margin.left)
                .put("margin.right", this.margin.right)
                .put("margin.top", this.margin.top)
                .put("margin.bottom", this.margin.bottom)
                .put("limitX", limitX)
                .put("limitY", limitY)
                .put("centerX", centerX)
                .put("centerY", centerY)
                .put("font", font.getName())
                .put("fontSize", fontSize)
                .put("lineDistance", lineDistance)
                .put("rectangle.height", rectangle.getHeight())
                .put("rectangle.width", rectangle.getWidth())
                .build();
    }


    @Data
    @AllArgsConstructor
    public static class Margin {
        /**
         * 边距 顶部
         */
        public final float top;

        /**
         * 边距 底部
         */
        public final float bottom;

        /**
         * 边距 左
         */
        public final float left;

        /**
         * 边距 右
         */
        public final float right;

    }

}
