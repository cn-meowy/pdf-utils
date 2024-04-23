package cn.meowy.pdf.factory.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.math.Calculator;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.struct.*;
import cn.meowy.pdf.utils.*;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
     * 缓存
     */
    protected final static ThreadLocal<Map<String, Object>> CACHE = new ThreadLocal<>();

    /**
     * 处理器
     */
    protected final static Map<String, PDFManager> MANAGER_MAP = new HashMap<>();

    protected final static Map<String, Class<? extends AlignmentHandler>> HANDLER = new HashMap<>();

    static  {
        HANDLER.put(Alignment.LEFT.name(), AlignmentLeft.class);
        HANDLER.put(Alignment.CENTER.name(), AlignmentCenter.class);
        HANDLER.put(Alignment.RIGHT.name(), AlignmentRight.class);
        HANDLER.put(Alignment.TOP.name(), AlignmentTop.class);
        HANDLER.put(Alignment.MIDDLE.name(), AlignmentMiddle.class);
        HANDLER.put(Alignment.BOTTOM.name(), AlignmentBottom.class);
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
        Document template = TemplateUtils.loadTemplate(templateName, data);
        Assert.state(XmlUtils.rootElementCheck(template, XmlElement.PAGE), "模板根节点错误!\n{}", templateName);
        Element element = template.getRootElement();
        MANAGER_MAP.get(element.getName()).handler(doc, element, data);
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
     * @param doc      文档
     * @param element  节点
     * @param data     数据
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
        String number = XmlUtils.getStr(element, attributeKey, getParams());
        if (StrUtil.isNotBlank(number)) {
            return ExUtils.execute(() -> (float) Calculator.conversion(number), "属性配置错误!错误属性为[{}]: {}", attributeKey, number);
        }
        return null;
    }

    /**
     * 获取x坐标
     *
     * @param element 元素
     * @return x坐标
     */
    protected float getX(Element element) {
        Float x = getFloatAttribute(element, XmlAttribute.X);
        if (Objects.isNull(x)) {
            x = getCache(X_COORDINATE);
        } else {
            if (x < 0) {
                x += setting().rectangle.getWidth();
                this.setX(x);
            }
        }
        return x;
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
     * 获取y坐标
     *
     * @param element 元素
     * @return y坐标
     */
    protected float getY(Element element) {
        Float y = getFloatAttribute(element, XmlAttribute.Y);
        if (Objects.isNull(y)) {
            y = getCache(Y_COORDINATE);
        } else {
            if (y < 0) {
                y += setting().rectangle.getHeight();
                this.setY(y);
            }
        }
        return y;
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
     * 创建新页
     *
     * @return 新页
     */
    public PDPage newPage() {
        return new PDPage(setting().rectangle);
    }
}
