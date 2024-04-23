package cn.meowy.pdf.struct;

import cn.hutool.core.map.multi.Table;
import cn.meowy.pdf.utils.FontUtils;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.List;

/**
 * 居中对齐
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class AlignmentCenter extends AlignmentHandler {
    /**
     * 行空间
     */
    protected float beginX;

    /**
     * 行空间
     */
    protected float endX;

    /**
     * 单行总长度
     */
    protected float totalWidth;

    protected float space;

    public AlignmentCenter(String context, PDFont font, float fontSize, float lineDistance, float x, float y, float newPosition, float limitLeft, float limitRight, float limitTop, float limitBottom, PDRectangle rectangle, boolean underline) {
        super(context, font, fontSize, lineDistance, x, y, newPosition, limitLeft, limitRight, limitTop, limitBottom, rectangle, underline);
        this.horizontal = true;
        this.space = Float.min(this.x - this.limitLeft, this.limitRight - this.x);
        this.beginX = this.x - space;
        this.endX = this.x + space;
    }


    /**
     * 下一行
     */
    @Override
    protected void nextLine() {
        if (this.totalWidth < 2 * space) {
            // 单行可容纳
            float half = this.space - (this.totalWidth / 2);
            this.x = this.beginX + half;
        } else {
            // 重置x坐标
            this.x = this.beginX;
        }
        float tempY = this.y - height - lineDistance;
        if (tempY < limitBottom) {
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
        this.x = this.newPosition - this.space;
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
        if (tempX > this.endX) {
            nextLine();
        }
        // 总长度减小
        this.totalWidth -= width;
        table.put(this.x, this.y, String.valueOf(ch));
        // 移动到结尾
        this.x += width;
    }

    /**
     * 获取PDF页面内容
     *
     * @return pages
     */
    @Override
    public List<Table<Float, Float, String>> pages() {
        for (StringBuilder text : toLine()) {
            String str = text.toString();
            this.totalWidth = FontUtils.width(this.font, str, this.fontSize);
            if (this.totalWidth > (this.space * 2)) {
                // 超过单行, 重置x坐标
                this.x -= this.space;
            } else {
                // 单行
                this.x -= (this.totalWidth / 2);
            }
            // 输出
            for (char ch : str.toCharArray()) {
                put(ch, FontUtils.width(this.font, ch, this.fontSize));
            }
        }
        return this.pages;
    }

}
