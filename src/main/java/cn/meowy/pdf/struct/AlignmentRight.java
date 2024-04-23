package cn.meowy.pdf.struct;

import cn.hutool.core.map.multi.Table;
import cn.meowy.pdf.utils.FontUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.List;

/**
 * 右对齐
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class AlignmentRight extends AlignmentHandler {

    /**
     * 行空间
     */
    protected float lineSpace;

    /**
     * 总长度
     */
    protected float totalWidth;

    public AlignmentRight(String context, PDFont font, float fontSize, float lineDistance, float x, float y, float newPosition, float limitLeft, float limitRight, float limitTop, float limitBottom, PDRectangle rectangle, boolean underline) {
        super(context, font, fontSize, lineDistance, x, y, newPosition, limitLeft, limitRight, limitTop, limitBottom, rectangle, underline);
        this.horizontal = true;
        this.lineSpace = this.x;
    }

    /**
     * 下一行
     */
    @Override
    protected void nextLine() {
        this.lineSpace = this.newPosition;
        resetX();
        float tempY = this.y - this.height - this.lineDistance;
        if (tempY < this.limitBottom) {
            // 换页
            nextPage();
        } else {
            this.y = tempY;
        }
    }

    /**
     * 下一页
     */
    @Override
    protected void nextPage() {
        super.nextPage();
        this.lineSpace = this.newPosition;
        this.y = this.limitTop;
    }

    /**
     * 添加文本
     *
     * @param ch    字符
     * @param width 宽度
     */
    @Override
    protected void put(char ch, float width) {
        float tempX = this.x + width;
        if (tempX > lineSpace) {
            nextLine();
        }
        table.put(this.x, this.y, String.valueOf(ch));
        // 总长度减小
        this.totalWidth -= width;
        // 将坐标移动到结尾
        this.x += width;
    }

    /**
     * 获取PDF页面内容
     *
     * @return pages
     */
    @Override
    public List<Table<Float, Float, String>> pages() {
        for (StringBuilder sb : toLine()) {
            String text = sb.toString();
            this.totalWidth = FontUtils.width(this.font, text, this.fontSize);
            // 重置x
            resetX();
            // 输出
            for (char ch : text.toCharArray()) {
                put(ch, FontUtils.width(this.font, ch, this.fontSize));
            }
        }
        return this.pages;
    }

    /**
     * 重置x
     */
    private void resetX() {
        if (this.lineSpace < this.totalWidth) {
            // 文字总宽度大于行空间
            this.x = this.limitLeft;
        } else {
            // 单行
            this.x -= this.totalWidth;
        }
    }
}
