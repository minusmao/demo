package com.example.fruit;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * 类名：CodeGenerator
 * 描述：MyBatis Plus 的代码生成类
 * 使用：填好配置信息，直接运行，即可生成代码
 * 时间：2021-6-12
 * 注意1：采用的 mybatis-plus-generator 的 3.4.1 版本，与 3.5.0 版本不同
 * 注意2：由于没有指定 mapper.xml 生成的位置，所以被自动生成在 com.example.mapper 包的 xml 文件夹下
 *       但是默认的 mapper.xml 扫描路径为 classpath*:/mapper/** /*.xml，需要自行移动，否则报错
 */
public class CodeGenerator {
    /* 程序入口 */
    public static void main(String[] args) {
        // 创建 AutoGenerator 对象
        AutoGenerator autoGenerator = new AutoGenerator();

        // 创建数据源，即连接数据库的相关信息
        DataSourceConfig dataSourceConfig = new DataSourceConfig();       // 数据源配置对象
        dataSourceConfig.setDbType(DbType.MYSQL);                         // 数据库类型
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");       // 驱动名称
        dataSourceConfig.setUsername("techrice");                         // 用户名称
        dataSourceConfig.setPassword("123456");                           // 用户密码
        dataSourceConfig.setUrl("jdbc:mysql://localhost/demo1_fruit");    // URL
        autoGenerator.setDataSource(dataSourceConfig);

        // 全局配置，即生成的代码的相关配置
        GlobalConfig globalConfig = new GlobalConfig();                                  // 全局配置对象
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");    // 生成的代码的位置
        globalConfig.setAuthor("TechRice");                                              // 生成的代码中注释中的用户名
        globalConfig.setOpen(false);                                                     // 生成完代码后是否打开该文件夹
        globalConfig.setServiceName("%sService");                                        // 生成的 Service 层的名称
        autoGenerator.setGlobalConfig(globalConfig);

        // 包信息配置，即生成的代码的包名配置
        PackageConfig packageConfig = new PackageConfig();    // 包配置对象
        packageConfig.setParent("com.example.fruit");         // 父包名
        packageConfig.setEntity("entity");                    // Entity 实体类的包名
        packageConfig.setMapper("mapper");                    // Mapper 的包名
        packageConfig.setService("service");                  // Service 的包名
        packageConfig.setServiceImpl("service.impl");         // ServiceImpl 的包名
        packageConfig.setController("controller");            // Controller 的包名
        autoGenerator.setPackageInfo(packageConfig);

        // 策略配置，即和数据库匹配的规则
        StrategyConfig strategyConfig = new StrategyConfig();     // 策略配置对象
        strategyConfig.setInclude("fruit");                       // 匹配的表名，可设置多个 setInclude("table_name1", "table_name2", ...)
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);    // 设置表名和实体类名的映射：下划线命名_to_驼峰命名
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);    // 设置表中字段和实体类属性的映射：下划线命名_to_驼峰命名
//        strategyConfig.setEntityLombokModel(true);    // 设置自动生成的代码支持 Lombok
        autoGenerator.setStrategy(strategyConfig);

        // 运行
        autoGenerator.execute();
    }
}
