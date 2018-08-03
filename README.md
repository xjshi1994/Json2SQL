# Json2SQL

## feature
* flatten every data in the array or nested array into entries.

## Object
* convert json to mysql script for Data scientist to analyze the json data more easily on database. 

## sample
* json : ![](https://github.com/xjshi1994/picture_warehouse/blob/master/Json2SQL/orignal_json.png)
* result: ![](https://github.com/xjshi1994/picture_warehouse/blob/master/Json2SQL/Json2SQL_sample.png)

## TODO
* this cannot handle with the null array. 

## 注意事项
* data.log contains mannual things to do.
* schema type 为<stub> 需要手动修改
* data有可能超过varchar(1000),改成text
* column name过长， 手动删减
