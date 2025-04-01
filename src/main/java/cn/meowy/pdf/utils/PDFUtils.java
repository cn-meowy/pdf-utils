package cn.meowy.pdf.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.meowy.pdf.factory.manager.*;
import cn.meowy.pdf.service.FontService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.dom4j.Document;

import java.io.IOException;

/**
 * PDF工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/19
 **/
public class PDFUtils {


    static {
        PDFManager.addHandler(new PageManager(), XmlElement.PAGE);
        PDFManager.addHandler(new HeaderFooterManager(), XmlElement.HEADER, XmlElement.FOOTER);
        PDFManager.addHandler(new ParagraphManager(), XmlElement.PARAGRAPH);
        PDFManager.addHandler(new BrManager(), XmlElement.BR);
        PDFManager.addHandler(new PrManager(), XmlElement.PR);
        PDFManager.addHandler(new TableManager(), XmlElement.TABLE);
        PDFManager.addHandler(new MountManager(), XmlElement.MOUNT);
        PDFManager.addHandler(new LineManager(), XmlElement.LINE);
    }

    public static <T> void write(String pdfPath, String templateName, T data) {
        try (PDDocument document = new PDDocument()) {
            PDFManager.action(document, templateName, data);                    // 执行PDF处理
            document.save(FileUtil.touch(pdfPath));                             // 保存PDF
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void write(String pdfPath, FontService fontService, String templateName, T data) {
        try (PDDocument document = new PDDocument()) {
            PDFManager.action(document, fontService, templateName, data);                    // 执行PDF处理
            document.save(FileUtil.touch(pdfPath));                             // 保存PDF
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
