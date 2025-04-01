package cn.meowy.pdf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 字体工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/18
 **/
public class FontUtils {

    public final static String TTF_SUFFIX = ".ttf";

    private final static Map<String, String> FONT_STORE = new HashMap<>();

    private final static Map<String, Metrics> FONT_METRICS = new HashMap<>();

    /**
     * 初始化
     *
     * @param dirPath 字体路径
     */
    public static void init(String dirPath) {
        if (StrUtil.isNotBlank(dirPath)) {
            File file = new File(dirPath);
            if (file.exists() && file.isDirectory()) {
                // 遍历所有字体文件
                FileUtil.loopFiles(dirPath, new SuffixFileFilter(TTF_SUFFIX)).forEach(f -> FONT_STORE.put(StrUtil.removeSuffix(f.getName(), TTF_SUFFIX), f.getAbsolutePath()));
            }
        }
    }

    /**
     * 添加字体
     *
     * @param path 字体路径
     */
    public static void add(String path) {
        Assert.state(StrUtil.isNotBlank(path), "文件路径[{}]不存在!", path);
        File file = new File(path);
        Assert.state(file.exists() && file.isFile() && StrUtil.endWith(file.getName(), TTF_SUFFIX), "请检查字体文件是否存在且文件类型为[{}]", TTF_SUFFIX);
        FONT_STORE.put(StrUtil.removeSuffix(file.getName(), TTF_SUFFIX), path);
    }

    /**
     * 加载字体
     *
     * @param document pdf 文档
     * @param path     字体路径
     * @return 字体
     */
    public static PDFont loadFont(PDDocument document, String path) {
        return ExUtils.execute(() -> PDType0Font.load(document, Files.newInputStream(new File(path).toPath())), "获取字体文件路径失败!");
    }

    /**
     * 加载字体
     *
     * @param document pdf 文档
     * @param fontName 字体名称
     * @return 字体
     */
    public static PDFont loadByName(PDDocument document, String fontName) {
        String filePath = FONT_STORE.get(fontName);
        Assert.state(Objects.nonNull(filePath), "[{}]字体不存在!", fontName);
        return loadFont(document, filePath);
    }

    /**
     * 获取字符宽度
     *
     * @param font      字体
     * @param fontSize  字体大小
     * @return 宽度
     */
    public static float width(PDFont font, float fontSize) {
        return width(font, "啊", fontSize);
    }

    /**
     * 获取字符宽度
     *
     * @param font      字体
     * @param character 字符
     * @param fontSize  字体大小
     * @return 宽度
     */
    public static float width(PDFont font, Character character, float fontSize) {
        return width(font, String.valueOf(character), fontSize);
    }

    /**
     * 获取字符宽度
     *
     * @param font      字体
     * @param str 字符
     * @param fontSize  字体大小
     * @return 宽度
     */
    public static float width(PDFont font, String str, float fontSize) {
        return ExUtils.execute(() -> font.getStringWidth(str) / 1000 * fontSize);
    }

    /**
     * 获取字符宽度
     *
     * @param font      字体
     * @param str 字符
     * @param fontSize  字体大小
     * @return 宽度
     */
    public static float width(PDFont font, String str, float fontSize, float lineSpace) {
        return ExUtils.execute(() -> font.getStringWidth(str) / 1000 * fontSize + (str.length() * lineSpace));
    }

    /**
     * 获取字体高度
     *
     * @param font     字体
     * @param fontSize 大小
     * @return 高度
     */
    public static float height(PDFont font, float fontSize) {
        if (!FONT_METRICS.containsKey(font.getName())) {
            cacheMetrics(font);
        }
        return FONT_METRICS.get(font.getName()).height * fontSize;
    }

    /**
     * 构建字体
     *
     * @param font 字体
     */
    private static void cacheMetrics(PDFont font) {
        float base = font.getFontDescriptor().getXHeight() / 1000;
        float ascent = font.getFontDescriptor().getAscent() / 1000 - base;
        float descent = font.getFontDescriptor().getDescent() / 1000;
        FONT_METRICS.put(font.getName(), new Metrics(base + ascent - descent, ascent, descent));
    }


    @Data
    @AllArgsConstructor
    public static class Metrics {
        private final float height;
        private final float ascent;
        private final float descent;
    }
}
