package cn.meowy.pdf.factory.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.mutable.MutablePair;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.FontUtils;
import cn.meowy.pdf.utils.annotation.StyleProperty;
import cn.meowy.pdf.utils.enums.Alignment;
import cn.meowy.pdf.utils.enums.BorderStyle;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.dom4j.Element;

import java.awt.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 表格处理器
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
@Slf4j
public class TableManager extends PDFManager {

    protected static final Map<String, Map<String, Field>> STYLE_KEY = new HashMap<>();         // 样式键

    protected static final String TR = "tr";                                                    // tr

    protected static final String TD = "td";                                                    // td

    protected static final String SPACE = String.valueOf(CharPool.SPACE);                          // SPACE

    static {
        STYLE_KEY.put(Style.class.getSimpleName(), Arrays.stream(ReflectUtil.getFields(Style.class)).collect(Collectors.toMap(f -> SET + StrUtil.upperFirst(f.getName()), field -> field)));
        STYLE_KEY.put(TableRow.class.getSimpleName(), Arrays.stream(ReflectUtil.getFields(TableRow.class)).collect(Collectors.toMap(f -> SET + StrUtil.upperFirst(f.getName()), field -> field)));
        STYLE_KEY.put(TableCell.class.getSimpleName(), Arrays.stream(ReflectUtil.getFields(TableCell.class)).collect(Collectors.toMap(f -> SET + StrUtil.upperFirst(f.getName()), field -> field)));
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
        PageStruct setting = setting();                                                                                                                                         // 全局设置
        Map<String, Object> params = getParams();                                                                                                                               // 当前页面属性
        Style tableStyle = defaultTableStyle(element, params);                                                                                                                  // 表格样式设置
        List<TableRow> table = extractTableDataWithStyles(element, tableStyle, params);                                                                                         // 提取带样式的表格
        float x = tableStyle.x;                                                                                                                                                 // 起始x坐标
        float y = tableStyle.y;                                                                                                                                                 // 起始y坐标
        int startPageIndex = getLastPageNum(doc);
        if (CollUtil.isNotEmpty(table) && CollUtil.isNotEmpty(CollUtil.getFirst(table).getCells())) {
            extractCellPosition(doc, tableStyle, table);
            for (TableRow row : table) {
                int pageIndex = startPageIndex;
                float rowY = y;                                                                                                                                                 // 重置y坐标
                float rowX = x;                                                                                                                                                 // 重置x坐标
                TableCell minHeightCell = getMinHeightCell(row);
                if (minHeightCell.height > rowY - setting.margin.bottom && minHeightCell.height <= setting.limitY + setting.margin.bottom) {                                    // 若当前行高度大于当前页剩余高度且小于当前页总高度时,行起始位置下移至下一页
                    rowY = setting.limitY;
                    pageIndex++;
                }
                for (TableCell cell : row.cells) {
                    if (cell.rowspanFirst) {                                                                                                                                    // 输出
                        Pair<Integer, Float> endPageIndexAndEndY = drawBorder(doc, cell, pageIndex, rowY, rowX, setting.limitY, setting.margin.bottom);                         // 绘制单元格边框
                        if (minHeightCell == cell) {                                                                                                                            // 找到最小行高,记录行输入完成后的y坐标
                            startPageIndex = endPageIndexAndEndY.getKey();                                                                                                      // 设置下一行起始页索引
                            y = endPageIndexAndEndY.getValue();                                                                                                                 // 设置下一行起始y坐标
                        }
                    }
                    writeContext(doc, cell, pageIndex, rowX, rowY, setting.limitY, setting.margin.bottom);                                                                      // 写入文本
                    rowX = rowX + cell.width;                                                                                                                                   // 当前行x坐标右移
                }
            }
            setLocation(x, y);                                                                                                                                                  // 输出完毕,下移y坐标
        }
    }

