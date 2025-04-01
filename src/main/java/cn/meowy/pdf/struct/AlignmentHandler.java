package cn.meowy.pdf.struct;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import cn.hutool.core.util.CharUtil;
import cn.meowy.pdf.utils.FontUtils;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.ArrayList;
import java.util.List;

/**
 * 对齐方式处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public abstract class AlignmentHandler {
    /**
     * 页面
     */
    protected List<Table<Float, Float, Character>> pages;

    /**
     * 当前绘制的页面 坐标 及 内容
     */
    protected Table<Float, Float, Character> table;

    /**
     * 文字高度
     */
    protected float height;

    /**
     * 行间距
     */
    @Getter
    protected float lineDistance;

    /**
     * 初始x坐标
     */
    @Getter
    protected float x;

    /**
     * 初始y坐标
     */
    @Getter
    protected float y;

    /**
     * 下一行坐标(根据排版方式确定是x轴或y轴)
     */
    protected float newPosition;

    /**
     * 边距 左
     */
    protected float limitLeft;

    /**
     * 边距 右
     */
    protected float limitRight;

    /**
     * 边距 上
     */
    protected float limitTop;

    /**
     * 边距 下
     */
    protected float limitBottom;

    /**
     * 文字
     */
    protected String context;

    /**
     * 字体
     */
    protected PDFont font;

    /**
     * 字体大小
     */
    protected float fontSize;

    /**
     * 字体大小
     */
    protected PDRectangle rectangle;

    /**
     * 下划线
     */
    @Getter
    protected boolean underline;

    /**
     * 水平
     */
    @Getter
    protected boolean horizontal;

    public AlignmentHandler(String context, PDFont font, float fontSize, float lineDistance, float x, float y, float newPosition, float limitLeft, float limitRight, float limitTop, float limitBottom, PDRectangle rectangle, boolean underline) {
        this.context = context;
        this.font = font;
        this.fontSize = fontSize;
        this.lineDistance = lineDistance;
        this.x = x;
        this.y = y;
        this.newPosition = newPosition;
        this.limitLeft = limitLeft;
        this.limitRight = rectangle.getWidth() - limitRight;
        this.limitTop = rectangle.getHeight() - limitTop;
        this.limitBottom = limitBottom;
        this.rectangle = rectangle;
        this.underline = underline;

        this.height = FontUtils.height(font, fontSize);
        this.pages = new ArrayList<>();
        this.table = new RowKeyTable<>();
        this.pages.add(this.table);
    }

    /**
     * 下一行
     */
    protected abstract void nextLine();

    /**
     * 下一页
     */
    protected void nextPage() {
        this.table = new RowKeyTable<>();
        this.pages.add(this.table);
    }

    /**
     * 添加文本
     *
     * @param ch    字符
     * @param width 宽度
     */
    protected abstract void put(char ch, float width);


    /**
     * 获取PDF页面内容
     *
     * @return pages
     */
    public List<Table<Float, Float, Character>> pages() {
        for (char ch : this.context.toCharArray()) {
            if (isBreak(ch)) {
                // 换行符
                nextLine();
            } else {
                put(ch, FontUtils.width(this.font, ch, this.fontSize));
            }
        }
        return this.pages;
    }

    /**
     * 文本转行列
     *
     * @return 行列
     */
    protected List<StringBuilder> toLine() {
        StringBuilder last = new StringBuilder();
        List<StringBuilder> lines = CollUtil.newArrayList(last);
        for (char ch : this.context.toCharArray()) {
            if (isBreak(ch)) {
                last = new StringBuilder();
                lines.add(last);
            } else {
                last.append(ch);
            }
        }
        return lines;
    }

    /**
     * 判断字符是否为换行符
     *
     * @param ch 字符
     * @return true-是,false-否
     */
    protected boolean isBreak(char ch) {
        return CharUtil.equals(ch, CharUtil.LF, false) || CharUtil.equals(ch, CharUtil.CR, false);
    }

}
