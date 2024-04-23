package cn.meowy.pdf.struct;

import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * 左对齐
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class AlignmentLeft extends AlignmentHandler {

    public AlignmentLeft(String context, PDFont font, float fontSize, float lineDistance, float x, float y, float newPosition, float limitLeft, float limitRight, float limitTop, float limitBottom, PDRectangle rectangle, boolean underline) {
        super(context, font, fontSize, lineDistance, x, y, newPosition, limitLeft, limitRight, limitTop, limitBottom, rectangle, underline);
        this.horizontal = true;
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
        if (tempX > limitRight) {
            // 换行
            nextLine();
        }
        table.put(this.x, this.y, String.valueOf(ch));
        // 移动到结束未知
        this.x += width;
    }

    /**
     * 下一行
     */
    protected void nextLine() {
        this.x = this.newPosition;
        this.y = this.y - this.height - this.lineDistance;
        if (this.y < limitBottom) {
            // 换页
            nextPage();
        }
    }

    /**
     * 下一行
     */
    protected void nextPage() {
        super.nextPage();
        // 重置x,y
        this.x = newPosition;
        this.y = this.limitTop;
    }

}
