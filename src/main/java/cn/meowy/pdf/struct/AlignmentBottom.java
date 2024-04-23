package cn.meowy.pdf.struct;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * 下对齐
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class AlignmentBottom extends AlignmentHandler {


    public AlignmentBottom(String context, PDFont font, float fontSize, float lineDistance, float x, float y, float newPosition, float limitLeft, float limitRight, float limitTop, float limitBottom, PDRectangle rectangle, boolean underline) {
        super(context, font, fontSize, lineDistance, x, y, newPosition, limitLeft, limitRight, limitTop, limitBottom, rectangle, underline);
        this.horizontal = false;
    }

    /**
     * 下一行
     */
    @Override
    protected void nextLine() {

    }

    /**
     * 添加文本
     *
     * @param ch    字符
     * @param width 宽度
     */
    @Override
    protected void put(char ch, float width) {

    }
}