    /**
     * 获取最小高度的单元格
     *
     * @param row 行
     * @return 单元格
     */
    private TableCell getMinHeightCell(TableRow row) {
        TableCell minHeightCell = CollUtil.getFirst(row.cells);
        float minHeight = minHeightCell.height;
        for (TableCell cell : row.cells) {
            if (cell.rowspanFirst && NumberUtil.compare(minHeight, cell.height) < 1) {
                minHeight = cell.height;
                minHeightCell = cell;
            }
        }
        return minHeightCell;
    }

    private void writeContext(PDDocument doc, TableCell cell, final int startPageIndex, float x, final float y, float top, float bottom) {
        if (StrUtil.isNotBlank(cell.text)) {
            PDFont font = loadFont(doc, cell.font);
            float pageHeight = top - bottom;
            if (!cell.direction.isVertical()) {                                                                                                                                                                                                                     // 水平方向
                float singleCharHeight = FontUtils.height(font, cell.fontSize) + cell.lineDistance;                                                                                                                                                                 // 单个字符所占高度
                Pair<Integer, Float> startPageAndStartY = startYAndStartPage(cell.verticalAlignment, startPageIndex, y, cell.paddingTop, cell.paddingBottom, cell.height, CollUtil.size(cell.lines), singleCharHeight, bottom, pageHeight);
                setStartXAndStartYAndIndexPage(cell, x, top, bottom, startPageAndStartY.getValue(), singleCharHeight, startPageIndex);
                Opt.ofNullable(CollUtil.getLast(cell.lines))
                        .ifPresent(line -> line.setX(startX(line.cell.horizontalAlignment, line.cell.width, line.line, font, line.cell.fontSize, line.cell.lineSpace, x, line.cell.paddingLeft, line.cell.paddingRight)));                                      // 设置最后一行的x坐标
                cell.lines.forEach(line -> PDFWriteUtils.writeSimple(doc, getPage(doc, line.indexPage), line.line, line.cell.textColor, font, line.cell.fontSize, line.x, line.y, cell.lineSpace));
            } else {                                                                                                                                                                                                                                                // 垂直方向                                                                                                                                            // 垂直方向
                float singleCharHeight = FontUtils.height(font, cell.fontSize) + cell.lineSpace;                                                                                                                                                                 // 单个字符所占高度
                float singleCharWidth = FontUtils.width(font, cell.fontSize) + cell.lineDistance;                                                                                                                                                                 // 单个字符所占高度
                float tempX = x;
                for (Line line : cell.lines) {
                    int pageIndex = startPageIndex;
                    float tempY = y;
                    if (!cell.verticalAlignment.isTop()) {                                                                                                                      // 下对齐,不足长度的行,行首增加空格补齐
                        float emptyY = cell.height - (line.line.length() * singleCharHeight);
                        emptyY = cell.verticalAlignment.isMiddle() ? emptyY / 2 : emptyY;
                        tempY -= emptyY;
                        if (tempY < bottom) {                                                                                                                                   // 需要换页
                            BigDecimal[] decimals = new BigDecimal(bottom - tempY).divideAndRemainder(new BigDecimal(pageHeight));
                            pageIndex += decimals[0].intValue() + 1;
                            tempY = top - decimals[1].floatValue();
                        }
                    }
                    for (int chIndex = 0; chIndex < line.line.length(); chIndex++) {
                        tempY -= singleCharHeight;
                        if (tempY < bottom) {
                            tempY = top;
                            pageIndex++;
                        }
                        PDFWriteUtils.writeSimple(doc, pageIndex, line.line.charAt(chIndex), cell.textColor, font, cell.fontSize, tempX, tempY, false, false, cell.lineSpace);
                    }
                    tempX += singleCharWidth;
                }
            }
        }
    }

    private List<String> sub(List<String> strings, int length) {
        List<String> result = new ArrayList<>();
        for (String string : strings) {
            for (int i = 0; i < string.length(); i += length) {
                result.add(StrUtil.sub(string, i, i + length));
            }
        }
        return result;
    }

    private int extractSubLine(List<String> strings, int length) {
        int result = 0;
        for (String string : strings) {
            result += new BigDecimal(string.length()).divide(new BigDecimal(length), 0, RoundingMode.UP).intValue();
        }
        return result;
    }

