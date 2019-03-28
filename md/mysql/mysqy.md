# mysql相关语句

#### 判断语句
- if not exists(如果不存在)


#### 数据库语句
1. 创建数据库
> CREATE DATABASE IF NOT EXISTS [数据名] CHARACTER SET [编码];

5. 修改数据库
> ALTER DATABASE [数据库名] CHARACTER SET [编码];

6. 删除数据库
> DROP DATABASE IF EXISTS [数据库名];

4. 查看数据库创建信息
> SHOW CREATE DATABASE [数据库名];

3. 查看警告信息
> SHOW WARNINGS ;

2. 显示有多少个数据库
> SHOW DATABASES ;


#### 表相关语句

1. 使用数据库
> USE [数据库名]

2. 当前打开的数据库
> select DATABASE();

3. 创建表
UNSIGNED: 无负数, 最小0开始
> CREATE TABLE IF NOT EXISTS [表名] (name CHAR, age TINYINT UNSIGNED, [列信息...], ...);