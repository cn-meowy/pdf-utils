<?xml version="1.0" encoding="UTF-8" ?>
<!--
1 - 14项
-->
<slot>
    <paragraph>1. 银行存款</paragraph>
    <br/>
    <table x="{margin.left}" border-color="BLUE">
        <tr>
            <td>账户名称</td>
            <td>银行账号</td>
            <td>币种</td>
            <td>账户类型</td>
            <td>利率</td>
            <td>账户余额</td>
            <td>是否属于资金归集(资金池或其他资金监管)账户</td>
            <td>起始日期</td>
            <td>终止日期</td>
            <td>是否用于冻结、担保或存在其他使用限制(如是请注明)</td>
            <td>备注</td>
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
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的银行存款(包括余额为零的存款账户)外，本公司并无在贵行的其他存款。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>2. 银行借款</paragraph>
    <br/>
    <table x="20" border-color="RED">
        <tr>
            <td>借款人名称</td>
            <td>借款账号</td>
            <td>币种</td>
            <td>余额</td>
            <td>借款日期</td>
            <td>到期日期</td>
            <td>利率</td>
            <td>抵(质)押品/担保人</td>
            <td>备注</td>
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
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的银行借款外，本公司并无自贵行的其他借款。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>3. 自</paragraph>
    <paragraph underline="true"> ${begin} </paragraph>
    <paragraph>至</paragraph>
    <paragraph underline="true"> ${end} </paragraph>
    <paragraph>期间内注销的银行存款账户</paragraph>
    <br/>
    <table x="20" border-color="YELLOW">
        <tr>
            <td>借款人名称</td>
            <td>借款账号</td>
            <td>币种</td>
            <td>余额</td>
            <td>借款日期</td>
            <td>到期日期</td>
            <td>利率</td>
            <td>抵(质)押品/担保人</td>
            <td>备注</td>
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
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的注销账户外，本公司在此期间并未在贵行注销其他账户。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>4. 本公司作为委托人的委托贷款</paragraph>
    <br/>
    <table x="20" border-color="GREEN">
        <tr>
            <td>账户名称</td>
            <td>银行结算账号</td>
            <td>资金借入方</td>
            <td>币种</td>
            <td>利率</td>
            <td>余额</td>
            <td>本公司作为委托人的委托贷款起息日期</td>
            <td>本公司作为委托人的委托贷款到期日期</td>
            <td>备注</td>
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
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的委托贷款外，本公司并无通过贵行办理的其他以本公司作为委托人的委托贷款。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>5. 本公司作为借款人的委托贷款</paragraph>
    <br/>
    <table x="20" border-color="PRUPLE">
        <tr>
            <td>账户名称</td>
            <td>银行结算账号</td>
            <td>资金借出方</td>
            <td>币种</td>
            <td>利率</td>
            <td>余额</td>
            <td>本公司作为借款人的委托贷款起息日期</td>
            <td>本公司作为借款人的委托贷款到期日期</td>
            <td>备注</td>
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
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的委托贷款外，本公司并无通过贵行办理的其他以本公司作为借款人的委托贷款。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>6(1). 本公司为其他单位提供的、以贵行为担保受益人的担保</paragraph>
    <br/>
    <table x="20" border-color="ORANGE">
        <tr>
            <td>被担保人</td>
            <td>担保方式</td>
            <td>币种</td>
            <td>担保余额</td>
            <td>担保到期日</td>
            <td>担保合同编号</td>
            <td>备注</td>
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
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的担保外，本公司并无其他以贵行为担保受益人的担保。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>6(2). 贵行向本公司为提供的担保(如保函业务、备用信用证明业务等)</paragraph>
    <br/>
    <table x="20" border-color="LIGHT_GRAY">
        <tr>
            <td>被担保人</td>
            <td>担保方式</td>
            <td>币种</td>
            <td>担保余额</td>
            <td>担保到期日</td>
            <td>担保合同编号</td>
            <td>备注</td>
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
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的担保外，本公司并无贵行提供的其他担保。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>7. 本公司为出票人且由贵行承兑而尚未支付的银行承兑汇票</paragraph>
    <br/>
    <table x="20" border-color="PINK">
        <tr>
            <td>银行承兑汇票号码</td>
            <td>结算账户账号</td>
            <td>币种</td>
            <td>票面余额</td>
            <td>出票日</td>
            <td>到期日</td>
            <td>抵(质)押品</td>
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
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的银行承兑汇票外，本公司并无由贵行承兑而尚未支付的其他银行承兑汇票。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>8. 本公司向贵行已贴现而尚未到期的商业汇票</paragraph>
    <br/>
    <table x="20" border-color="MAGENTA">
        <tr>
            <td>商业汇票号码</td>
            <td>承兑人名称</td>
            <td>币种</td>
            <td>票面余额</td>
            <td>出票日</td>
            <td>到期日</td>
            <td>贴现日</td>
            <td>贴现率</td>
            <td>贴现净额</td>
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
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的商业汇票外，本公司并无向贵行已贴现而尚未到期的其他商业汇票。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>9. 本公司为持票人且由贵行托收的商业汇票</paragraph>
    <br/>
    <table x="20" border-color="CYAN">
        <tr>
            <td>商业汇票号码</td>
            <td>承兑人名称</td>
            <td>币种</td>
            <td>票面余额</td>
            <td>出票日</td>
            <td>到期日</td>
        </tr>
        <#list infoList as item>
            <tr>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的商业汇票外，本公司并无由贵行托收的其他商业汇票。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>10. 本公司为申请人、由贵行开具的、未履行完毕的不可撤销信用证</paragraph>
    <br/>
    <table x="20" border-color="DARKGOLD">
        <tr>
            <td>信用证号码</td>
            <td>受益人</td>
            <td>币种</td>
            <td>信用证金额</td>
            <td>到期日</td>
            <td>未使用金额</td>
        </tr>
        <#list infoList as item>
            <tr>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的不可撤销信用证外，本公司并无由贵行托开具的、未履行完毕的其他不可撤销信用证。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>11. 本公司与贵行之间未履行完毕的外汇买卖合约</paragraph>
    <br/>
    <table x="20" border-color="LIGHTGOLD">
        <tr>
            <td>类别</td>
            <td>合约号码</td>
            <td>贵行卖出币种</td>
            <td>贵行买入币种</td>
            <td>未履行的合约买卖金额</td>
            <td>汇率</td>
            <td>交收日期</td>
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
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的外汇买卖合约外，本公司并无与贵行之间未履行完毕的其他外汇买卖合约。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>12. 本公司存放于贵行托管的证券或其他产权文件</paragraph>
    <br/>
    <table x="20" border-color="191 239 255">
        <tr>
            <td>证券或其他产权文件名称</td>
            <td>证券代码或产权文件编号</td>
            <td>数量</td>
            <td>币种</td>
            <td>金额</td>
        </tr>
        <#list infoList as item>
            <tr>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的证券或其他产权文件外，本公司并无存放于贵行托管的其他证券或其他产权文件。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>13. 本公司购买的由贵行发行的未到期银行理财产品</paragraph>
    <br/>
    <table x="20" border-color="85 26 139">
        <tr>
            <td>产品名称</td>
            <td>产品类型(封闭式/开放式)</td>
            <td>币种</td>
            <td>持有份额</td>
            <td>产品净值</td>
            <td>购买日</td>
            <td>到期日</td>
            <td>是否被用于担保或存在其他使用限制</td>
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
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <paragraph>除上述列示的银行理财产品外，本公司并未购买其他由贵行发行的理财产品。</paragraph>
    <br/>
<!--    ****************************************************************************************************        -->
    <paragraph>14. 其他</paragraph>
    <br/>
    <table x="20" border-color="255 240 245">
        <#list infoList as item>
            <tr>
                <td>${item.data}</td>
            </tr>
        </#list>
    </table>
    <br/>
    <br/>
    <pr rectangle="B4" />
<!--    ****************************************************************************************************        -->
    <paragraph>附表. 资金归集(资金池或其他资金管理)账户具体信息</paragraph>
    <br/>
    <table x="20">
        <tr>
            <td>资金提供机构名称(即拨入资金的具体机构)</td>
            <td>资金提供机构账号</td>
            <td>资金使用机构名称(即向该具体机构拨出资金)</td>
            <td>资金使用机构账号</td>
            <td>币种</td>
            <td>截止函证基准日拨入或拨出资金余额(拨出填列正数、拨入填列负数)</td>
            <td>备注</td>
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
            </tr>
        </#list>
    </table>
    <br/>
</slot>