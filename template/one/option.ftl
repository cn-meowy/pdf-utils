<?xml version="1.0" encoding="UTF-8" ?>
<!--
1 - 14项
-->
<slot>
    <paragraph>1. 表格打印测试</paragraph>
    <br/>

    <paragraph>跨列展示</paragraph>
    <br/>
    <table x="20">
        <tr>
            <td rowspan="2">第一列</td>
            <td rowspan="2">第二列</td>
            <td rowspan="2">#[第三列]</td>
            <td rowspan="2">第四列</td>
            <td rowspan="2">第五列</td>
            <td rowspan="2">第六列</td>
            <td rowspan="2">第七列</td>
            <td colspan="2">#[第八列]</td>
            <td rowspan="2">第九列</td>
        </tr>
        <tr>
            <td>第八列[1]</td>
            <td>第八列[2]</td>
        </tr>
        <#list infoList as item>
            <tr>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td colspan="2">${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
</slot>