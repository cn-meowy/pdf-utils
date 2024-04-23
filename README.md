## 一个PDF工具集,可根据模板文件生成PDF。支持文本和表格。
1. 文本自动计算换行，支持水平方向左、右、居中对齐。
2. 表格引入了开源项目easytable。https://github.com/vandeseer/easytable
```xml
<dependencies>
    <dependency>
     <groupId>com.github.vandeseer</groupId>
    <artifactId>easytable</artifactId>
    <version>1.0.1</version>
    </dependency>
</dependencies>
```
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