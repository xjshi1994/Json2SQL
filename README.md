# Json2SQL
## Object
* convert json to mysql script for Data scientist to analyze the json data more easily on database. 

## sample
* json : ![](https://github.com/xjshi1994/picture_warehouse/blob/master/Json2SQL/orignal_json.png)
* result: ![](https://github.com/xjshi1994/picture_warehouse/blob/master/Json2SQL/Json2SQL_sample.png)

## TODO
* Because this program cannot handle with the null, or bad format json such as different entry number in the same level array. So the idea is to make a data clean module to format the json and create schema according to the whole jsons. Then sending formatted jsons to the json2sql module.

## 注意事项
* schema type 为<stub> 需要手动修改
* data有可能超过varchar(1000),改成text
* column name过长， 手动删减