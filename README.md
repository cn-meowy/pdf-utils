## 一个PDF工具集,可根据模板文件生成PDF。支持文本和表格。
1. 文本自动计算换行，支持水平方向左、右、居中对齐。
2. 使用原生PDFBox实现表格的绘制,支持自使用宽高,可以以单元格为单位定制边框、背景色、文本色等内容
## 使用方式
1. 构建模版: 使用freeMark语法构建模版xml。
2. 根据模版内容输出PDF。
```java
// 测试方法  运行 AppTest.pdf();查看运行结果。 
void test() {
    // 初始化字体库
    FontUtils.init(FONT_PATH);
    // 初始化模版库
    TemplateUtils.init(TEMPLATE_PATH, TEMP_PATH);
    // 输出PDF
    PDFUtils.write(PATH, TEMPLATE_NAME, data());
}

```