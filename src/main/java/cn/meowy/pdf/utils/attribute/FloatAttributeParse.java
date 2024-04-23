package cn.meowy.pdf.utils.attribute;

import cn.hutool.core.util.StrUtil;

/**
 * float
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class FloatAttributeParse implements AttributeParse {


    /**
     * 解析
     *
     * @param value 入参
     * @return 出参
     */
    @Override
    public Float parse(String value) {
        return StrUtil.isBlank(value) ? null : Float.parseFloat(value);
    }
}
