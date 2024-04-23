package cn.meowy.pdf.struct;

import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * 上对齐
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class AlignmentTop extends AlignmentHandler {

    protected float maxWidth;

    public AlignmentTop(String context, PDFont font, float fontSize, float lineDistance, float x, float y, float newPosition, float limitLeft, float limitRight, float limitTop, float limitBottom, PDRectangle rectangle, boolean underline) {
        super(context, font, fontSize, lineDistance, x, y, newPosition, limitLeft, limitRight, limitTop, limitBottom, rectangle, underline);
        this.horizontal = false;
        this.maxWidth = 0f;
    }

    /**
     * 添加文本
     *
     * @param ch    字符
     * @param width 宽度
     */
    public void put(char ch, float width) {
        float tempY = this.y - height;
        if (tempY < limitBottom) {
            // 换行
            nextLine();
        }
        // 计算字体最大宽度
        maxWidth(width);
        table.put(this.x, this.y, String.valueOf(ch));
        this.y = this.y - this.height - this.lineDistance;
    }

    /**
     * 下一行
     */
    public void nextLine() {
        this.y = newPosition;
        // 重置最大字体宽度
        this.maxWidth = 0f;
        this.x = this.x + this.maxWidth + this.lineDistance;
        if (this.x > limitRight) {
            // 换页
            nextPage();
        }
    }

    /**
     * 获取最大宽度
     *
     * @param width 宽度
     */
    private void maxWidth(float width) {
        this.maxWidth = Float.max(maxWidth, this.maxWidth);
    }

    /**
     * 下一页
     */
    public void nextPage() {
        super.nextPage();
        // 重置x,y
        this.x = this.limitLeft;
        this.y = newPosition;
        // 重置最大字体宽度
        this.maxWidth = 0f;
    }

}
