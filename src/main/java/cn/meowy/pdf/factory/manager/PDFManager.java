package cn.meowy.pdf.factory.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.multi.Table;
import cn.hutool.core.math.Calculator;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.struct.*;
import cn.meowy.pdf.utils.*;
import cn.meowy.pdf.utils.annotation.StyleProperty;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.dom4j.Document;
import org.dom4j.Element;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * PDF处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public abstract class PDFManager {

    /**
     * 页面配置
     */
    protected final static String GLOBAL_PAGE_SETTING = "GLOBAL_PAGE_SETTING";

    /**
     * 页面配置
     */
    protected final static String PAGE_SETTING = "PAGE_SETTING";

    /**
     * X坐标
     */
    protected final static String X_COORDINATE = "X";

    /**
     * Y坐标
     */
    protected final static String Y_COORDINATE = "Y";

    /**
     * 页面配置缓存
     */
    protected final static ThreadLocal<Map<PDPage, Object>> PAGE_SETTING_CACHE = new ThreadLocal<>();

    /**
     * 缓存
     */
    protected final static ThreadLocal<Map<String, Object>> CACHE = new ThreadLocal<>();

    /**
     * 字体缓存
     */
    protected final static ThreadLocal<Map<String, PDFont>> FONT_CACHE = new ThreadLocal<>();

    /**
     * 处理器
     */
    protected final static Map<String, PDFManager> MANAGER_MAP = new HashMap<>();

    /**
     * 需要清理的字段属性
     */
    protected final static List<Field> CLEAN_FIELDS;

    /**
     * 对齐方式
     */
    protected final static Map<String, Class<? extends AlignmentHandler>> HANDLER = new HashMap<>();

    protected static final String SET = "set";

    static {
        CLEAN_FIELDS = Arrays.stream(PDFManager.class.getDeclaredFields()).filter(f -> Modifier.isStatic(f.getModifiers()) && f.getType().isAssignableFrom(ThreadLocal.class)).collect(Collectors.toList());
        HANDLER.put(Alignment.LEFT.name(), AlignmentLeft.class);
        HANDLER.put(Alignment.CENTER.name(), AlignmentCenter.class);
        HANDLER.put(Alignment.RIGHT.name(), AlignmentRight.class);
        HANDLER.put(Alignment.TOP.name(), AlignmentTop.class);
        HANDLER.put(Alignment.MIDDLE.name(), AlignmentMiddle.class);
        HANDLER.put(Alignment.BOTTOM.name(), AlignmentBottom.class);
    }

    public static PDFManager getPDFManager() {
        return MANAGER_MAP.get("page");
    }

    /**
     * 添加处理器
     *
     * @param manager   处理器
     * @param nodeNames 处理器名称
     */
    public static void addHandler(PDFManager manager, String... nodeNames) {
        if (Objects.nonNull(nodeNames)) {
            for (String nodeName : nodeNames) {
                MANAGER_MAP.put(nodeName, manager);
            }
        }
    }

    public static <T> void action(PDDocument doc, String templateName, T data) {
        Document template = TemplateUtils.loadTemplate(templateName, data);  // 读取模版
        Assert.state(XmlUtils.rootElementCheck(template, XmlElement.PAGE), "模板根节点错误!\n{}", templateName);
        Element element = template.getRootElement();                         // 获取根节点，后续根据子节点自动应用不同的处理器
        MANAGER_MAP.get(element.getName()).handler(doc, element, data);      // 根节点处理器
    }

    /**
     * 根据节点名称获取处理器
     *
     * @param nodeName 处理器名称
     * @return 处理器
     */
    public PDFManager get(String nodeName) {
        return MANAGER_MAP.get(nodeName);
    }

    /**
     * 缓存值
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     */
    protected <T> void setCache(String key, T value) {
        Map<String, Object> map = getCache();
        map.put(key, value);
    }

    /**
     * 获取缓存
     *
     * @return 缓存池
     */
    private Map<String, Object> getCache() {
        Map<String, Object> map = CACHE.get();
        if (Objects.isNull(map)) {
            synchronized (PDFManager.class) {
                map = CACHE.get();
                if (Objects.isNull(map)) {
                    map = new ConcurrentHashMap<>();
                    CACHE.set(map);
                }
            }
        }
        return map;
    }

    /**
     * 获取缓存值
     *
     * @param key key
     * @param <T> 缓存值类型
     * @return 缓存值
     */
    protected <T> T getCache(String key) {
        return (T) getCache().get(key);
    }


    /**
     * 节点处理器
     *
     * @param doc     文档
     * @param element 元素节点
     * @param data    数据
     */
    public <T> void handler(PDDocument doc, Element element, T data) {
        get(element.getName()).handler(doc, element, data);
    }

    /**
     * 匹配子节点处理器处理
     *
     * @param doc     文档
     * @param element 节点
     * @param data    数据
     */
    protected <T> void childHandlers(PDDocument doc, Element element, T data) {
        element.elements().forEach(e -> get(e.getName()).handler(doc, e, data));
    }

    /**
     * 获取flot 属性值
     *
     * @param element      元素
     * @param attributeKey 属性值
     * @return float
     */
    protected <T> Float getFloatAttribute(Element element, String attributeKey) {
        return getFloatAttribute(element, attributeKey, getParams());
    }

    /**
     * 获取flot 属性值
     *
     * @param element      元素
     * @param attributeKey 属性值
     * @param params       属性列表
     * @return float
     */
    protected <T> Float getFloatAttribute(Element element, String attributeKey, Map<String, Object> params) {
        String number = XmlUtils.getStr(element, attributeKey, params);
        if (StrUtil.isNotBlank(number)) {
            return ExUtils.execute(() -> (float) Calculator.conversion(number), "属性配置错误!错误属性为[{}]: {}", attributeKey, number);
        }
        return null;
    }

    /**
     * 设置当前x坐标
     */
    protected float currentX() {
        return getCache(X_COORDINATE);
    }

    /**
     * 设置x坐标
     *
     * @param x x坐标
     */
    protected void setX(float x) {
        setCache(X_COORDINATE, x);
    }

    /**
     * 设置xy坐标
     *
     * @param x x坐标
     * @param y y坐标
     */
    protected void setLocation(float x, float y) {
        setCache(X_COORDINATE, x);
        setCache(Y_COORDINATE, y);
    }

    /**
     * 设置y坐标
     */
    protected float currentY() {
        return getCache(Y_COORDINATE);
    }

    /**
     * 设置y坐标
     *
     * @param y y坐标
     */
    protected void setY(float y) {
        setCache(Y_COORDINATE, y);
    }

    /**
     * 获取页面配置
     */
    protected PageStruct setting() {
        return getCache(PAGE_SETTING);
    }

    /**
     * 获取全局页面配置
     */
    protected PageStruct globalSetting() {
        return getCache(GLOBAL_PAGE_SETTING);
    }

    /**
     * 获取最后一页
     *
     * @param doc 文档
     * @return 页面
     */
    public PDPage getLastPage(PDDocument doc) {
        int pages = doc.getNumberOfPages();
        if (pages == 0) {
            return newPage(doc);
        }
        return doc.getPage(pages - 1);
    }

    /**
     * 获取指定页面
     *
     * @param doc     文档
     * @param pageNum 页面
     * @return 页面
     */
    public PDPage getPage(PDDocument doc, final int pageNum) {
        int number = doc.getNumberOfPages();
        if (pageNum < 0) {
            return getLastPage(doc);
        } else if (number == 0 && pageNum == 0) {
            return newPage(doc);
        } else if ((number - 1) < pageNum) {
            PDPage last = null;
            for (int i = (number - 1); i <= pageNum; i++) {
                last = newPage(doc);
            }
            return last;
        } else {
            return doc.getPage(pageNum);
        }
    }

    /**
     * 获取指定页面
     *
     * @param doc 文档
     * @return 页面
     */
    public int getLastPageNum(PDDocument doc) {
        int pages = doc.getNumberOfPages();
        if (pages == 0) {
            return 0;
        }
        return pages - 1;
    }

    /**
     * 创建新页
     *
     * @param doc 文档
     * @return 新页
     */
    public PDPage newPage(PDDocument doc) {
        PDPage page = newPage();
        doc.addPage(page);
        return page;
    }

    /**
     * 获取参数
     *
     * @return 参数
     */
    protected Map<String, Object> getParams() {
        return MapUtil.builder(setting().map).put("x", getCache(X_COORDINATE)).put("y", getCache(Y_COORDINATE)).build();
    }

    /**
     * 获取指定页面的配置参数
     *
     * @return 参数
     */
    protected Map<String, Object> getParams(PDDocument doc, int index) {
        return MapUtil.builder(getPageSetting(doc, index).map).put("x", getCache(X_COORDINATE)).put("y", getCache(Y_COORDINATE)).build();
    }

    /**
     * 创建新页
     *
     * @return 新页
     */
    public PDPage newPage() {
        PDPage page = new PDPage(setting().rectangle);
        cachePageSetting(page);
        return page;
    }

    /**
     * 缓存pdf页面配置信息
     *
     * @param page page
     */
    protected void cachePageSetting(PDPage page) {
        Map<PDPage, Object> map = PAGE_SETTING_CACHE.get();
        if (Objects.isNull(map)) {
            synchronized (PDFManager.class) {
                if (Objects.isNull(PAGE_SETTING_CACHE.get())) {
                    map = new HashMap<>();
                    PAGE_SETTING_CACHE.set(map);
                }
            }
        }
        map.put(page, setting());
    }

    /**
     * 获取指定页面的配置信息
     *
     * @param doc   文档
     * @param index 页面索引
     * @return 配置信息
     */
    protected PageStruct getPageSetting(PDDocument doc, int index) {
        Map<PDPage, Object> cache = PAGE_SETTING_CACHE.get();
        if (Objects.nonNull(cache)) {
            List<PDPage> keys = new ArrayList<>(cache.keySet());
            for (PDPage key : keys) {
                if (doc.getPages().indexOf(key) == index) {
                    return (PageStruct) cache.get(key);
                }
            }
        }
        return globalSetting();
    }

    /**
     * 设置坐标
     *
     * @param element 节点
     * @param struct  元素
     */
    public void setCoordinate(Element element, PageStruct struct) {
        Float x = getFloatAttribute(element, XmlAttribute.X);                           // 从标签中读取x坐标值
        Float y = getFloatAttribute(element, XmlAttribute.Y);                           // 从标签中读取y坐标值
        if (Objects.nonNull(x)) {                                                       // 不为空则设置为指定x坐标为起始位置
            setCache(X_COORDINATE, x);
        } else if (Objects.equals(struct.textDirection, TextDirection.HORIZONTAL)) {    // 未指定且文本对齐方式为水平时计算x坐标起始位置
            if (Objects.equals(struct.alignment, Alignment.RIGHT)) {
                setCache(X_COORDINATE, struct.margin.left);
            } else if (Objects.equals(struct.alignment, Alignment.CENTER)) {
                setCache(X_COORDINATE, struct.centerX);
            } else {
                setCache(X_COORDINATE, struct.limitX);
            }
        } else {
            setCache(X_COORDINATE, struct.margin.left);
        }
        if (Objects.nonNull(y)) {                                                       // 不为空则设置为指定y坐标为起始位置
            setCache(Y_COORDINATE, y);
        } else if (Objects.equals(struct.textDirection, TextDirection.VERTICAL)) {      // 未指定且文本对齐方式为垂直时计算y坐标起始位置
            if (Objects.equals(struct.alignment, Alignment.BOTTOM)) {
                setCache(Y_COORDINATE, struct.margin.bottom);
            } else if (Objects.equals(struct.alignment, Alignment.MIDDLE)) {
                setCache(Y_COORDINATE, struct.centerY);
            } else {
                setCache(Y_COORDINATE, struct.limitY);
            }
        } else {
            setCache(Y_COORDINATE, struct.limitY);
        }
    }

    /**
     * 加载样式
     *
     * @param mapper  字段映射
     * @param style   样式
     * @param element xml元素
     * @param params  实时参数
     * @param <T>     类型
     * @return 样式
     */
    public <T> T loadStyle(Map<String, Field> mapper, T style, Element element, Map<String, Object> params) {
        mapper.forEach((k, v) -> {                                                                                            // 遍历属性并赋值
            Object value = AttributeUtils.parse(XmlUtils.getStr(element, getPropertyKey(v), params), v.getType(), null);       // 读取xml中的属性值
            if (Objects.nonNull(value)) {                                                                                                  // 属性存在则赋值,不存在则使用默认
                ExUtils.execute(() -> MethodUtils.invokeMethod(style, k, value), "");                                             // 设置属性值
            }
        });
        return style;
    }

    /**
     * 根据字段获取xml属性键
     *
     * @param field 字段
     * @return key
     */
    public String getPropertyKey(Field field) {
        StyleProperty property = field.getAnnotation(StyleProperty.class);
        if (Objects.nonNull(property)) {
            return property.value();
        }
        return StrUtil.toSymbolCase(field.getName(), CharPool.DASHED);
    }

    /**
     * 加载字体
     *
     * @param doc      文档
     * @param fontName 字体名称
     * @return 字体
     */
    public PDFont loadFont(PDDocument doc, String fontName) {
        if (StringUtils.isNotBlank(fontName)) {
            Map<String, PDFont> map = FONT_CACHE.get();
            PDFont font;
            if (Objects.isNull(map)) {
                FONT_CACHE.set(MapUtil.builder(new HashMap<String, PDFont>()).build());
            }
            font = map.get(fontName);
            if (Objects.isNull(font)) {
                font = FontUtils.loadFont(doc, fontName);
                map.put(fontName, font);
            }
            return font;
        } else {
            return setting().font;
        }

    }

    /**
     * 清理缓存
     */
    public void clean() {
        CLEAN_FIELDS.forEach(f -> ((ThreadLocal<?>) ExUtils.execute(() -> f.get(this))).remove());
    }

    /**
     * PDF文本输出
     *
     * @author: Mr.Zou
     * @date: 2024/4/19
     **/
    protected static class PDFWriteUtils {

        /**
         * 输出文本
         *
         * @param doc       文档
         * @param indexPage 起始页
         * @param font      字体
         * @param fontSize  字体大小
         * @param color     颜色
         * @param handler   文字方向处理器
         */
        public static void write(PDDocument doc, final int indexPage, PDFont font, float fontSize, Color color, final AlignmentHandler handler) {
            int index = indexPage;
            for (Table<Float, Float, String> cells : handler.pages()) {
                try (PDPageContentStream contents = new PDPageContentStream(doc, getPage(doc, index++), PDPageContentStream.AppendMode.APPEND, true, true)) {
                    cells.forEach((x, y, s) -> writeChar(s, x, y, contents, color, font, fontSize, handler.isUnderline(), handler.isHorizontal(), handler.getLineDistance()));
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * 简单文本输出
         *
         * @param doc      文档
         * @param page     输出页
         * @param context  内容
         * @param color    颜色
         * @param font     字体
         * @param fontSize 字体大小
         * @param x        起始x坐标
         * @param y        起始y坐标
         */
        public static void writeSimple(PDDocument doc, PDPage page, String context, Color color, PDFont font, float fontSize, float x, float y) {
            try (PDPageContentStream contents = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                writeChar(StrUtil.nullToEmpty(context), x, y, contents, color, font, fontSize, false, false, 0);    // 输出字符
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 简单文本输出
         *
         * @param doc       文档
         * @param pageIndex 输出页
         * @param ch        内容
         * @param color     颜色
         * @param font      字体
         * @param fontSize  字体大小
         * @param x         起始x坐标
         * @param y         起始y坐标
         */
        public static void writeSimple(PDDocument doc, int pageIndex, char ch, Color color, PDFont font, float fontSize, float x, float y, boolean underline, boolean horizontal, float lineDistance) {
            try (PDPageContentStream contents = new PDPageContentStream(doc, getPage(doc, pageIndex), PDPageContentStream.AppendMode.APPEND, true, true)) {
                writeChar(String.valueOf(ch), x, y, contents, color, font, fontSize, underline, horizontal, lineDistance);    // 输出字符
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
         */
        public static void writeChar(String ch, float x, float y, PDPageContentStream contents, Color color, PDFont font, float fontSize, boolean underline, boolean horizontal, float lineDistance) {
            ExUtils.execute(() -> {
                contents.beginText();                                                                                       // 输出文字
                contents.setFont(font, fontSize);                                                                           // 设置字体&字体大小
                contents.newLineAtOffset(x, y);                                                                             // 设置文本输出坐标
                contents.setNonStrokingColor(color);                                                                        // 设置颜色
                contents.showText(ch);                                                                                      // 设置输出文件内容
                contents.endText();                                                                                         // 结束输出
                if (underline) {                                                                                            // 下划线设置
                    if (horizontal) {                                                                                       // 水平对齐
                        contents.moveTo(x, y - 2);                                                                       // 下划线起始位置
                        contents.lineTo(x + FontUtils.width(font, ch, fontSize), y - 2);                              // 下划线结束位置
                    } else {                                                                                                // 垂直对齐
                        contents.moveTo(x + 2, y);                                                                       // 下划线起始位置
                        contents.lineTo(x + 2, y - FontUtils.height(font, fontSize) - lineDistance);                  // 下划线结束位置
                    }
                    contents.stroke();                                                                                      // 输出
                }
            }, "输出字符失败: {} !", ch);
        }

        /**
         * 划线
         *
         * @param doc        文档
         * @param color      线条颜色
         * @param indexPage  起始页
         * @param fromX      起始x
         * @param fromY      起始y
         * @param toX        结束x
         * @param toY        结束y
         * @param lineHeight 线条高度
         */
        public static void drawLine(PDDocument doc, Color color, final int indexPage, float fromX, float fromY, float toX, float toY, float lineHeight) {
            try (PDPageContentStream contents = new PDPageContentStream(doc, getPage(doc, indexPage), PDPageContentStream.AppendMode.APPEND, true, true)) {
                contents.setLeading(lineHeight);
                contents.setStrokingColor(color);
                contents.moveTo(fromX, fromY);
                contents.lineTo(toX, toY);
                contents.stroke();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 划线
         *
         * @param doc       文档
         * @param color     线条颜色
         * @param indexPage 起始页
         * @param fromX     起始x
         * @param fromY     起始y
         * @param toX       结束x
         * @param toY       结束y
         */
        public static void drawBackgroundColor(PDDocument doc, Color color, final int indexPage, float fromX, float fromY, float toX, float toY) {
            try (PDPageContentStream contents = new PDPageContentStream(doc, getPage(doc, indexPage), PDPageContentStream.AppendMode.APPEND, true, true)) {
                // 设置背景色（浅蓝色）
                contents.setNonStrokingColor(color);
                contents.addRect(fromX, fromY, toX - fromX, toY - fromY);
                contents.fill();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 获取指定页
         *
         * @param doc   PDF文档
         * @param index 索引页
         * @return PDF页
         */
        public static PDPage getPage(PDDocument doc, int index) {
            int total = doc.getNumberOfPages();
            if ((total - 1) >= index) {
                return doc.getPage(index);
            } else {
                for (int i = total; i <= index; i++) {
                    doc.addPage(getPDFManager().newPage());
                }
                return doc.getPage(index);
            }

        }
    }
}
