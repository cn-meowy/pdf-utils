package cn.meowy.pdf.utils.attribute;

import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.ColorUtils;

import java.awt.*;

/**
 * 颜色
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class ColorAttributeParse implements AttributeParse<Color> {



    /**
     * 解析
     *
     * @param value 入参
     * @return 出参
     */
    @Override
    public Color parse(String value) {
        return StrUtil.isBlank(value) ? null : ColorUtils.get(value);
    }
}
