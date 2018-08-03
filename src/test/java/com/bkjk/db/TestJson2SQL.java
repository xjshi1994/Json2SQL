package com.bkjk.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
@Slf4j
public class TestJson2SQL {
    @Test
    public static void main(String[] args) {
        String json1 = "{\n" +
                "\t\"asyncTaskId\": null,\n" +
                "\t\"cisReport\": [{\n" +
                "\t\t\"buildEndTime\": \"2018-06-24 19:07:53\",\n" +
                "\t\t\"creditBehaviorInfo\": {\n" +
                "\t\t\t\"avgCredits\": 0,\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"last12MthsLoanCnt\": 0,\n" +
                "\t\t\t\"last1MthsLoanCnt\": 0,\n" +
                "\t\t\t\"last3MthsLoanCnt\": 0,\n" +
                "\t\t\t\"last6MthsLoanCnt\": 0,\n" +
                "\t\t\t\"loanClosedCnt\": 0,\n" +
                "\t\t\t\"loanNoClosedCnt\": 0,\n" +
                "\t\t\t\"loanOrderCnt\": 0,\n" +
                "\t\t\t\"loanOrgCnt\": 0,\n" +
                "\t\t\t\"subReportType\": \"14238\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"1\",\n" +
                "\t\t\t\"undefinedCnt\": 0\n" +
                "\t\t},\n" +
                "\t\t\"econnoisserurInfo\": {\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"state\": \"0\",\n" +
                "\t\t\t\"subReportType\": \"14236\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"1\"\n" +
                "\t\t},\n" +
                "\t\t\"fraudRiskInfo\": {\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"state\": \"0\",\n" +
                "\t\t\t\"subReportType\": \"14237\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"1\"\n" +
                "\t\t},\n" +
                "\t\t\"hasSystemError\": false,\n" +
                "\t\t\"historySimpleQueryInfo\": {\n" +
                "\t\t\t\"count\": {\n" +
                "\t\t\t\t\"last12Month\": 1,\n" +
                "\t\t\t\t\"last18Month\": 1,\n" +
                "\t\t\t\t\"last1Month\": 0,\n" +
                "\t\t\t\t\"last24Month\": 3,\n" +
                "\t\t\t\t\"last3Month\": 1,\n" +
                "\t\t\t\t\"last6Month\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"items\": [{\n" +
                "\t\t\t\t\"last12Month\": 1,\n" +
                "\t\t\t\t\"last18Month\": 1,\n" +
                "\t\t\t\t\"last1Month\": 0,\n" +
                "\t\t\t\t\"last24Month\": 1,\n" +
                "\t\t\t\t\"last3Month\": 1,\n" +
                "\t\t\t\t\"last6Month\": 1,\n" +
                "\t\t\t\t\"unitMember\": \"商业银行\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"last12Month\": 0,\n" +
                "\t\t\t\t\"last18Month\": 0,\n" +
                "\t\t\t\t\"last1Month\": 0,\n" +
                "\t\t\t\t\"last24Month\": 2,\n" +
                "\t\t\t\t\"last3Month\": 0,\n" +
                "\t\t\t\t\"last6Month\": 0,\n" +
                "\t\t\t\t\"unitMember\": \"消费金融公司\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"subReportType\": \"19902\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"suspectedBulllending\": {\n" +
                "\t\t\t\t\"applyFinclCnt\": 0,\n" +
                "\t\t\t\t\"applyNetLoanCnt\": 0,\n" +
                "\t\t\t\t\"appplyCnt\": 0\n" +
                "\t\t\t},\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"1\"\n" +
                "\t\t},\n" +
                "\t\t\"isFrozen\": false,\n" +
                "\t\t\"overdueLoanInfo\": {\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"overdueDetails\": null,\n" +
                "\t\t\t\"overdueStat\": null,\n" +
                "\t\t\t\"subReportType\": \"14239\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"2\"\n" +
                "\t\t},\n" +
                "\t\t\"personAntiSpoofingDescInfo\": {\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"personAntiSpoofingDesc\": \"1、反欺诈风险评分为0分，风险等级为低，建议通过。\\n2、未命中羊毛党名单。\\n3、未命中欺诈风险名单。\\n4、未检测到信贷行为。\\n5、在近两年被机构查询过3次个人信息。\",\n" +
                "\t\t\t\"subReportType\": \"14242\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"1\"\n" +
                "\t\t},\n" +
                "\t\t\"personAntiSpoofingInfo\": {\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"hitTypes\": \"被机构查询信息\",\n" +
                "\t\t\t\"riskLevel\": \"低\",\n" +
                "\t\t\t\"riskScore\": 0,\n" +
                "\t\t\t\"subReportType\": \"14241\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"suggest\": \"建议通过\",\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"1\"\n" +
                "\t\t},\n" +
                "\t\t\"personRiskInfo\": {\n" +
                "\t\t\t\"errorMessage\": \"\",\n" +
                "\t\t\t\"stat\": null,\n" +
                "\t\t\t\"subReportType\": \"14227\",\n" +
                "\t\t\t\"subReportTypeCost\": \"96043\",\n" +
                "\t\t\t\"summary\": null,\n" +
                "\t\t\t\"treatErrorCode\": null,\n" +
                "\t\t\t\"treatResult\": \"2\"\n" +
                "\t\t},\n" +
                "\t\t\"queryConditions\": [{\n" +
                "\t\t\t\"caption\": \"被查询者姓名\",\n" +
                "\t\t\t\"name\": \"name\",\n" +
                "\t\t\t\"value\": \"杨巍\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"caption\": \"被查询者证件号码\",\n" +
                "\t\t\t\"name\": \"documentNo\",\n" +
                "\t\t\t\"value\": \"130826198410031233\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"caption\": \"手机号码\",\n" +
                "\t\t\t\"name\": \"phone\",\n" +
                "\t\t\t\"value\": \"13122256419\"\n" +
                "\t\t}],\n" +
                "\t\t\"queryReasonID\": \"1\",\n" +
                "\t\t\"reportID\": \"2018062419500071\",\n" +
                "\t\t\"subReportTypes\": \"96043\",\n" +
                "\t\t\"subReportTypesShortCaption\": \"1、个人反欺诈分析报告（96043）\",\n" +
                "\t\t\"treatResult\": 1\n" +
                "\t}],\n" +
                "\t\"message\": \"查询成功\",\n" +
                "\t\"status\": \"OK\"\n" +
                "}";
        Json2SQL json2SQL = new Json2SQL(json1);
         // variable table name
        //log.info(json2SQL.generateCreateTableSQL("tongdun"));
        //log.info(json2SQL.generateInsertSQL("tongdun"));
        log.info(json2SQL.generateCreateTableSQL("tongdun"));
        log.info(json2SQL.generateInsertSQL("tongdun"));
        System.out.println(json2SQL.generateCreateTableSQL("tongdun"));
        System.out.println(json2SQL.generateInsertSQL("tongdun"));
    }
}