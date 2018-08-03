package com.bkjk.db;

import com.github.wnameless.json.flattener.JsonFlattener;
import javafx.beans.binding.ObjectExpression;

import java.util.*;


// https://sqlify.io/convert
// https://www.producthunt.com/posts/sqlify
public class Json2SQL {
  private int totalRow = 0;
  private final String json;
  private Map<String, Map<String, Map<String, Object>>> group;
  private SQLGenerator sqlGenerator;
  private boolean isInit = false;
  public Json2SQL(String json) {
    this.json = json;
  }

  private void sqlInit() {
    if (json == null) {
      throw new RuntimeException("json is null!");
    } else {
      Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);
      getThreeMap(flattenJson);
      if (group != null) {
        Map<String, List<Object>> dataframe = insertData();
        sqlGenerator = new SQLGenerator(dataframe, totalRow);
      } else {
        throw new RuntimeException("group is null!");
      }
    }
  }

  public String generateCreateTableSQL(String tableName) {
    if (!isInit) {
      sqlInit();
      isInit = true;
    }
    return sqlGenerator.getCreateSQL(tableName);
  }

  public String generateInsertSQL(String tableName) {
    if (!isInit) {
      sqlInit();
      isInit = true;
    }
    return sqlGenerator.getFinalInsert(tableName);
  }

  // step 1
  // first level: column name without last field
  // second level: column name with bracket
  // third level: field and value
  private void getThreeMap(Map<String, Object> flattenJson) {
    Map<String, Map<String, Map<String, Object>>> grouped = new HashMap<>();

    for (Map.Entry<String, Object> entry : flattenJson.entrySet()) {
      String key = entry.getKey();
      if (key.endsWith("]")) {
        key = key + "." +UtilTools.removeBracket(UtilTools.getLastField(key));
      }

      String firstKey = UtilTools.getPreForKey(key);

      grouped.putIfAbsent(firstKey, new HashMap<>());

      // second level map
      Map<String, Map<String, Object>> secondMap = grouped.get(firstKey);
      String secondKey = UtilTools.removeLast(key);
      secondMap.putIfAbsent(secondKey, new HashMap<>());

      // third level map
      String thirdKey = UtilTools.getLastField(key);
      Map<String, Object> thirdMap = secondMap.get(secondKey);
      thirdMap.put(thirdKey, entry.getValue());

    }
    // assign the value
    this.group = grouped;
  }

  // calculate the leaf node;
  // For every of them, grow it to get subResult(one entry in sql)
  // then get final map
  private Map<String, List<Object>> insertData() {
    List<Map<String, List<Object>>> subResult = new LinkedList<>();

    List<String> deepest = getLeafNodes();

    // for every leaf node,  chain the all its parent, then add to the subResult;
    // for every
    for (String str : deepest) {
      Map<String, List<Object>> subMap = getSubMap(str);

      if (subMap != null) {
        subResult.add(subMap);
      }

    }
    return getFinalMap(subResult);
  }

  // Fetch all the leaf nodes(column name without the field)
  private List<String> getLeafNodes() {
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
    this.totalRow = getNewTotalRows(deepest);
    return deepest;
  }

  // for leaf node, they are constructed into one entry. key is the column name of leaf node
  // 为每一个独立的tree构建它的所有entry存入submap
  private Map<String, List<Object>> getSubMap(String leafNode) {
    Map<String, List<Object>> subMap = new HashMap<>();

    // base
    if (leafNode.equals("base")) {
      return null;
    }

    Map<String, Map<String, Object>> secondMap = group.get(leafNode);
    for (Map.Entry<String, Map<String, Object>> secondEntry : secondMap.entrySet()) {
      // curKey with bracket
      String curKey = secondEntry.getKey();

      while (curKey != null) {
        Map<String, Object> thirdMap = getThirdMap(curKey);

        // assume third map not null

        if (thirdMap != null) {
          for (Map.Entry<String, Object> thirdEntry : thirdMap.entrySet()) {
            String column = UtilTools.getColumn(UtilTools.removeBracket(curKey), thirdEntry.getKey());
            subMap.putIfAbsent(column, new LinkedList<>());
            subMap.get(column).add(thirdEntry.getValue());
          }
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

  // 根据每一个独立的tree的所有entry,做Cartesian product
  private Map<String, List<Object>> getFinalMap(List<Map<String, List<Object>>> subResult) {
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
    int wholeLevel;
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

  private void insertFinalMap(Map<String, List<Object>> finalMap, Map<String, List<Object>> map, int wholeLevel, int partLevel) {
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


  private Map<String, Object> getThirdMap(String secondKey) {
    String firstKey = UtilTools.removeBracket(secondKey);
    Map<String, Map<String, Object>> secondMap = group.get(firstKey);
    if (secondMap==null) {
      return null;
    }
    return group.get(firstKey).get(secondKey);
  }

  // Idea: totalRow = multiply of num of leaf node in each separate cluster(except base level, because it counts as 1)
  private int getNewTotalRows(List<String> l) {
    int num = 1;
    for (String str : l) {
      if (!str.equals("base")) {
        num = num * group.get(str).size();
      }
    }
    return num;
  }

  private void tranverse(Map<String, List<Object>> result) {
    for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
      System.out.print(entry.getKey() + " ");
      System.out.println(entry.getValue().get(0).getClass());
    }
    System.out.println();

    for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
      if (entry == null) continue;
      int k = 0;
      for (int i = 0; i < entry.getValue().size(); i++) {
        System.out.print(entry.getValue().get(i) + " ");
        k = i;
      }
      System.out.println("********" + k);
    }
  }

  }