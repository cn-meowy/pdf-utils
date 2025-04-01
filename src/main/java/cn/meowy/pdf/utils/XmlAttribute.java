package cn.meowy.pdf.utils;

/**
 * xml属性
 *
 * @author: Mr.Zou
 * @date: 2024/4/18
 **/
public interface XmlAttribute {

    /**
     * 矩形大小
     * A4、A5、A6 .....
     */
    String RECTANGLE = "rectangle";

    /**
     * 字体
     */
    String FONT = "font";

    /**
     * 字体大小
     */
    String FONT_SIZE = "font-size";

    /**
     * 颜色
     */
    String COLOR = "color";

    /**
     * 行间距
     */
    String LINE_DISTANCE = "line-distance";

    /**
     * 文字间距
     */
    String LINE_SPACE = "line-space";

    /**
     * 对齐方式
     */
    String ALIGNMENT = "alignment";

    /**
     * 边距
     */
    String MARGIN = "margin";

    /**
     * 插槽
     */
    String SLOT = "slot";

    /**
     * x坐标
     */
    String X = "x";

    /**
     * y坐标
     */
    String Y = "y";

    /**
     * 文本方向
     * VERTICAL-垂直,HORIZONTAL-水平
     */
    String TEXT_DIRECTION = "text-direction";

    /**
     * beginPage
     */
    String BEGIN_PAGE = "beginPage";

    /**
     * endPage
     */
    String END_PAGE = "endPage";
}
