# 项目：demo2_admin

## 1. 项目介绍

一个简单的 `Spring Boot + Spring Security + Vue` 前后端分离的 Demo 项目，实现了完整的后台权限管理+JWT登陆认证的功能，参考的教程链接：https://www.bilibili.com/video/BV1af4y1s7Wh

## 2. 技术选型

### 前端技术
| 框架    | 说明                      | 官网                       |
| ------- | ------------------------ | -------------------------- |
| Vue2    | 前端框架                  | https://cn.vuejs.org/      |
| Vue CLI | Vue 项目脚手架            | https://cli.vuejs.org/zh/  |
| Element | 前端 UI 框架              | https://element.eleme.cn   |
| Axios   | 前端HTTP框架              | http://www.axios-js.com/   |
| Mock    | 拦截请求模拟后端接口的框架 | http://mockjs.com/         |

### 后端技术

| 框架           | 说明             | 官网                                        |
| -------------- | --------------- | ------------------------------------------- |
| SpringBoot     | 后端框架         | https://spring.io/projects/spring-boot      |
| MyBatis        | 持久层框架       | https://mybatis.org/mybatis-3/zh/index.html |
| MyBatis-Plus   | MyBatis 增强框架 | https://mp.baomidou.com/                    |
| SpringSecurity | 权限框架         | https://spring.io/projects/spring-security  |

## 3. 项目部署

### 前端部署（端口：8080）

```shell
cd ./vue_admin    # step0: 进入目录

npm install       # step1: 加载模块

npm run serve     # step2: 运行项目

npm run build     # step3: 打包项目
```

### 后端部署（端口：8088）

```shell
cd ./springboot_admin     # step0: 进入目录

mvn compile               # step1: 编译项目

mvn exec:java -Dexec.mainClass="com.example.fruit.FruitApplication"    # step2: 直接使用 Maven 运行

mvn package               # step3: 打包项目

java -jar admin-0.0.1-SNAPSHOT.jar    # step4: 运行项目
```

注：编译和运行可以直接运行 `mvn spring-boot:run`

## 4. 注意事项

* 项目中超级管理员 `admin` 用户的密码为 `111111`，重置密码的设定为 `com.example.admin.common.lang.DEFAULT_PASSWORD`，代码中写为 `888888`，可自行修改

* 后端数据库为 `MySQL` 数据库，运行前先执行 `/sql` 目录下的 `SQL` 语句，并在 `application.yml` 文件中设置用户名和密码

* 后端使用了 `Redis` 来缓存数据，所以启动后端前需要启动 `Redis` 服务，如下是 `Windows` 版的下载链接：https://github.com/tporadowski/redis