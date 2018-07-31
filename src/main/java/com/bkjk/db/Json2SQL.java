package com.bkjk.db;

import com.github.wnameless.json.flattener.JsonFlattener;

import java.util.*;

public class Json2SQL {
    private int totalRow = 0;
    private final String json;


    public Json2SQL(String json) {
        this.json = json;
    }

    public void getSQL(String tableName) {
        Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);
        Map<String, Map<String, Map<String, Object>>> grouped = getThreeMap(flattenJson);
        Map<String, List<Object>> dataframe = insertData(grouped);
        SQLGenerator sqlGenerator = new SQLGenerator(totalRow);
        System.out.println(sqlGenerator.getFinalSQL(dataframe, tableName));
    }

    // step 1
    public Map<String, Map<String, Map<String, Object>>> getThreeMap(Map<String, Object> flattenJson) {
        Map<String, Map<String, Map<String, Object>>> grouped = new HashMap<>();

        for (Map.Entry<String, Object> entry : flattenJson.entrySet()) {
            String firstKey = null;

            firstKey = UtilTools.getPreForKey(entry.getKey());

            grouped.putIfAbsent(firstKey, new HashMap<>());

            // second level map
            Map<String, Map<String, Object>> secondMap = grouped.get(firstKey);
            String secondKey = UtilTools.removeLast(entry.getKey());
            secondMap.putIfAbsent(secondKey, new HashMap<>());

            // third level map
            String thirdKey = UtilTools.getLastField(entry.getKey());
            Map<String, Object> thirdMap = secondMap.get(secondKey);
            thirdMap.put(thirdKey, entry.getValue());
        }

        // after get three map, calculate totalRows.
        getTotalRows(grouped);
        return grouped;
    }

    public void getTotalRows(Map<String, Map<String, Map<String, Object>>> group) {
        List<String> list = new ArrayList<>();
        // add to list except base.
        for (Map.Entry<String, Map<String, Map<String, Object>>> entry : group.entrySet()) {
            if (!entry.getKey().equals("base")) {
                list.add(entry.getKey());
            }
        }

        // sort list
        Collections.sort(list);

        int num = 0;
        String prev = "";
        String cur = "";
        List<Integer> numList = new LinkedList<>();

        // start from last
        for (int i = list.size() - 1; i >= 0; i--) {
            // assume the first is the last level
            cur = list.get(i);

            // skip the entry with the same root
            if (UtilTools.removeLast(prev).equals(cur)) {
                prev = cur;
                continue;
            } else {
                // when meet with the different root, add the size of the to the numlist
                num = group.get(cur).size();
                numList.add(num);
            }
            prev = cur;
        }

        int result = 1;
        for (int i = 0; i < numList.size(); i++) {
            result = result * numList.get(i);
        }

        totalRow = result;
    }

    // calculate the deepest, group them to get subResult, then get final map
    public Map<String, List<Object>> insertData(Map<String, Map<String, Map<String, Object>>> group) {
        List<Map<String, List<Object>>> subResult = new LinkedList<>();

        List<String> deepest = getDeepest(group);

        // for every deepest key,  chain the all its parent, then add to the subResult;
        for (String str : deepest) {
            if (getSubMap(str, group) != null) {
                subResult.add(getSubMap(str, group));
            }
        }
        Map<String, List<Object>> result = getFinalMap(subResult, group);
        return result;
    }

    public Map<String, List<Object>> getFinalMap(List<Map<String, List<Object>>> subResult, Map<String, Map<String, Map<String, Object>>> group) {
        Map<String, List<Object>> finalMap = new HashMap<>();

        // insert base first
        if (group.get("base") != null) {
            Map<String, Map<String, Object>> secondMap = group.get("base");
            for (Map.Entry<String, Map<String, Object>> secondEntry : secondMap.entrySet()) {
                Map<String, Object> thirdMap = secondEntry.getValue();
                for (Map.Entry<String, Object> thirdEntry : thirdMap.entrySet()) {
                    String column = UtilTools.getColumn("base", thirdEntry.getKey());
                    List<Object> sublist = new ArrayList<>();
                    for (int i = 0; i < totalRow; i++) {
                        sublist.add(thirdEntry.getValue());
                    }
                    finalMap.put(column, sublist);
                }
            }
        }
        int partLevel = 1;
        int wholeLevel = 0;
        for (Map<String, List<Object>> map : subResult) {
            // revise
            int initSize = 0;
            for (List<Object> l : map.values()) {
                initSize = l.size();
            }
            wholeLevel = totalRow / (partLevel * initSize);
            insertFinalMap(finalMap, map, wholeLevel, partLevel);
            partLevel = partLevel * initSize;
        }
        return finalMap;
    }

    public void insertFinalMap(Map<String, List<Object>> finalMap, Map<String, List<Object>> map, int wholeLevel, int partLevel) {
        for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
            String smallMapKey = entry.getKey();
            List<Object> smallMapList = entry.getValue();

            finalMap.putIfAbsent(smallMapKey, new LinkedList<>());

            // partLevel finished
            for (Object o : smallMapList) {
                for (int i = 0; i < partLevel; i++) {
                    finalMap.get(smallMapKey).add(o);
                }
            }

            List<Object> dup = finalMap.get(smallMapKey);
            for (int j = 0; j < wholeLevel - 1; j++) {
                finalMap.get(smallMapKey).addAll(dup);
            }
        }
    }


    public Map<String, List<Object>> getSubMap(String key1, Map<String, Map<String, Map<String, Object>>> group) {
        Map<String, List<Object>> subMap = new HashMap<>();
        // base
        if (key1.equals("base")) {
            return null;
        }

        Map<String, Map<String, Object>> secondMap = group.get(key1);
        for (Map.Entry<String, Map<String, Object>> secondEntry : secondMap.entrySet()) {
            String curKey = secondEntry.getKey();
            while (curKey != null) {
                Map<String, Object> thirdMap = getThirdMap(curKey, group);
                if (thirdMap == null) {
                    System.out.println("third map is null");
                    return null;
                }

                for (Map.Entry<String, Object> thirdEntry : thirdMap.entrySet()) {
                    String column = UtilTools.getColumn(UtilTools.removeBracket(curKey), thirdEntry.getKey());
                    subMap.putIfAbsent(column, new LinkedList<>());
                    subMap.get(column).add(thirdEntry.getValue());
                }

                // get all its parent from bottom
                if (curKey.contains(".")) {
                    curKey = UtilTools.removeLast(curKey);
                } else {
                    curKey = null;
                }
            }
        }
        return subMap;
    }

    public Map<String, Object> getThirdMap(String secondKey, Map<String, Map<String, Map<String, Object>>> group) {
        String firstKey = UtilTools.removeBracket(secondKey);
        return group.get(firstKey).get(secondKey);
    }

    public List<String> getDeepest(Map<String, Map<String, Map<String, Object>>> group) {
        List<String> deepest = new LinkedList<>();
        List<String> list = new ArrayList<>();

        for (Map.Entry<String, Map<String, Map<String, Object>>> entry : group.entrySet()) {
            list.add(entry.getKey());
        }

        Collections.sort(list);

        String pre = "";

        for (int i = list.size() - 1; i >= 0; i--) {
            String cur = list.get(i);
            if (!UtilTools.removeLast(pre).equals(cur)) {
                deepest.add(cur);
            }
            pre = cur;
        }
        return deepest;
    }

    public void tranverse(Map<String, List<Object>> result) {
        for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
            System.out.print(entry.getKey() + " ");
            System.out.println(entry.getValue().get(0).getClass());
        }
        System.out.println();

        for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
            int k = 0;
            for (int i = 0; i < entry.getValue().size(); i++) {
                System.out.print(entry.getValue().get(i) + " ");
                k = i;
            }
            System.out.println("********" + k);
        }
    }

}