    /**
     * 设置行的x、y坐标已经页索引
     *
     * @param cell             单元格
     * @param x                初始x坐标
     * @param top              页面最大高度
     * @param bottom           页面最小高度
     * @param startY           起始y坐标
     * @param singleCharHeight 单位字符高度
     * @param pageIndex        初始索引页
     */
    private void setStartXAndStartYAndIndexPage(TableCell cell, float x, float top, float bottom, float startY, float singleCharHeight, int pageIndex) {
        for (int i = 0; i < CollUtil.size(cell.lines); i++) {
            Line line = cell.lines.get(i);
            if (startY < (bottom + singleCharHeight)) {                                                                                                                           // 换页 设置 页面索引、y坐标
                startY = top;
                pageIndex++;
            }
            startY -= singleCharHeight;
            line.setIndexPage(pageIndex);
            line.setY(startY);
            line.setX(x + cell.paddingLeft);
        }
    }

    private float startX(Alignment horizontalAlignment, float cellWidth, String text, PDFont font, float fontSize, float lineSpace, float x, float paddingLeft, float paddingRight) {
        if (horizontalAlignment.isLeft()) {
            return x + paddingLeft;
        } else if (horizontalAlignment.isRight()) {
            return x + cellWidth - FontUtils.width(font, text, fontSize, lineSpace) - paddingRight;
        } else {
            return x + ((cellWidth - FontUtils.width(font, text, fontSize, lineSpace)) / 2);
        }
    }

    private Pair<Integer, Float> startYAndStartPage(Alignment verticalAlignment, int startPageIndex, float startY, float paddingTop, float paddingBottom, float height, int totalLines, float singleCharHeight, float bottom, float pageHeight) {
        if (verticalAlignment.isTop()) {                                                                                // 垂直
            return new MutablePair<>(startPageIndex, startY - paddingTop);
        } else if (verticalAlignment.isBottom()) {
            float topDistance = height - (totalLines * singleCharHeight) - paddingBottom;                               // 距离顶部
            float emptySpace = startY - topDistance;                                                                    // 空白空间
            return extractVerticalStartYAndStartPage(startPageIndex, startY, bottom, pageHeight, emptySpace);
        } else {
            float emptySpace = (height - (totalLines * singleCharHeight)) / 2;                                          // 空白空间
            return extractVerticalStartYAndStartPage(startPageIndex, startY, bottom, pageHeight, emptySpace);
        }
    }

    /**
     * 计算y起始页和起始位置
     */
    private Pair<Integer, Float> extractVerticalStartYAndStartPage(int startPageIndex, float startY, float bottom, float pageHeight, float emptySpace) {
        float firstPageEmptySpace = startY - bottom;                                                                    // 首页剩余空间
        if (emptySpace > firstPageEmptySpace) {                                                                         // 换页
            BigDecimal totalHeight = new BigDecimal(emptySpace - firstPageEmptySpace);
            BigDecimal pageHeightBigDecimal = new BigDecimal(pageHeight);
            return new MutablePair<>(startPageIndex + totalHeight.divide(pageHeightBigDecimal, 0, RoundingMode.UP).intValue(), pageHeight - totalHeight.divideAndRemainder(pageHeightBigDecimal)[1].floatValue());
        }
        return new MutablePair<>(startPageIndex, startY - emptySpace);
    }


    /**
     * 绘制边框
     *
     * @param doc            文档
     * @param cell           单元格
     * @param startPageIndex 起始页
     * @param rowY           当前行起始y坐标
     * @param rowX           当前行起始x坐标
     * @param top            新页y起始坐标
     * @param bottom         底部空间
     * @return 当前行结束时y坐标
     */
    private Pair<Integer, Float> drawBorder(PDDocument doc, TableCell cell, final int startPageIndex, float rowY, float rowX, float top, float bottom) {
        float startY = rowY;                                                                                                        // 初始化起始y坐标,用于计算y空间
        float remainingHeight = cell.height;                                                                                        // 剩余需要打印的高度
        for (int pageIndex = startPageIndex; ; pageIndex++) {
            float ySpace = startY - bottom;                                                                                         // 当前页可用y容量
            boolean firstPage = pageIndex == startPageIndex;                                                                        // 是否为首页
            if (ySpace >= remainingHeight) {                                                                                        // 当前页可容纳则直接输入
                float endY = startY - remainingHeight;
                drawLine(doc, cell, pageIndex, rowX, rowX + cell.width, startY, endY, firstPage, true);               // 画出边框
                return new MutablePair<>(pageIndex, endY);                                                                          // 跳出循环
            } else {                                                                                                                // 当前页可不够容纳则直接输出完剩余空间
                drawLine(doc, cell, pageIndex, rowX, rowX + cell.width, startY, bottom, firstPage, false);             // 画出边框
                remainingHeight -= ySpace;                                                                                          // 计算剩余所需高度
                startY = top;                                                                                                       // 重置到下一页起始y
            }
        }
    }

