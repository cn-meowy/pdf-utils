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
            <paragraph y="{limitY} - 50" x="{centerX}" font-size="30" alignment="CENTER">银行询证函(格式一)</paragraph>
            <br/>
            <paragraph y="{y} - 20" x="{limitX} - 10" alignment="RIGHT">编号: ${requestNo}</paragraph>
            <br/>
            <paragraph y="{y} - 20" x="{margin.left}" underline="true">${bankName}</paragraph>
            <paragraph>(以下简称“贵行”，即“函证收件人”)</paragraph>
            <br/>
            <paragraph x="{margin.left} + 20">本公司聘请的 [</paragraph>
            <paragraph underline="true">${accountingFirmName}</paragraph>
            <paragraph>]正对本公司[</paragraph>
            <paragraph underline="true">${year}</paragraph>
            <paragraph>年度(或期间)]的财务报表进行审计，按照[中国注册会计师审计准则][列明其他相关审计准则名称]的要求，应当询证本公司与贵行相关的信息。下列第1-14项及附表(如适用)信息出自本公司的记录：</paragraph>
            <br/>
            <paragraph x="{margin.left} + 20">(1)如与贵行记录相符，请在本函“结论部分[签字或签章]或[签发电子签名]</paragraph>
            <paragraph y="{y} + 5" font-size="5" float="true">1</paragraph>
            <paragraph>”；</paragraph>
            <br/>
            <paragraph x="{margin.left} + 20">(2)如有不符，请在本函“结论”部分列表不符项目及具体内容，并签字或签章]或[签发电子签名]。</paragraph>
            <br/>
            <paragraph x="{margin.left} + 20">本公司谨授权贵行将回函直接寄至</paragraph>
            <paragraph underline="true">${accountingFirmAddress}</paragraph>
            <paragraph>[或直接转交</paragraph>
            <paragraph underline="true">${accountant}</paragraph>
            <paragraph>函证经办人</paragraph>
            <paragraph y="{y} + 5" font-size="5" float="true">2</paragraph>
            <paragraph>]，地址及联系方式</paragraph>
            <paragraph y="{y} + 5" font-size="5" float="true">3</paragraph>
            <paragraph>如下：</paragraph>
            <br/>
            <mount slot="one/contact-table.ftl"/>
            <paragraph x="{margin.left} + 20">本公司谨授权贵行可从本公司账户支取办理本询证函回函服务的费用(如适用)。
            </paragraph>
            <br/>
            <paragraph x="{margin.left} + 20">截至[</paragraph>
            <paragraph underline="true">${date}</paragraph>
            <paragraph>](即“函证基准日”)，本公司与贵行相关的信息</paragraph>
            <paragraph y="{y} + 5" font-size="5" float="true">4</paragraph>
            <paragraph>列示如下：</paragraph>
            <br/>
            <mount slot="one/first-page-footer.ftl"/>
            <pr rectangle="B4" />
            <mount slot="one/option.ftl"/>
            <pr />
            <mount slot="one/conclusion.ftl"/>
        </footer>
    </header>
</page>