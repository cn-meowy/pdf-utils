package cn.meowy.pdf.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

/**
 * 模板工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/18
 **/
@Slf4j
public class TemplateUtils {

    /**
     * 配置缓存
     */
    private final static Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_23);

    private static String TEMP_FILE_PATH;

    /**
     * 加载模板配置
     *
     * @param templateDirPath 模板路径
     * @param tempFilePath    临时文件路径
     * @return 配置信息
     */
    public static void init(String templateDirPath, String tempFilePath) {
        // 判断是否为文件夹
        ExUtils.execute(() -> {
            //设置模板目录
            CONFIGURATION.setDirectoryForTemplateLoading(new File(templateDirPath));
            //设置默认编码格式
            CONFIGURATION.setDefaultEncoding("UTF-8");
        }, "初始化模板配置信息失败!");
        TEMP_FILE_PATH = StrUtil.addSuffixIfNot(tempFilePath, StrUtil.SLASH);
    }

    /**
     * 获取模板
     *
     * @param xmlTemplateName 模板名称
     * @return 模板
     */
    public static Template getTemplate(String xmlTemplateName) {
        return ExUtils.execute(() -> CONFIGURATION.getTemplate(xmlTemplateName), "获取模版失败[{}]!", xmlTemplateName);
    }

    /**
     * 加载模板文件
     *
     * @param xmlTemplateName xml模板
     * @param data            xml模板 插值数据
     * @return xml文档
     */
    public static <T> Document loadTemplate(String xmlTemplateName, T data) {
        Assert.state(StrUtil.isNotBlank(xmlTemplateName), "模板文件路径不能为空!");
        // 使用ftl 模板替换数据
        Template template = getTemplate(xmlTemplateName);
        String tempFile = TEMP_FILE_PATH + UUID.randomUUID() + ".xml";
        log.debug(tempFile + "    | " + xmlTemplateName);
        ExUtils.execute(() -> template.process(data, new FileWriter(tempFile)), "模板解析失败! {}", xmlTemplateName);
        SAXReader reader = new SAXReader();
        return ExUtils.execute(() -> reader.read(tempFile), "读取模板文件异常!");
    }


}
