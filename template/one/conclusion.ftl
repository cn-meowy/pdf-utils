<?xml version="1.0" encoding="UTF-8" ?>
<#-- 结论页 -->
<slot>
    <br/>
    <paragraph></paragraph>
    <br/>
    <table x="{margin.left} * 2" column-width="180 150 180" horizontal-alignment="LEFT">
        <tr>
            <td colum-span="3" border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">函证编号: ${requestNo}</td>
        </tr>
        <#if customerRequestNo??>
            <tr>
                <td colum-span="3" border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">客户函证编号: ${customerRequestNo}</td>
            </tr>
        </#if>
        <tr>
            <td colum-span="3" border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">企业名称: ${companyName}</td>
        </tr>
        <tr>
            <td border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="1"></td>
            <td horizontal-alignment="CENTER" border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">以下由被询证银行填列</td>
            <td border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="1"></td>
        </tr>
    </table>
    <#--    <line x="{margin.left} + 50" width="{c}" height="1" />-->
    <#--    <paragraph x="{x}">以下由被询证银行填列</paragraph>-->
    <#--    <line x="{x}" width="100" height="1"/>-->
    <br/>
    <br/>
    <table x="{margin.left}" horizontal-alignment="LEFT">
        <tr>
            <td colum-span="6" border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">结论: </td>
        </tr>
        <tr>
                <td colum-span="6" border-width-top="1" border-width-left="1" border-width-right="1" border-width-bottom="0"><#if conclusion>经本行核对,正确<#else>经本行核对,错误</#if></td>
        </tr>
        <#list conclusions as item>
            <tr>
                <td colum-span="6" border-width-top="0" border-width-left="1" border-width-right="1" border-width-bottom="0">${item}</td>
            </tr>
        </#list>
    </table>
    <table x="{margin.left}" horizontal-alignment="LEFT" block="true">
        <tr height="{y} - 150 - {margin.bottom}">
            <td border-width-top="0" border-width-left="1" border-width-right="1" border-width-bottom="0" colum-span="6"></td>
        </tr>
        <tr horizontal-alignment="RIGHT" height="50">
            <td border-width-top="0" border-width-left="1" border-width-right="0" border-width-bottom="0" colum-span="2">${date}</td>
            <td border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">经办人: ${accountant}</td>
            <td border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">职务: 经办人</td>
            <td border-width-top="0" border-width-left="0" border-width-right="1" border-width-bottom="0" colum-span="2" horizontal-alignment="LEFT">电话: ${accountingFirmPhoneNumber}</td>
        </tr>
        <tr horizontal-alignment="RIGHT" height="50">
            <td border-width-top="0" border-width-left="1" border-width-right="0" border-width-bottom="0" colum-span="2"></td>
            <td border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">复核人: ${accountant}</td>
            <td border-width-top="0" border-width-left="0" border-width-right="0" border-width-bottom="0">职务: 审核人</td>
            <td border-width-top="0" border-width-left="0" border-width-right="1" border-width-bottom="0" colum-span="2" horizontal-alignment="LEFT">电话: ${accountingFirmPhoneNumber}</td>
        </tr>
        <tr horizontal-alignment="RIGHT" height="50">
            <td border-width-top="0" border-width-left="1" border-width-right="0" border-width-bottom="1" colum-span="4">(银行盖章)</td>
            <td border-width-top="0" border-width-left="0" border-width-right="1" border-width-bottom="1" colum-span="2"></td>
        </tr>
    </table>
</slot>