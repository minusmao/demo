# 项目：demo1_fruit

## 1. 项目介绍

一个简单的 `Spring Boot + Vue` 前后端分离的 Demo 项目，实现了基本的 CRUD 和图表展示功能，参考的教程链接：https://www.bilibili.com/video/BV1i5411A7gu

## 2. 技术选型

### 前端技术
| 框架    | 说明           | 官网                       |
| ------- | ------------- | -------------------------- |
| Vue2    | 前端框架       | https://cn.vuejs.org/      |
| Vue CLI | Vue 项目脚手架 | https://cli.vuejs.org/zh/  |
| Element | 前端 UI 框架   | https://element.eleme.cn   |
| ECharts | 可视化图表库   | https://echarts.apache.org |
| Axios   | 前端HTTP框架   | http://www.axios-js.com/   |

### 后端技术

| 框架         | 说明             | 官网                                        |
| ------------ | --------------- | ------------------------------------------- |
| SpringBoot   | 后端框架         | https://spring.io/projects/spring-boot      |
| MyBatis      | 持久层框架       | https://mybatis.org/mybatis-3/zh/index.html |
| MyBatis-Plus | MyBatis 增强框架 | https://mp.baomidou.com/                    |

## 3. 项目部署

### 前端部署（端口：8080）

```shell
cd ./vue_fruit    # step0: 进入目录

npm install       # step1: 加载模块

npm run serve     # step2: 运行项目

npm run build     # step3: 打包项目
```

### 后端部署（端口：8088）

```shell
cd ./springboot_fruit     # step0: 进入目录

mvn compile               # step1: 编译项目

mvn exec:java -Dexec.mainClass="com.example.fruit.FruitApplication"    # step2: 直接使用 Maven 运行

mvn package               # step3: 打包项目

java -jar fruit-0.0.1-SNAPSHOT.jar    # step4: 运行项目
```

注：编译和运行可以直接运行 `mvn springboot:run`

## 4. 注意事项

* 由于此项目为一个小的 demo 项目，所以前端对后端的请求没有进行统一 api 封装，所以每个 view 视图下面的请求地址为 `http://localhost:8088/*/**` 需要自行更改

* 后端数据库为 `MySQL` 数据库，运行前先执行 `/sql` 目录下的 `SQL` 语句，并在 `application.yml` 文件中设置用户名和密码
