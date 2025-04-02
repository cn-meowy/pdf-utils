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

## xml标签说明

```
<page/> <!-- 根节点,用于定义全局信息 -->
<header/> <!-- 页眉节点,子节点生成完毕后在页面添加上页眉 -->
<footer/> <!-- 页脚节点,子节点生成完毕后在页面添加上页脚 -->
<paragraph/> <!-- 段落,未指定起止位置时默认接续上一标签结束位置,若超过页面定义边距后自动换行 -->
<br/> <!-- 换行符,y坐标向下移动一行 -->
<pr/> <!-- 换页符,y坐标向下移动一页,新增页面大小可重新覆盖设置,未指定大小的情况下默认沿用全局设置而非上一页面的大小 -->
<mount/> <!-- 挂载符,可以将外部模板文件在当前位置进行插入 -->
<slot/> <!-- 插槽,外部模版的根节点 -->
<table/> <!-- 表格标签 -->
<tr/> <!-- 表格行标签 -->
<td/> <!-- 表格单元格标签 -->
```

## xml标签可选属性说明

```
page:
    - rectangle: 页面大小定义,例: A4
    - font: 字体定义,使用 FontUtils.init(FONT_PATH); 初始化全局的字体路径后,通过字体名称即可
    - font-size: 字体大小,例: 12
    - color: 字体颜色,可选枚举名或RGB色彩空间,例: BLACK 或 255 255 255
    - x: x坐标起始位置
    - y: y坐标起始位置
    - line-distance: 行间距
    - text-direction: 文本方向(HORIZONTAL-水平,VERTICAL-垂直)
    - alignment: 文本对齐方式(LEFT-左对齐,CENTER-居中对齐,RIGHT-右对齐)
    - margin: 边距, 从左到右分别为 上、下、左、右
header、footer、mount:
    - slot: 插槽,用于指定当前当前节点的模版样式引用
paragraph: 
    - x: x坐标起始位置
    - y: y坐标起始位置
    - float: 浮动,可选值 true/false ,默认false
    - underline: 下划线,可选值 true/false,默认false
br:
    - 无可选属性
pr:
    - rectangle: 页面大小,从换页后为起始页到一下个手工换页处将使用新页面大小进行内容绘制
table:
    - x: x坐标起始位置
    - y: y坐标起始位置
    - background: 背景色,可选颜色枚举或RGB色彩空间,例: BLACK 或 255 255 255
    - color: 文本颜色,可选颜色枚举或RGB色彩空间,例: BLACK 或 255 255 255
    - direction: 文本方向
    - vertical-alignment: 垂直对齐方式(BOTTOM-底, MIDDLE-居中, TOP-上)
    - horizontal-alignment: 水平对齐方式(LEFT-左对齐,CENTER-居中对齐,RIGHT-右对齐)
    - font: 字体
    - font-size: 字体大小
    - border-style: 边框样式(未实现)
    - border-color: 边框颜色,可选颜色枚举或RGB色彩空间,例: BLACK 或 255 255 255
    - border-size: 边框大小
    - padding-top: 边距 上
    - padding-bottom: 边距 下
    - padding-left: 边距 左
    - padding-right: 边距 右
    - line-distance: 行间距
    - line-space: 字间距
tr:
    - min-height: 最小高度
    - 除x、y属性外可使用table相同属性进行覆盖
td:
    - border-width-top: 边框宽度 上
    - border-width-bottom: 边框宽度 下
    - border-width-left: 边框宽度 左
    - border-width-right: 边框宽度 右
    - rowspan: 跨行合并
    - colspan: 跨列合并
```

## xml属性动态参数说明

1. {} 包裹的字符串可解析为运行时动态参数,模板文件读取后会动态加载,若为数字类型可执行数学运算

```
{x}: 当前绘制x位置
{y}: 当前绘制y位置
{centerX}: 当前页面中中心点x坐标
{centerY}: 当前页面中中心点y坐标
{limitX}: 限制x坐标, 当前页面宽度减右边距
{limitY}: 限制y坐标, 当前页面高度减上边距
{margin.top}: 上边距
{margin.bottom}: 下边距
{margin.left}: 左边距
{margin.right}: 右边距
{fontSize}: 全局字体大小
{lineDistance}: 全局行间距
{rectangle.height}: 当前页面高度
{rectangle.width}: 当前页面宽度
```