    /**
     * 划出边框
     *
     * @param doc       文档
     * @param cell      单元格
     * @param pageIndex 页面索引
     * @param startX    起始x
     * @param endX      结束x
     * @param startY    起始y
     * @param endY      结束y
     * @param top       是否封闭 上
     * @param bottom    是否封闭 下
     */
    private void drawLine(PDDocument doc, TableCell cell, int pageIndex, float startX, float endX, float startY, float endY, boolean top, boolean bottom) {
        if (Objects.nonNull(cell.backgroundColor)) {
            PDFWriteUtils.drawBackgroundColor(doc, cell.backgroundColor, pageIndex, startX, startY, endX, endY);
        }
        if (top && cell.borderWidthTop > 0) {
            PDFWriteUtils.drawLine(doc, cell.getBorderColor(), pageIndex, startX, startY, endX, startY, cell.borderWidthTop);       // 上
        }
        if (bottom && cell.borderWidthBottom > 0) {
            PDFWriteUtils.drawLine(doc, cell.getBorderColor(), pageIndex, startX, endY, endX, endY, cell.borderWidthBottom);        // 下
        }
        if (cell.borderWidthLeft > 0) {
            PDFWriteUtils.drawLine(doc, cell.getBorderColor(), pageIndex, startX, startY, startX, endY, cell.borderWidthLeft);      // 左
        }
        if (cell.borderWidthRight > 0) {
            PDFWriteUtils.drawLine(doc, cell.getBorderColor(), pageIndex, endX, startY, endX, endY, cell.borderWidthRight);         // 右
        }
    }

    /**
     * 计算单元格位置
     *
     * @param doc        文档
     * @param tableStyle 全局表格样式
     * @param table      表格
     */
    private void extractCellPosition(PDDocument doc, Style tableStyle, List<TableRow> table) {
        Float totalWidth = tableStyle.getTotalWidth();
        TableRow firstRow = table.get(0);
        int cellNum = firstRow.getCells().stream().mapToInt(c -> ObjectUtil.defaultIfNull(c.colspan, 1)).sum();
        float perCellWidth = totalWidth / cellNum;
        for (int rowIndex = 0; rowIndex < table.size(); rowIndex++) {
            TableRow row = table.get(rowIndex);
            float maxHeight = ObjectUtil.defaultIfNull(row.getMinHeight(), 0F);
            for (int cellIndex = 0; cellIndex < row.cells.size(); cellIndex++) {
                TableCell cell = row.cells.get(cellIndex);
                cell.width = getCellWidth(cell, perCellWidth);                                                                                                                  // 计算当前单元格宽度
                PDFont font = loadFont(doc, cell.font);
                int lines = cell.direction.isVertical() ? extractVerticalTextLines(cell, font) : extractHorizontalTextLines(cell, font);
                maxHeight = Math.max(maxHeight, lines * (FontUtils.height(font, cell.fontSize) + (cell.direction.isVertical() ? cell.lineSpace : cell.lineDistance)) + cell.paddingTop + cell.paddingBottom);                    // 总高度
                if (Objects.nonNull(cell.rowspan) && cell.rowspan > 1) {
                    for (int j = 1; j <= cell.rowspan; j++) {
                        TableCell copyCell = TableCell.builder().build();
                        BeanUtil.copyProperties(cell, copyCell);
                        copyCell.rowspanFirst = false;
                        copyCell.rowspanCell = cell;
                        table.get(rowIndex + j).cells.add(cellIndex, copyCell);                                                                                                 // 在指定位置插入元素
                    }
                }
            }
            Opt.ofNullable(maxHeight).ifPresent(h -> row.cells.forEach(cell -> {                                                                                  // 设置当前行每一个单元格的高度为当前行最大的单元格高度
                cell.height = getCellHeight(cell, h);
                if (!cell.rowspanFirst) {
                    cell.rowspanCell.height += getCellHeight(cell, h);
                }
            }));
        }
    }

