package cn.meowy.pdf.utils.attribute;

import cn.hutool.core.util.StrUtil;
import org.vandeseer.easytable.settings.VerticalAlignment;

/**
 * 垂直对齐方式
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class VerticalAlignmentAttributeParse implements AttributeParse {
    /**
     * 解析
     *
     * @param value 入参
     * @return 出参
     */
    @Override
    public VerticalAlignment parse(String value) {
        return StrUtil.isBlank(value) ? null : VerticalAlignment.valueOf(value);
    }
}
