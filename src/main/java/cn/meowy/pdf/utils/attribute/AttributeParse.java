package cn.meowy.pdf.utils.attribute;

/**
 * 元素解析
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public interface AttributeParse<R> {

    /**
     * 解析
     *
     * @param value 入参
     * @return 出参
     */
    R parse(String value);

}
