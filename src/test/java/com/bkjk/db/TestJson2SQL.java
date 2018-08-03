package com.bkjk.db;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
@Slf4j
public class TestJson2SQL {
    @Test
    public void testCreate() {
        String json1 = "{\n" +
                "  \"kind\": \"youtube#searchListResponse\",\n" +
                "  \"etag\": \"\\\"m2yskBQFythfE4irbTIeOgYYfBU/PaiEDiVxOyCWelLPuuwa9LKz3Gk\\\"\",\n" +
                "  \"nextPageToken\": \"CAUQAA\",\n" +
                "  \"regionCode\": \"KE\",\n" +
                "  \"pageInfo\": {\n" +
                "    \"totalResults\": 4249,\n" +
                "    \"resultsPerPage\": 5\n" +
                "  },\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"kind\": \"youtube#searchResult\",\n" +
                "      \"etag\": \"\\\"m2yskBQFythfE4irbTIeOgYYfBU/QpOIr3QKlV5EUlzfFcVvDiJT0hw\\\"\",\n" +
                "      \"id\": {\n" +
                "        \"kind\": \"youtube#channel\",\n" +
                "        \"videoId\": \"UCJowOS1R0FnhipXVqEnYU1A\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"kind\": \"youtube#searchResult\",\n" +
                "      \"etag\": \"\\\"m2yskBQFythfE4irbTIeOgYYfBU/AWutzVOt_5p1iLVifyBdfoSTf9E\\\"\",\n" +
                "      \"id\": {\n" +
                "        \"kind\": \"youtube#video\",\n" +
                "        \"videoId\": \"Eqa2nAAhHN0\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"kind\": \"youtube#searchResult\",\n" +
                "      \"etag\": \"\\\"m2yskBQFythfE4irbTIeOgYYfBU/2dIR9BTfr7QphpBuY3hPU-h5u-4\\\"\",\n" +
                "      \"id\": {\n" +
                "        \"kind\": \"youtube#video\",\n" +
                "        \"videoId\": \"IirngItQuVs\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String tableName = "dataTest";
        Json2SQL json2SQL = new Json2SQL(json1);
        System.out.println(json2SQL.generateCreateTableSQL(tableName));
        System.out.println(json2SQL.generateInsertSQL(tableName));
    }
    @Test
    public void testFunc() {
        String firstKey = "attrs.author[0]";
        if (firstKey.endsWith("]")) {
            firstKey = firstKey + "." +UtilTools.removeBracket(UtilTools.getLastField(firstKey));
        }
        System.out.println(firstKey);
    }
}