package cn.meowy.pdf.utils.attribute;

/**
 * 元素解析
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public interface AttributeParse {

    /**
     * 解析
     *
     * @param value 入参
     * @param <R>   出参类型
     * @return 出参
     */
    <R> R parse(String value);

}