    /**
     * 计算文本所需要占用的行数
     *
     * @param cell 单元格
     * @param font 字体
     * @return 文本行数
     */
    private int extractHorizontalTextLines(TableCell cell, PDFont font) {
        float textLineSpace = cell.width - cell.paddingLeft + cell.paddingRight;                                                                                            // 计算文本行空间
        List<Line> lines = CollUtil.newArrayList(Line.builder().cell(cell).build());
        cell.setLines(lines);
        if (StrUtil.isNotBlank(cell.text)) {
            float tempLineSpace = textLineSpace;                                                                                                                            // 当前行剩余空间
            StrBuilder content = StrBuilder.create();
            for (int i = 0, lastIndex = cell.text.length() - 1; i < cell.text.length(); i++) {
                char ch = cell.text.charAt(i);
                if (ch == CharPool.CR || ch == CharPool.LF) {                                                                                                               // 包含换行符,执行换行
                    addLine(lines, content.toString(), cell, i != lastIndex);                                                                                       // 写入行内容并判断是否需要新增下一行
                    content.clear();                                                                                                                                        // 清理字符换串
                } else {                                                                                                                                                    // 普通字符
                    float charSpace = FontUtils.width(font, ch, cell.fontSize) + cell.lineSpace;                                                                            // 获取字符宽度+字符间距
                    tempLineSpace -= charSpace;                                                                                                                             // 计算当前行是否剩余空间
                    if (tempLineSpace < 0) {                                                                                                                                // 无法容纳当前字符
                        tempLineSpace = textLineSpace - charSpace;                                                                                                          // 计算新一行放置字符后剩余的控件
                        addLine(lines, content.toString(), cell, i != lastIndex);                                                                                   // 将字符放置到下一行
                        content.clear();                                                                                                                                    // 清理字符换串
                    }
                    content.append(ch);                                                                                                                                     // 存入字符串缓存中
                }
            }
            if (!content.isEmpty()) {                                                                                                                                       // 缓存字符串未写入行信息
                addLine(lines, content.toString(), cell, false);                                                                                                    // 执行写入
            }
        }
        return lines.size();
    }

    private int extractVerticalTextLines(TableCell cell, PDFont font) {
        float textWidth = cell.width - cell.paddingLeft + cell.paddingRight;                                                                                                      // 计算文本行空间
        int lineTotal = new BigDecimal(textWidth / (FontUtils.width(font, cell.fontSize) + cell.lineSpace)).setScale(0, RoundingMode.DOWN).intValue();              // 总行数
        List<String> lineStr = splitLines(cell.text, CharPool.CR, CharPool.LF);
        if (CollUtil.size(lineStr) > lineTotal) {                                                                                                                                 // 根据换行符分割后超出行,去除换行符
            cell.setText(StrUtil.removeAll(cell.text, CharPool.CR, CharPool.LF));
            lineStr = CollUtil.newArrayList(cell.text);
        }
        int length = findLengthOfLine(lineStr, lineTotal);
        cell.setLines(sub(lineStr, length).stream().map(s -> Line.builder().line(s).cell(cell).build()).collect(Collectors.toList()));
        return length;
    }

    public int findLengthOfLine(List<String> strList, int maxLine) {
        int result = 1;
        if (CollUtil.isNotEmpty(strList)) {
            int maxLength = strList.stream().max(Comparator.comparingInt(String::length)).orElse(StrPool.COLON).length();
            if (strList.size() >= maxLine) {                                                                               // 列表大于等于行数,返回大字符数
                result = maxLength;
            } else {                                                                                                        // 小于行
                result = lengthSearch(strList, maxLength, maxLine);
            }
        }
        return result;
    }

