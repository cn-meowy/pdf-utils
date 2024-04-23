package cn.meowy.pdf.utils;

import cn.hutool.core.map.multi.Table;
import cn.meowy.pdf.struct.AlignmentHandler;
import cn.meowy.pdf.struct.AlignmentLeft;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


/**
 * PDF文本输出
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class PDFWriteUtils {


    public static void write(PDDocument doc, final int indexPage, PDRectangle rectangle, PDFont font, float fontSize, Color color, final AlignmentHandler handler) {
        int index = indexPage;
        for (Table<Float, Float, String> cells : handler.pages()) {
            try (PDPageContentStream contents = new PDPageContentStream(doc, getPage(doc, index++, rectangle), PDPageContentStream.AppendMode.APPEND, true, true)) {
                cells.forEach((x, y, s) -> writeChar(s, x, y, contents, font, fontSize, handler));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeSimple(PDDocument doc, PDPage page, String context, PDFont font, float fontSize, float x, float y) {
        try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
            contents.beginText();
            // 设置字体&字体大小
            contents.setFont(font, fontSize);
            writeChar(context, x, y, contents, font, fontSize, null);
            // 结束输出
            contents.endText();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输出字符
     *
     * @param ch       字符
     * @param x        x坐标
     * @param y        y坐标
     * @param contents pdf
     * @throws IOException io 异常
     */
    private static void writeChar(String ch, float x, float y, PDPageContentStream contents, PDFont font, float fontSize, final AlignmentHandler handler) {
        ExUtils.execute(() -> {
            // 输出文字
            contents.beginText();
            // 设置字体&字体大小
            contents.setFont(font, fontSize);
            // 设置文本输出坐标
            contents.newLineAtOffset(x, y);
            // 设置输出文件内容
            contents.showText(ch);
            // 结束输出
            contents.endText();
            if (Objects.nonNull(handler) && handler.isUnderline()) {
                if (handler.isHorizontal()) {
                    contents.moveTo(x, y - 2);
                    contents.lineTo(x + FontUtils.width(font, ch, fontSize), y - 2);
                } else {
                    contents.moveTo(x + 2, y);
                    contents.lineTo(x + 2, y - FontUtils.height(font, fontSize) - handler.getLineDistance());
                }
                contents.stroke();
            }
        }, "输出字符失败: {} !", ch);
    }

    /**
     * 划线
     *
     * @param doc       文档
     * @param indexPage 起始页
     * @param rectangle 页面大小
     * @param fromX     起始x
     * @param fromY     起始y
     * @param toX       结束x
     * @param toY       结束y
     */
    public static void drawLine(PDDocument doc, final int indexPage, PDRectangle rectangle, float fromX, float fromY, float toX, float toY, float lineWidth) {
        try (PDPageContentStream contents = new PDPageContentStream(doc, getPage(doc, indexPage, rectangle), PDPageContentStream.AppendMode.APPEND, true, true)) {
//            contents.setLineWidth(lineWidth);
            contents.moveTo(fromX, fromY);
            contents.lineTo(toX, toY);
            contents.stroke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定页
     *
     * @param doc       PDF文档
     * @param index     索引页
     * @param rectangle 页面大小
     * @return PDF页
     */
    public static PDPage getPage(PDDocument doc, int index, PDRectangle rectangle) {
        int total = doc.getNumberOfPages();
        if ((total - 1) >= index) {
            return doc.getPage(index);
        } else {
            for (int i = total; i <= index; i++) {
                doc.addPage(new PDPage(rectangle));
            }
            return doc.getPage(index);
        }
    }

}
