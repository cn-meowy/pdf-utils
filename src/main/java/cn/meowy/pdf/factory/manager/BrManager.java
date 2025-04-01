package cn.meowy.pdf.factory.manager;

import cn.meowy.pdf.factory.PDFManager;
import cn.meowy.pdf.utils.FontUtils;
import cn.meowy.pdf.utils.enums.TextDirection;
import cn.meowy.pdf.utils.structure.PageStruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.dom4j.Element;

/**
 * 换行
 *
 * @author: Mr.Zou
 * @date: 2024/4/20
 **/
public class BrManager extends PDFManager {

    /**
     * 节点处理器
     *
     * @param doc      文档
     * @param element  元素节点
     * @param data     数据
     */
    @Override
    public <T> void handler(PDDocument doc, Element element, T data) {
        PageStruct struct = setting();
        if (TextDirection.HORIZONTAL.equals(struct.getTextDirection())) {                                    // 水平对齐
            float y = currentY() - FontUtils.height(struct.font, struct.fontSize) - struct.lineDistance;     // 计算换行后的y坐标值
            if (y < struct.margin.bottom) {                                                                  // 换页,重置y
                newPage(doc);                                                                                // 创建新页
                setY(struct.limitY);                                                                         // 重置y坐标
            } else {                                                                                         // 无需换页,y坐标移动到下一行
                setY(y);
            }
            setX(struct.margin.left);                                                                        // 重置x左边
        } else {                                                                                             // 垂直对齐
            float x = currentY() + FontUtils.height(struct.font, struct.fontSize) + struct.lineDistance;     // 计算换行后的x坐标值
            if (x < struct.limitX) {                                                                         // 换页,重置x
                newPage(doc);                                                                                // 创建新的一页
                setX(struct.margin.left);                                                                    // 重置x坐标
            } else {                                                                                         // 无需换页,x坐标移动到下一行
                setX(x);                                                                                     // 设置x坐标
            }
            setY(struct.limitY);                                                                             // 重置y坐标
        }
    }


}
