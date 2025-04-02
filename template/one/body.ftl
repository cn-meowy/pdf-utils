<?xml version="1.0" encoding="UTF-8" ?>
<!--
page PDF模版基础配置
rectangle 页面大小
font 全局字体
font-size 全局字体大小
color 全局颜色
line-space 全局行间距
margin 边距(顺序为上、右、下、左)
         文本占位符
*********************
header 页眉配置 标签起止位置表示作用范围
footer 页脚配置 标签起止位置表示作用范围
slot 插槽,用于引入页眉样式文件
alignment 对齐方式: LEFT-左对齐、RIGHT-右对齐、CENTER-居中对齐
x x坐标起始位置(为负数时表示由页面最大位置为坐标原点计数)
y y坐标起始位置(为负数时表示由页面最大位置为坐标原点计数)
*********************
paragraph 段落
style 风格: underline-下划线
float 浮动方式: top-上浮动,floor-下浮
*********************
br 换行符
*********************
table 表格
tr 行
th 标题行单元格
td 单元格
*********************
pr 换页符
*********************
slot 插槽,在当前页面插入插槽内容
*********************
line 横线
width 宽度
height 高度
*********************
-->
<page rectangle="A4" font="TW-Kai-98_1" font-size="12" color="BLACK" x="40" y="830" line-distance="12"
      text-direction="HORIZONTAL" alignment="LEFT" margin="20 20 20 20">
    <header slot="header.ftl">
        <footer slot="footer.ftl">
            <paragraph y="{limitY} - 50" x="{centerX}" font-size="30" alignment="CENTER">测试PDF生成</paragraph>
            <br/>
            <paragraph y="{y} - 20" x="{limitX} - 10" alignment="RIGHT">编号: ${requestNo}</paragraph>
            <br/>
            <paragraph y="{y} - 20" x="{limitX} - 10" alignment="LEFT">《故事会》是由上海文艺出版社出版的故事性杂志，创刊于1963年7月</paragraph>
            <paragraph y="{y} + 10" font-size="5" float="true">[1][2][3][4][5]</paragraph>
            <paragraph >，版面为32开，其间经历数次改版。现今《故事会》每期销量在400万册左右，在中国期刊类书籍中销量第二（排名第一的为《读者》）。1998年时候的调查表明《故事会》在世界所有期刊销量中排名第5</paragraph>
            <paragraph y="{y} + 10" font-size="5" float="true">[6]</paragraph>
            <paragraph >。</paragraph>
            <br/>
            <paragraph x="{limitX} - 10">《故事会》现为半月刊，分为“上半月·红版”与“下半月·绿版”。</paragraph>
            <br/>
            <mount slot="one/first-page-footer.ftl"/>
            <pr rectangle="B4" />
            <mount slot="one/option.ftl"/>
        </footer>
    </header>
</page>