    private int lengthSearch(List<String> strList, int maxLength, int line) {
        for (int length = maxLength; length > 0; length--) {
            int subLine = extractSubLine(strList, length);
            if (subLine >= line) {
                return length + 1;
            }
        }
        return 1;
    }

    /**
     * 分割行
     *
     * @param context 文本
     * @param chars   分割字符
     * @return 行
     */
    private List<String> splitLines(String context, char... chars) {
        List<String> result = CollUtil.newArrayList(context);
        for (char ch : chars) {
            List<String> temp = new ArrayList<>();
            for (String str : result) {
                temp.addAll(StrUtil.split(str, ch));
            }
            result = temp;
        }
        return result;
    }

    /**
     * 增加空格
     *
     * @param length 长度
     * @return 空格字符
     */
    private String appendSpace(int length) {
        StrBuilder sb = StrBuilder.create();
        for (int i = 0; i < length; i++) {
            sb.append(SPACE);
        }
        return sb.toString();
    }

    /**
     * 添加行
     *
     * @param lines   行
     * @param content 文本
     * @param cell    单元格
     * @param newLine 是否新增下一行
     */
    private void addLine(List<Line> lines, String content, TableCell cell, boolean newLine) {
        CollUtil.getLast(lines).setLine(content);                                               // 将字符串设置到最后一个行内容中
        if (newLine) {                                                                          // 是否已经读取到最后一个字符,若否则继续增加行
            lines.add(Line.builder().cell(cell).build());
        }
    }

    /**
     * 计算单元格高度
     *
     * @param cell      单元格
     * @param maxHeight 最大高度
     * @return 高度
     */
    private float getCellHeight(TableCell cell, float maxHeight) {
        if (Objects.nonNull(cell.height)) {
            return cell.height;
        } else {
            return maxHeight;
        }
    }

    /**
     * 计算单元格宽度
     *
     * @param cell         单元格
     * @param perCellWidth 平均宽度
     * @return 宽度
     */
    private float getCellWidth(TableCell cell, float perCellWidth) {
        if (Objects.isNull(cell.width)) {
            return perCellWidth * ObjectUtil.defaultIfNull(cell.colspan, 1);
        } else if (cell.getWidth() < 1) {
            return perCellWidth * perCellWidth * ObjectUtil.defaultIfNull(cell.colspan, 1);
        } else {
            return cell.width;
        }
    }

    /**
     * 提前带样式的表格
     *
     * @param element 节点
     * @param params  当前表格内容
     * @return 带样式的表格
     */
    private List<TableRow> extractTableDataWithStyles(Element element, Style style, Map<String, Object> params) {
        List<TableRow> table = new ArrayList<>();                                                                                           // 表格
        Map<String, Field> rowField = STYLE_KEY.get(TableRow.class.getSimpleName());                                                        // 行属性
        Map<String, Field> cellField = STYLE_KEY.get(TableCell.class.getSimpleName());                                                      // 单元格属性
        for (Element tr : element.elements(TR)) {                                                                                           // 行
            TableRow row = loadStyle(rowField, copy(style, TableRow.class), tr, params);                                                    // 行样式设置
            List<TableCell> cells = new ArrayList<>();
            for (Element td : tr.elements(TD)) {                                                                                            // 单元格
                TableCell cell = initCell(loadStyle(cellField, copy(row, TableCell.class), td, params));                                    // 单元格样式设置
                cell.setText(td.getText());                                                                                                 // 文本内容
                cells.add(cell);
            }
            row.setCells(cells);
            table.add(row);
        }
        return table;
    }

