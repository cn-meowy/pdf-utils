package cn.meowy.pdf.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.Map;

/**
 * 字体服务接口
 *
 * @author: Mr.Zou
 * @date: 2025-04-01
 **/
public abstract class FontService {
    /**
     * 字体缓存
     */
    protected final static ThreadLocal<Map<String, PDFont>> FONT_CACHE = new ThreadLocal<>();

    /**
     * 根据字符类型获取字体
     *
     * @param ch          字符
     * @param doc         文档
     * @param defaultFont 默认字体
     * @return 字体
     */
    public PDFont loadFont(char ch, PDDocument doc, PDFont defaultFont) {
        return defaultFont;
    }

    public void clean() {
        FONT_CACHE.remove();
    }

}
