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

## Tips
* data.log contains manual things to do.
* schema type which <stub> need manual revise.
* length of data which longer than varchar(1000),needed to be changed to text
* column name may exceed the legit length of MySQL, so it should be manually decreased.
