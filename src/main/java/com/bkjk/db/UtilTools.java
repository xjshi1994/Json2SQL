package com.bkjk.db;

public class UtilTools {
    // remove []
    public static String removeBracket(String str) {
        String result = "";
        result = str.replaceAll("\\[.*?\\]", "");
        return result;
    }

    public static String removeLast(String str) {
        int lastIndex = str.lastIndexOf(".");

        if (lastIndex != -1) {
            return str.substring(0, lastIndex);
        }
        return str;
    }


    public static String rmOutBracket(String str) {
        return str.replaceAll("\\[|\\]", "");
    }

    public static String getPreForKey(String str) {
        if (str.equals(removeLast(str))) {
            return "base";
        }
        return removeBracket(removeLast(str));
    }

    public static String getLastField(String str) {
        int lastIndex = str.lastIndexOf(".");
        if (lastIndex != -1) {
            return str.substring(lastIndex + 1, str.length());
        }
        return str;
    }

    public static String getColumn(String entryKey, String thirdEntryKey) {
        if (entryKey.equals("base")) {
            // *****************revise here
            // it is array but there is no field in it, so take it as base!
            return rmOutBracket(thirdEntryKey);
        } else {
            return entryKey + "." + thirdEntryKey;
        }
    }
    public static String removeLastCommma(String str) {
        return str.substring(0, str.length() - 1);
    }
}
