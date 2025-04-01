package cn.meowy.pdf.utils.attribute;

import cn.hutool.core.util.StrUtil;
import cn.meowy.pdf.utils.enums.BooleanEnums;

/**
 * Boolean
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class BooleanAttributeParse implements AttributeParse<Boolean> {

    /**
     * 解析
     *
     * @param value 入参
     * @return 出参
     */
    @Override
    public Boolean parse(String value) {
        return StrUtil.isBlank(value) ? null : BooleanEnums.get(value);
    }
}