    /**
     * 初始化单元格
     *
     * @param cell 单元格
     */
    private TableCell initCell(TableCell cell) {
        cell.setBorderWidthTop(ObjectUtil.defaultIfNull(cell.getBorderWidthTop(), 1F));
        cell.setBorderWidthBottom(ObjectUtil.defaultIfNull(cell.getBorderWidthBottom(), 1F));
        cell.setBorderWidthLeft(ObjectUtil.defaultIfNull(cell.getBorderWidthLeft(), 1F));
        cell.setBorderWidthRight(ObjectUtil.defaultIfNull(cell.getBorderWidthRight(), 1F));
        cell.setRowspanFirst(true);
        return cell;
    }

    /**
     * 表格默认样式
     *
     * @param element 节点
     * @param params  动态参数
     * @return 默认样式
     */
    private Style defaultTableStyle(Element element, Map<String, Object> params) {
        PageStruct setting = setting();                                                                                                     // 全局设置
        return loadStyle(STYLE_KEY.get(Style.class.getSimpleName()), Style.builder()
                .x(currentX())
                .y(currentY())
                .totalWidth(setting.limitX - setting.margin.left)
                .textColor(setting.pdColor)
                .direction(setting.textDirection)
                .verticalAlignment(Alignment.MIDDLE)
                .horizontalAlignment(Alignment.CENTER)
                .fontSize(setting.fontSize)
                .borderStyle(BorderStyle.SOLID)
                .borderColor(Color.BLACK)
                .borderSize(1F)
                .paddingTop(1F)
                .paddingBottom(1F)
                .paddingLeft(1F)
                .paddingRight(1F)
                .lineDistance(1F)
                .lineSpace(setting.lineSpace)
                .build(), element, params);
    }

    /**
     * 赋值
     *
     * @param style 样式
     * @param clazz 类型
     * @param <T>   类型
     * @return 样式
     */
    private <T extends Style> T copy(Style style, Class<T> clazz) {
        T result = ReflectUtil.newInstance(clazz);
        BeanUtil.copyProperties(style, result);
        return result;
    }

    /**
     * 简单样式属性
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Style {
        protected Float x;                                  // x坐标
        protected Float y;                                  // y坐标
        protected Float totalWidth;                         // 宽度
        @StyleProperty("background")
        protected Color backgroundColor;                    // 背景色
        @StyleProperty("color")
        protected Color textColor;                          // 文本颜色
        protected TextDirection direction;                  // 文本对齐方式 - 水平、垂直
        protected Alignment verticalAlignment;              // 文本对齐方式 - 水平：居中、左对齐、右对齐 ， 垂直：居中、上对齐、下对齐
        protected Alignment horizontalAlignment;            // 文本对齐方式 - 水平：居中、左对齐、右对齐 ， 垂直：居中、上对齐、下对齐
        protected String font;                              // 字体
        protected Float fontSize;                           // 字体大小
        protected BorderStyle borderStyle;                  // 边框样式
        protected Color borderColor;                        // 边框颜色
        protected Float borderSize;                         // 边框大小
        protected Float paddingTop;                         // 内边距 上
        protected Float paddingBottom;                      // 内边距 下
        protected Float paddingLeft;                        // 内边距 左
        protected Float paddingRight;                       // 内边距 右
        protected Float lineDistance;                       // 行距
        protected Float lineSpace;                          // 字体间距
    }

    /**
     * 行属性
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    protected static class TableRow extends Style {
        protected Float minHeight;               // 最小高度
        protected List<TableCell> cells;         // 单元格列表

    }

    /**
     * 单元格属性
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    protected static class TableCell extends Style {
        protected Float width;                   // 宽度
        protected Float height;                  // 高度
        protected Float borderWidthTop;          // 边框宽度 上
        protected Float borderWidthBottom;       // 边框宽度 下
        protected Float borderWidthLeft;         // 边框宽度 左
        protected Float borderWidthRight;        // 边框宽度 右
        protected Integer rowspan;               // 跨行
        protected Integer colspan;               // 跨列
        protected Boolean rowspanFirst;          // 跨行起始页
        protected TableCell rowspanCell;         // 跨行源单元格
        protected String text;                   // 文本
        protected List<Line> lines;              // 行内容
    }


    /**
     * 行
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    protected static class Line {
        protected int indexPage;
        protected Float x;
        protected Float y;
        protected String line;
        protected TableCell cell;
    }

}
