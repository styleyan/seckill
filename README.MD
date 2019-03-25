# 相关文档

- [spring docs](https://docs.spring.io/spring/docs/4.1.7.RELEASE/spring-framework-reference/)
- [logback](http://logback.qos.cn/manual/configuration.html)


### 遇到的问题


##### 数据库连接失败，提示 "com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: Could not create connection to"
解决方法: 使用Navicat for mysql 连接没有问题，但是一用项目连接就出现上面错误, 造成这个错误的主要原因是安装的是mysql8.0版本，而项目使用的驱动包是5.1版本，将项目驱动版本改成8.0.13成功解决了此问题。


##### 数据库连接失败, 提示 "Public Key Retrieval is not allowed"，相关版本
````
MySQL version: 8.0.13 
jdbc:mysql://xxx.xx.xx.xxx:3306/seckill?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf8
MySQL 驱动: 8.0.13
c3p0: 0.9.1.2
````
解决方法: 给 <context:property-placeholder> 添加 system-properties-mode="FALLBACK" 属性即可。


##### 数据库连接失败, 提示 "nested exception is java.sql.SQLException: An attempt by a client to checkout a Connection has timed out."
解决方法: 配置c3p0的时候，有一个配置属性是checkoutTimeout，把这个配置属性去掉就正常了


### 扩展知识

- SOA
- 微服务
- 服务网格

### 其他知识

- Dubbo
- Mybatis
- kafka
- ZooKeeper
- redis

