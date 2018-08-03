package com.bkjk.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class SQLGenerator {
    private final int totalRow;
    private final Map<String, List<Object>> finalDataframe;


    // constructor: finalDataframe, rows

    public SQLGenerator(Map<String, List<Object>> finalDataframe, int rows) {

        totalRow = rows;
        this.finalDataframe = finalDataframe;
    }

    // inner sql in the create sql
    private String getInnerSQL() {
        StringBuilder innerCreateSQL = new StringBuilder();
        int numToBeRevised = 0;
        List<String> columnToBeRevised = new LinkedList<>();
        for (Map.Entry<String, List<Object>> entry : finalDataframe.entrySet()) {

            // Use the first entry to detect the type
            // when the first entry is null, mark it as <stub>
            Object firstEntry = entry.getValue().get(0);
            String innerType;

            if (firstEntry != null) {
                innerType = UtilTools.getLastField(firstEntry.getClass().toString());
                switch (innerType) {
                    case "String":
                        innerType = "varchar(1000)";
                        break;
                    case "BigDecimal":
                        innerType = "float";
                        break;
                    case "Boolean":
                        innerType = "boolean";
                        break;
                    default:
                        throw new RuntimeException("json format is not correct, no match type in json");
                }
            } else {
                numToBeRevised++;
                columnToBeRevised.add(entry.getKey());
                innerType = "<stub>";
            }
            // replace dot with underline because sql syntax
            innerCreateSQL.append(entry.getKey().replace(".", "_")).append(" ").append(innerType).append(",");
        }
        // remind user of column to be revised
        log.info("num to be revised: " + numToBeRevised);
        log.info("column name following: " + columnToBeRevised);
        innerCreateSQL = new StringBuilder(innerCreateSQL.substring(0, innerCreateSQL.length() - 1));
        return innerCreateSQL.toString();
    }

    //
    private List<String> getInnerInsertSQL(String tableName) {
        List<String> insertList = new LinkedList<>();

        StringBuilder tableStructure = new StringBuilder();
        for (Map.Entry<String, List<Object>> entry : finalDataframe.entrySet()) {
            // replace dot with underline because sql syntax
            tableStructure.append(entry.getKey().replace(".", "_")).append(",");
        }
        tableStructure = new StringBuilder(UtilTools.removeLastCommma(tableStructure.toString()));
        String insertPre = String.format("insert into %s (%s) value", tableName, tableStructure.toString());

        // detect null array
        for (List<Object> l : finalDataframe.values()) {
            if (l.size() < totalRow) {
                System.out.println(l);
                System.out.println(totalRow);
                throw new RuntimeException("null array or array with different field name");
            }
        }

        for (int i = 0; i < totalRow; i++) {
            StringBuilder temp = new StringBuilder();
            for (Map.Entry<String, List<Object>> entry : finalDataframe.entrySet()) {
                Object value = entry.getValue().get(i);

                // if it is null value, no quotation around it

                if (value != null) {
                    if (UtilTools.getLastField(value.getClass().toString()).equals("String")) {
                        // surround the string with ''
                        temp = new StringBuilder(String.format(temp + " \'%s\',", value));
                    } else {
                        temp.append(value).append(",");
                    }
                } else temp.append(value).append(",");
            }
            temp = new StringBuilder(UtilTools.removeLastCommma(temp.toString()));
            insertList.add(String.format(insertPre + "(%s);", temp.toString()));
        }
        return insertList;
    }

    public String getCreateSQL(String tableName) {
        String innerSQL = getInnerSQL();
        // utf8 to solve code of chinese word
        return "create table " + tableName + "(" + innerSQL + ")CHARSET=utf8;";
    }

    public String getFinalInsert(String tableName) {
        List<String> insertList = getInnerInsertSQL(tableName);
        String insertSQL = "";
        for (String str : insertList) insertSQL += str;
        return insertSQL;
    }

    //
    public String getFinalSQL(String tableName, boolean isFirst) {
        if (isFirst) {
            String createSQL = getCreateSQL(tableName);
            List<String> insertList = getInnerInsertSQL(tableName);
            StringBuilder insertSQL = new StringBuilder();
            for (String str : insertList) {
                insertSQL.append(str);
            }
            return createSQL + " " + insertSQL;
        } else {
            return getFinalInsert(tableName);
        }
    }
}
