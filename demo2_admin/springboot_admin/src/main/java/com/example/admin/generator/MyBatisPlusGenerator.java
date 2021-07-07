package com.example.admin.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 文件名：MyBatisPlusGenerator.java
 * 描述：MyBatisPlus 的代码生成器，根据数据库快速生成 entity、mapper、service、controller 层
 * 时间：2021-6-27
 * 作者：TechRice
 * 注意：1、使用前先创建两个公共类（包名.entity.BaseEntity、包名.entity.BaseController）
 *      2、使用时，在控制台输入表名即可，多个表用英文逗号隔开
 * 官方示例：https://mp.baomidou.com/guide/generator.html
 */
public class MyBatisPlusGenerator {
    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();    // note: 读取控制台
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
    /* 程序入口 */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");    // 获得当前项目的路径
        gc.setOutputDir(projectPath + "/src/main/java");        // 生成的代码的位置
        gc.setAuthor("TechRice");                               // 生成的代码中注释中的用户名
        gc.setOpen(false);                                      // 生成完代码后是否打开该文件夹
        gc.setServiceName("%sService");                         // 生成的 Service 层的名称
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/demo2_admin");    // 访问数据库的 URL
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");            // 驱动名称
        dsc.setUsername("techrice");                              // 用户名称
        dsc.setPassword("123456");                                // 用户密码
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName(scanner("模块名"));    // 模块名（控制台输入，项目没有分模块开发，所以不设置）
        pc.setParent("com.example.admin");       // 生成的代码的包名
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        String templatePath = "/templates/mapper.xml.ftl";    // 模板引擎是 freemarker
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名，如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);          // 设置表名和实体类名的映射：下划线命名_to_驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);    // 设置表中字段和实体类属性的映射：下划线命名_to_驼峰命名
        strategy.setSuperEntityClass("com.example.admin.entity.BaseEntity");                // 设置 entity 层的公共父类
//        strategy.setEntityLombokModel(true);                          // 设置自动生成的代码支持 Lombok
        strategy.setRestControllerStyle(true);                          // 设置 controller 层为 Restful 风格
        strategy.setSuperControllerClass("com.example.admin.controller.BaseController");    // 设置 controller 层的公共父类
        strategy.setSuperEntityColumns("id", "created", "updated", "statu");                // 写于公共父类中的公共字段
        strategy.setInclude(scanner("表名，多个请用英文逗号分割").split(","));         // 匹配的表名，控制台输入
        strategy.setControllerMappingHyphenStyle(true);
//        strategy.setTablePrefix(pc.getModuleName() + "_");                                // 忽略表的前缀（一般为模块名），暂不设置
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 运行
        mpg.execute();
    }
}
