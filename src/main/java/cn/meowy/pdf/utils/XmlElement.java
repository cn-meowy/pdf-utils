package cn.meowy.pdf.utils;

/**
 * xml元素
 *
 * @author: Mr.Zou
 * @date: 2024/4/18
 **/
public interface XmlElement {

    /**
     * 页面元素
     */
    String PAGE = "page";

    /**
     * 段落元素
     */
    String PARAGRAPH = "paragraph";

    /**
     * 表格元素
     */
    String TABLE = "table";

    /**
     * 表格行
     */
    String TR = "tr";

    /**
     * 表格单元格
     */
    String TD = "td";

    /**
     * 换行符
     */
    String BR = "br";

    /**
     * 换页符
     */
    String PR = "pr";

    /**
     * 插槽
     */
    String MOUNT = "mount";

    /**
     * 插槽
     */
    String SLOT = "slot";

    /**
     * 线条
     */
    String LINE = "line";

    /**
     * 页眉
     */
    String HEADER = "header";

    /**
     * 页脚
     */
    String FOOTER = "footer";


}
