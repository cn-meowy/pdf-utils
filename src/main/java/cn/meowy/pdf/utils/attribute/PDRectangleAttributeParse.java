package cn.meowy.pdf.utils.attribute;

import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.XmlAttribute;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PDF
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class PDRectangleAttributeParse implements AttributeParse {

    public final static Map<String, PDRectangle> PD_RECTANGLE_MAP = new ConcurrentHashMap<>();

    static {
        PD_RECTANGLE_MAP.put("A0", PDRectangle.A0);
        PD_RECTANGLE_MAP.put("A1", PDRectangle.A1);
        PD_RECTANGLE_MAP.put("A2", PDRectangle.A2);
        PD_RECTANGLE_MAP.put("A3", PDRectangle.A3);
        PD_RECTANGLE_MAP.put("A4", PDRectangle.A4);
        PD_RECTANGLE_MAP.put("A5", PDRectangle.A5);
        PD_RECTANGLE_MAP.put("A6", PDRectangle.A6);
        PD_RECTANGLE_MAP.put("LETTER", PDRectangle.LETTER);
        PD_RECTANGLE_MAP.put("LEGAL", PDRectangle.LEGAL);
        PD_RECTANGLE_MAP.put("TABLOID", PDRectangle.TABLOID);
        PD_RECTANGLE_MAP.put("B4", new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
    }

    /**
     * 解析
     *
     * @param value 入参
     * @return 出参
     */
    @Override
    public PDRectangle parse(String value) {
        return StrUtil.isBlank(value) ? null : PD_RECTANGLE_MAP.get(value);
    }
}
