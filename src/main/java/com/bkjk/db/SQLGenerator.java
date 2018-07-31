package com.bkjk.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SQLGenerator {
    private final int totalRow;

    public SQLGenerator(int rows) {
        totalRow = rows;
    }

    // inner sql in the create sql
    public String getInnerSQL(Map<String, List<Object>> result) {
        String innerCreateSQL = "";
        for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
            String innerType = UtilTools.getLastField(entry.getValue().get(0).getClass().toString());
            if (innerType.equals("String")) {
                innerType = "varchar(1000)";
            } else if (innerType.equals("BigDecimal")) {
                innerType = "float";
            } else {

            }
            // replace dot with underline because sql syntax
            innerCreateSQL = innerCreateSQL + entry.getKey().replace(".", "_") + " " + innerType + ",";
        }
        innerCreateSQL = innerCreateSQL.substring(0, innerCreateSQL.length() - 1);
        return innerCreateSQL;
    }

    public List<String> getInnerInsertSQL(Map<String, List<Object>> result, String tableName) {
        List<String> insertList = new LinkedList<>();

        String tableStructure = "";
        for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
            // replace dot with underline because sql syntax
            tableStructure = tableStructure + entry.getKey().replace(".", "_") + ",";
        }
        tableStructure = UtilTools.removeLastCommma(tableStructure);
        String insertPre = String.format("insert into %s (%s) value", tableName, tableStructure);

        for (int i = 0; i < totalRow; i++) {
            String temp = "";
            for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
                Object value = entry.getValue().get(i);
                if (UtilTools.getLastField(value.getClass().toString()).equals("String")) {
                    // surround the string with ''
                    temp = String.format(temp + " \'%s\',", value);
                } else {
                    temp = temp + value + ",";
                }
            }
            temp = UtilTools.removeLastCommma(temp);
            insertList.add(String.format(insertPre + "(%s);", temp));
        }
        return insertList;
    }

    public String getCreateSQL(Map<String, List<Object>> result, String tableName) {
        String innerSQL = getInnerSQL(result);
        // utf8 to solve code of chinese word
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
