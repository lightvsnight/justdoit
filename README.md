
本项目用于技术积累
架构和文档设计请参考 
https://codeup.aliyun.com/61d6df966112fe9819dab19d/builders/justdoit.git
master

***  不可以自己乱造， 请按规划来，有问题请联系 李先生，邮箱ziyou1102@aliyun.com

-- authserver

开发jDK为 17

目前暂时采用，
核心认证服务采用：spring-oauth2-authorization-server 2.0.0-M2

mybatis 3.5.19
mybatis-plus 3.5.15

--------新增分支0.0.1-------
master分支在重写认证服务端的OAuth2AuthorizationServerConfigurer类型，发现依赖的子类，大部分为私有类和方法，禁止访问或重写。
所以，需要切换依赖的版本，以寻找重写认证端的改造方法。


开源免责声明，请参考《免责声明.doc》


