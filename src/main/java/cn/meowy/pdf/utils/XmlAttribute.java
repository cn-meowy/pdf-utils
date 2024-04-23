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
     * 对齐方式
     */
    String ALIGNMENT = "alignment";

    /**
     * 边距
     */
    String MARGIN = "margin";

    /**
     * 风格: underline-下划线
     */
    String STYLE = "style";

    /**
     * 浮动方式: top-上浮动,floor-下浮
     */
    String FLOAT = "float";

    /**
     * 下划线
     */
    String UNDERLINE = "underline";

    /**
     * 宽度
     */
    String WIDTH = "width";

    /**
     * 表格列宽度
     */
    String COLUMN_WIDTH = "column-width";

    /**
     * 高度
     */
    String HEIGHT = "height";

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
     * 表格边距
     */
    String PADDING = "padding";

    /**
     * 新位置
     */
    String NEW_POSITION = "new-position";

    /**
     * 块
     */
    String BLOCK = "block";

    /**
     * 表格 列合并
     */
    String COLUM_SPAN = "colum-span";

    /**
     * 表格 行合并
     */
    String ROW_SPAN = "row-span";

    /**
     * 换行符 true-启用,false-忽略
     */
    String LINE_BREAK = "line-break";

    /**
     * beginPage
     */
    String BEGIN_PAGE = "beginPage";

    /**
     * endPage
     */
    String END_PAGE = "endPage";
}
