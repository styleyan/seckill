# mysql相关语句

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
- IF NOT EXISTS(如果不存在)
- UNSIGNED: 无负数, 最小0开始
- NULL(字段值可以为空)
- NOT NULL(字段值禁止为空)
- AUTO_INCREMENT(自动编号, 会合 PRIMARY KEY:主键结合使用)
> CREATE TABLE IF NOT EXISTS [表名] (name CHAR, age TINYINT UNSIGNED, [列信息...], ...);
> CREATE TABLE IF NOT EXISTS [表名] (username VARCHAR(20) NOT NULL, age TINYINT UNSIGNED NULL);

4. 查看数据表是否存在
> SHOW TABLES; [当前数据库表]
> SHOW TABLES FROM [其他数据库名]; [其他数据库表]

5. 查看数据表结构
> SHOW COLUMNS FROM [表名];

6. 向表中添加数据
> INSERT [表名] VALUES ("列1", "列2", "列2", ...);
> INSERT [表名] (col_name,...) VALUES(VAL,...);

7. 表记录查找
> SELECT age FROM [表名];