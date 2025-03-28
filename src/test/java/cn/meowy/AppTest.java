/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package cn.meowy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.factory.manager.PDFManager;
import cn.meowy.pdf.factory.manager.PageManager;
import cn.meowy.pdf.struct.AlignmentCenter;
import cn.meowy.pdf.utils.FontUtils;
import cn.meowy.pdf.utils.PDFUtils;
import cn.meowy.pdf.utils.TemplateUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.settings.Settings;
import org.vandeseer.easytable.structure.Table;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
@Slf4j
public class AppTest {

    private final static String PATH;

    private final static String TEMP_PATH;

    private final static String TEMPLATE_PATH;

    private final static String FONT_PATH;
    private final static String FONT_NAME;

    private final static String TEMPLATE_NAME;

    private final static float PADDING = 10;

    static {
        String BASE_DIR = getBasePath();
        PATH = BASE_DIR + "temp/test.pdf";
        TEMP_PATH = BASE_DIR + "temp/template";
        TEMPLATE_PATH = BASE_DIR + "template";
        FONT_PATH = BASE_DIR + "ttf";
        FONT_NAME = "TW-Kai-98_1";
        TEMPLATE_NAME = "one/body.ftl";

    }


    @Test
    @SneakyThrows
    public void testApp() {
        PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDType1Font font = new PDType1Font(document, Files.newInputStream(new File(FONT_PATH).toPath()));
        float startY = page.getMediaBox().getHeight() - PADDING;
        try (final PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            Table table = Table.builder()
                    .build();

            TableDrawer.builder()
                    .page(page)
                    .contentStream(contentStream)
                    .table(table)
                    .startX(PADDING)
                    .startY(startY)
                    .endY(PADDING)
                    .build()
                    .draw(() -> document, () -> new PDPage(PDRectangle.A4), PADDING);

            startY -= (table.getHeight() + PADDING);

        }

        document.save(PATH);
        document.close();
    }


    @SneakyThrows
    @Test
    public void xmlTest() {
        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("/Applications/Java/workspace/self/pdf-utils/template/one/contact-table.ftl"));
        Document document = reader.read(new File("/Applications/Java/workspace/self/pdf-utils/template/one/body.ftl"));
        log.debug("end");
    }

    @Test
    public void managerTest() {
        PDFManager pdfManager = new PDFManager() {
        };
        PageManager pageManager = new PageManager();
        System.out.println("");
    }

    @Test
    @SneakyThrows
    public void pdfTest() {
        FileUtil.del(PATH);
        PDDocument document = new PDDocument();
        PDRectangle pageSize = new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
        float x = pageSize.getWidth() / 2;
        float y = pageSize.getHeight() / 2;
        PDFont font = FontUtils.loadFont(document, FONT_PATH + "/ali/" + FONT_NAME + ".ttf");
//        AlignmentLeft left = new AlignmentLeft(RandomStringUtils.randomAlphabetic(666), font, 20, 10, 100, 100, 100, 10, 100, 100, 100, PDRectangle.A4);
//        PDFWriteUtils.write(document, 0, PDRectangle.A4, font, 20, left);
//        AlignmentRight right = new AlignmentRight(RandomStringUtils.randomAlphabetic(666), font, 20, 10, 500, 700, 500, 100, 100, 100, 100, PDRectangle.A4);
//        PDFWriteUtils.write(document, 0, PDRectangle.A4, font, 20, right);
        AlignmentCenter center = new AlignmentCenter(RandomStringUtils.randomAlphabetic(666), font, 20, 10, 200, 600, 200, 100, 100, 100, 100, pageSize, false);
//        PDFWriteUtils.write(document, 0, font, 20, Color.BLACK, center);
        document.save(PATH);
        document.close();
    }

    @Test
    public void loadTemplate() {
//        Configuration load = TemplateUtils.load("/Applications/Java/workspace/self/pdf-utils/template");
    }

    @Test
    public void classTest() {
        Field[] declaredFields = ClassUtil.getDeclaredFields(Settings.class);
        System.out.println("");
    }

    @Test
    public void pdf() {
        FileUtil.del(TEMP_PATH);
        FileUtil.mkdir(TEMP_PATH);
        FileUtil.del(PATH);
        FontUtils.init(FONT_PATH);
        TemplateUtils.init(TEMPLATE_PATH, TEMP_PATH);
        PDFUtils.write(PATH, TEMPLATE_NAME, data());
    }

    private Map<String, Object> data() {
        String requestNo = RandomStringUtils.randomNumeric(32);
        return MapUtil.builder(new HashMap<String, Object>())
                .put("requestNo", requestNo)
                .put("bankName", "南宁新新银行")
                .put("accountingFirmName", "南宁新新会计师事务所")
                .put("year", "2024")
                .put("accountingFirmAddress", "广西南宁市青秀区新新大厦")
                .put("accountant", "一二三")
                .put("date", "2024年04月20日")
                .put("companyName", "Meowy网络科技有限公司")
                .put("conclusion", true)
                .put("conclusions", conclusions())
                .put("accountingFirmPhoneNumber", "13728739123")
                .put("accountingFirmFax", "0771-203233")
                .put("accountingFirmPostcode", "530000")
                .put("accountingFirmEmail", "13728739123@163.com")
                .put("infoList", dataList())
                .put("begin", "2023年04月20日")
                .put("end", "2024年04月20日")
                .put("headerContext", requestNo)
                .put("footerContext", "第 {} 页 / 共 {} 页")
                .build();
    }

    private List<Map<String, String>> dataList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add(MapUtil.builder(new HashMap<String, String>())
                    .put("data", RandomStringUtils.randomNumeric(22, 50))
                    .build());
        }
        return list;
    }

    private List<String> conclusions() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            result.add(RandomStringUtils.randomNumeric(25));
        }
        return result;
    }

    @Test
    public void logTest() {
        log.debug("----------------测试打印--------------------");
    }

    public static String getBasePath() {
        String path = ClassUtil.getClassPathResources().stream().findFirst().orElseThrow(() -> new RuntimeException("无法获取文件路径!"));
        return StrUtil.sub(path, 0, StrUtil.lastIndexOf(path, "target", path.length(), false));

    }
}
