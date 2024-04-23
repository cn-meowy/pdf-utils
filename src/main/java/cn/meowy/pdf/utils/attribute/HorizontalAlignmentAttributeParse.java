package cn.meowy.pdf.utils.attribute;

import cn.hutool.core.util.StrUtil;
import org.vandeseer.easytable.settings.HorizontalAlignment;

/**
 * 水平对齐方式
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class HorizontalAlignmentAttributeParse implements AttributeParse {
    /**
     * 解析
     *
     * @param value 入参
     * @return 出参
     */
    @Override
    public HorizontalAlignment parse(String value) {
        return StrUtil.isBlank(value) ? null : HorizontalAlignment.valueOf(value);
    }
}
