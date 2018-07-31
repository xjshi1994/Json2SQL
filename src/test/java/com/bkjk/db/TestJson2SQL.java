package com.bkjk.db;
public class TestJson2SQL {

    public static void main(String[] args) {
            String json1 = "{\n" +
                    "    \"error\": 0,\n" +
                    "    \"status\": \"success\",\n" +
                    "    \"results\": [\n" +
                    "        {\n" +
                    "            \"currentCity\": \"青岛\",\n" +
                    "            \"index\": [\n" +
                    "                {\n" +
                    "                    \"title\": \"穿衣\",\n" +
                    "                    \"zs\": \"较冷\",\n" +
                    "                    \"tipt\": \"穿衣指数\",\n" +
                    "                    \"des\": \"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。\"\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"title\": \"紫外线强度\",\n" +
                    "                    \"zs\": \"中等\",\n" +
                    "                    \"tipt\": \"紫外线强度指数\",\n" +
                    "                    \"des\": \"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。\"\n" +
                    "                }\n" +
                    "            ]\n" +
                    "\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            Json2SQL json2SQL = new Json2SQL(json1);
            // variable table name
             json2SQL.getSQL("xixi");

    }
}