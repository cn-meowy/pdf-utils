package cn.meowy.pdf.factory.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.*;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.BorderStyle;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.settings.VerticalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 表格处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class TableManager extends PDFManager {

    /**
     * 配置属性集合
     */
    private final static Map<String, Method> TABLE_SETTING_ATTRIBUTE = new HashMap<>();


    static {
        for (Method method : ClassUtil.getPublicMethods(Settings.class)) {
            if (StrUtil.startWith(method.getName(), "set")) {
                TABLE_SETTING_ATTRIBUTE.put(StrUtil.toSymbolCase(StrUtil.lowerFirst(StrUtil.removePrefix(method.getName(), "set")), CharUtil.DASHED), method);
            }
        }
    }


    /**
     * 节点处理器
     *
     * @param doc     文档
     * @param element 元素节点
     * @param data    数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        // 输出页面
        drawTable(doc, element, getLastPage(doc));
    }

    /**
     * 画出表格
     *
     * @param doc     文本
     * @param element 元素
     * @param page    页面
     */
    private void drawTable(PDDocument doc, Element element, PDPage page) {
        try (final PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
            float x = getX(element);
            float y = getY(element);
            PageStruct struct = setting();
            if (y < struct.margin.bottom) {
                // 创建新页
                newPage(doc);
                y = struct.limitY;
            }
            Settings defaultSettings = defaultSettings();
            float totalWidth = struct.limitX - x;
            Float newPosition = AttributeUtils.parse(XmlUtils.getStr(element, XmlAttribute.NEW_POSITION, getParams()), Float.class, 40f);
            Boolean block = Boolean.parseBoolean(XmlUtils.getStr(element, XmlAttribute.BLOCK, getParams()));
            Table.TableBuilder tableBuilder = Table.builder().settings(getSettings(doc, element, defaultSettings));
            int columNum = 0;
            for (Element tr : element.elements()) {
                if (StrUtil.equals(tr.getName(), XmlElement.TR)) {
                    Row.RowBuilder rowBuilder = rowBuilder(tr);
                    // 若存在行配置,则设置
                    columNum = 0;
                    for (Element td : tr.elements()) {
                        if (StrUtil.equals(td.getName(), XmlElement.TD)) {
                            TextCell cell = createTextCell(doc, td);
                            columNum += cell.getColSpan();
                            rowBuilder.add(cell);
                        }
                    }
                    Row row = rowBuilder.build();
                    replaceSettings(doc, tr, row.getSettings());
                    tableBuilder.addRow(row);
                }
            }
            // 配置列宽
            setWidth(totalWidth, columNum, tableBuilder, element);
            Table table = tableBuilder.build();
            // 若为一个块,判断当前页是否能够放下
            if (Boolean.TRUE.equals(block)) {
                if ((y - struct.margin.bottom) < table.getHeight()) {
                    // 放到下一页
                    page = newPage(doc);
                    // 重置y
                    y = struct.limitY;
                }
            }
            TableDrawer drawer = TableDrawer.builder()
                    .page(page)
                    .contentStream(contentStream)
                    .table(table)
                    .startX(x)
                    .startY(y)
                    .endY(struct.margin.bottom)
                    .build();

            drawer.draw(() -> doc, this::newPage, newPosition);
            setY(drawer.getFinalY());
        } catch (Throwable e) {
            throw new RuntimeException("构建表格失败!", e);
        }
    }

    /**
     * 表格默认配置
     *
     * @return 默认配置
     */
    private Settings defaultSettings() {
        PageStruct struct = setting();
        float borderWidth = 1f;
        float padding = 4f;
        return Settings.builder()
                .textColor(Color.BLACK)
                .borderWidthLeft(borderWidth)
                .borderWidthRight(borderWidth)
                .borderWidthTop(borderWidth)
                .borderWidthBottom(borderWidth)
                .borderColorLeft(Color.BLACK)
                .borderColorRight(Color.BLACK)
                .borderColorTop(Color.BLACK)
                .borderColorBottom(Color.BLACK)
                .borderStyleLeft(BorderStyle.SOLID)
                .borderStyleRight(BorderStyle.SOLID)
                .borderStyleTop(BorderStyle.SOLID)
                .borderStyleBottom(BorderStyle.SOLID)
                .font(struct.font)
                .fontSize((int) struct.fontSize)
                .textColor(struct.pdColor)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.MIDDLE)
                .paddingLeft(padding)
                .paddingRight(padding)
                .paddingTop(padding)
                .paddingBottom(padding)
                .wordBreak(true)
                .build();
    }

    /**
     * 复制配置
     *
     * @param source 源
     * @param target 目标
     */
    private Settings copySettings(Settings source, Settings target) {
        set(target::setPaddingLeft, source.getPaddingLeft());
        set(target::setPaddingRight, source.getPaddingRight());
        set(target::setPaddingTop, source.getPaddingTop());
        set(target::setPaddingBottom, source.getPaddingBottom());
        set(target::setFont, source.getFont());
        set(target::setFontSize, source.getFontSize());
        set(target::setHorizontalAlignment, source.getHorizontalAlignment());
        set(target::setTextColor, source.getTextColor());
        set(target::setVerticalAlignment, source.getVerticalAlignment());
        set(target::setWordBreak, source.isWordBreak());
        set(target::setBackgroundColor, source.getBackgroundColor());
        set(target::setBorderColorBottom, source.getBorderColorBottom());
        set(target::setBorderColorLeft, source.getBorderColorLeft());
        set(target::setBorderColorRight, source.getBorderColorRight());
        set(target::setBorderColorTop, source.getBorderColorTop());
        set(target::setBorderStyleBottom, source.getBorderStyleBottom());
        set(target::setBorderStyleLeft, source.getBorderStyleBottom());
        set(target::setBorderStyleRight, source.getBorderStyleRight());
        set(target::setBorderStyleTop, source.getBorderStyleTop());
        set(target::setBorderWidthTop, source.getBorderWidthTop());
        set(target::setBorderWidthLeft, source.getBorderWidthLeft());
        set(target::setBorderWidthRight, source.getBorderWidthRight());
        set(target::setBorderWidthBottom, source.getBorderWidthBottom());
        return target;
    }

    /**
     * 获取表格设置
     *
     * @param doc             文档
     * @param element         元素
     * @param defaultSettings 默认配置
     * @return
     */
    private Settings getSettings(PDDocument doc, Element element, Settings defaultSettings) {
        // 创建默认设置
        Settings settings = Settings.builder().build();
        if (Objects.nonNull(defaultSettings)) {
            settings = copySettings(defaultSettings, settings);
        }
        replaceSettings(doc, element, settings);
        return settings;
    }

    /**
     * 替换配置
     *
     * @param doc      文档
     * @param element  元素
     * @param settings 设置
     */
    private void replaceSettings(PDDocument doc, Element element, Settings settings) {
        // 根据配置覆盖
        for (Attribute attribute : element.attributes()) {
            Method method = TABLE_SETTING_ATTRIBUTE.get(attribute.getName());
            String value = attribute.getValue();
            if (Objects.nonNull(method) && Objects.nonNull(value)) {
                if (StrUtil.equalsIgnoreCase(attribute.getName(), "font")) {
                    // 字体配置
                    set(settings::setFont, FontUtils.loadByName(doc, value));
                } else {

                }
                Object parse = AttributeUtils.parse(value, method.getParameters()[0].getType(), null);
                ExUtils.execute(() -> method.invoke(settings, parse), "执行[{}]失败! param = {}", method.getName(), parse);
            }
        }
        // 内边距设置
        String paddingStr = XmlUtils.getStr(element, XmlAttribute.PADDING, getParams());
        if (StrUtil.isNotBlank(paddingStr)) {
            Float padding = Float.parseFloat(paddingStr);
            set(settings::setPaddingLeft, padding);
            set(settings::setPaddingRight, padding);
            set(settings::setPaddingTop, padding);
            set(settings::setPaddingBottom, padding);
        }
    }

    /**
     * set值
     *
     * @param consumer consumer
     * @param param    param
     * @param <T>      类型
     */
    private <T> void set(Consumer<T> consumer, T param) {
        if (ObjectUtil.isNotEmpty(param)) {
            consumer.accept(param);
        }
    }


    /**
     * 设置表格宽度配置
     *
     * @param total 总宽度
     * @param num   列数
     * @param table 表格
     * @return
     */
    private void setWidth(float total, int num, Table.TableBuilder table, Element element) {
        String columWidth = XmlUtils.getStr(element, XmlAttribute.COLUMN_WIDTH, getParams());
        if (StrUtil.isNotBlank(columWidth)) {
            ExUtils.execute(() -> Stream.of(columWidth.split(StrUtil.SPACE)).forEach(s -> table.addColumnsOfWidth(Float.parseFloat(s))), "表格列宽度配置错误!");
        } else {
            // 未配置
            float step = total / num;
            for (int i = 0; i < num; i++) {
                table.addColumnOfWidth(step);
            }
        }
    }

    /**
     * 创建文本单元格
     *
     * @param doc     文档
     * @param element 元素
     * @return 单元格
     */
    private TextCell createTextCell(PDDocument doc, Element element) {
        TextCell cell = TextCell.builder()
                .text(element.getStringValue())
                // 配置行
                .rowSpan(Integer.parseInt(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.ROW_SPAN, getParams()), "1")))
                // 配置列
                .colSpan(Integer.parseInt(ObjectUtil.defaultIfNull(XmlUtils.getStr(element, XmlAttribute.COLUM_SPAN, getParams()), "1")))
                .build();
        // 覆盖默认配置
        replaceSettings(doc, element, cell.getSettings());
        return cell;
    }

    /**
     * 构建行
     *
     * @param element 元素
     * @return 行
     */
    private Row.RowBuilder rowBuilder(Element element) {
        Row.RowBuilder builder = Row.builder();
        Float height = getFloatAttribute(element, XmlAttribute.HEIGHT);
        if (Objects.nonNull(height)) {
            builder.height(Float.max(height, 0f));
        }
        return builder;
    }

}
