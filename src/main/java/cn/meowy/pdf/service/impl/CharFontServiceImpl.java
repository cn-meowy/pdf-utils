package cn.meowy.pdf.service.impl;

import cn.hutool.core.lang.Validator;
import cn.meowy.pdf.service.FontService;
import cn.meowy.pdf.utils.ExUtils;
import cn.meowy.pdf.utils.FontUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.util.HashMap;
import java.util.Objects;

/**
 * 字符
 *
 * @author: Mr.Zou
 * @date: 2025-04-01
 **/
public class CharFontServiceImpl extends FontService {

    private final String chineseFontPath;

    private final String numberFontPath;

    private final String englishFontPath;

    public CharFontServiceImpl(String chineseFontPath, String numberFontPath, String englishFontPath) {
        this.chineseFontPath = chineseFontPath;
        this.numberFontPath = numberFontPath;
        this.englishFontPath = englishFontPath;
        FONT_CACHE.set(new HashMap<>());
    }

    /**
     * 根据字符类型获取字体
     *
     * @param ch          字符
     * @param doc         文档
     * @param defaultFont 默认字体
     * @return 字体
     */
    @Override
    public PDFont loadFont(char ch, PDDocument doc, PDFont defaultFont) {
        String value = String.valueOf(ch);
        if (Validator.isChinese(value)) {                           // 中文
            return getAndCache(doc, chineseFontPath);
        } else if (Validator.isNumber(value)) {                     // 数字
            return getAndCache(doc, numberFontPath);
        } else if (Validator.isWord(value)) {                       // 英文字符
            return getAndCache(doc, englishFontPath);
        } else {
            return defaultFont;
        }
    }

    private PDFont getAndCache(PDDocument doc, String fontPath) {
        PDFont font = FONT_CACHE.get().get(fontPath);
        if (Objects.isNull(font)) {
            font = ExUtils.execute(() -> FontUtils.loadFont(doc, fontPath), "[{}]加载字体失败!", fontPath);
            FONT_CACHE.get().put(fontPath, font);
        }
        return font;
    }
}
