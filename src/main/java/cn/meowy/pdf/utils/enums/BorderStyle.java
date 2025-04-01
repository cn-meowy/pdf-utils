package cn.meowy.pdf.utils.enums;

import lombok.AllArgsConstructor;

/**
 * 边框样式
 *
 * @author: Mr.Zou
 * @date: 2025-03-29
 **/
@AllArgsConstructor
public enum BorderStyle {
    NONE,   // 无边框
    DOTTED, // 点状边框
    DASHED, // 虚线边框
    SOLID,  // 实线边框
    DOUBLE, // 双线边框
    GROOVE, // 三维沟槽边框
    RIDGE,  // 三维脊状边框
    INSET,  // 三维嵌入边框
    OUTSET, // 三维突出边框
    HIDDEN, // 隐藏边框

}
