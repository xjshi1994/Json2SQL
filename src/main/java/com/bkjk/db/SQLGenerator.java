package com.bkjk.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SQLGenerator {
    private final UtilTools utilTools;
    private final int totalRow;
    public SQLGenerator(int rows) {
        utilTools = new UtilTools();
        totalRow = rows;
    }

    public String getInnerSQL(Map<String, List<Object>> result) {
        String innerCreateSQL = "";
        for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
            String innerType = utilTools.getLastField(entry.getValue().get(0).getClass().toString());
            if (innerType.equals("String")) {
                innerType = "varchar(1000)";
            } else if (innerType.equals("BigDecimal")) {
                innerType = "float";
            } else {

            }
            innerCreateSQL = innerCreateSQL + entry.getKey().replace(".", "_") + " " + innerType + ",";
        }
        innerCreateSQL = innerCreateSQL.substring(0, innerCreateSQL.length() - 1);
        return innerCreateSQL;
    }

    public List<String> getInnerInsertSQL(Map<String, List<Object>> result, String tableName) {
        List<String> insertList = new LinkedList<>();

        String tableStructure = "";
        for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
            tableStructure = tableStructure + entry.getKey().replace(".", "_") + ",";
        }
        tableStructure = utilTools.removeLastCommma(tableStructure);
        String insertPre = String.format("insert into %s (%s) value", tableName, tableStructure);

        for (int i = 0; i < totalRow; i++) {
            String temp = "";
            for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
                Object value = entry.getValue().get(i);
                if (utilTools.getLastField(value.getClass().toString()).equals("String")) {
                    temp = String.format(temp + " \'%s\',", value);
                } else {
                    temp = temp + value + ",";
                }

            }
            temp = utilTools.removeLastCommma(temp);
            insertList.add(String.format(insertPre + "(%s);", temp));
        }
        return insertList;
    }

    public String getCreateSQL(Map<String, List<Object>> result, String tableName) {
        String innerSQL = getInnerSQL(result);
        String createSQL = "create table " + tableName + "(" + innerSQL + ")CHARSET=utf8;";
        return createSQL;
    }

    public String getFinalSQL(Map<String, List<Object>> result, String tableName) {
        String createSQL = getCreateSQL(result, tableName);
        List<String> insertList = getInnerInsertSQL(result, tableName);
        String insertSQL = "";
        for (String str : insertList) {
            insertSQL = insertSQL + str;
        }
        return createSQL + insertSQL;

    }
}
