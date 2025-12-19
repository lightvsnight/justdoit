package org.sample.generator;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @describe: TODO
 * @author: L.hk
 * @date: 2023/8/22 17:36
 */
//执行 main 方法，控制台输入模块表名，回车自动生成对应项目目录中
public class MybatisPlusCodeGenerator {

    public static void main(String[] args) {

        System.out.println("==============输出路径/绝对路径==================");
        System.out.println(System.getProperty("user.dir"));
        String outputDir = "F:\\IdeaProjects\\justdoit\\authserver\\common\\";
        System.out.println(outputDir);
        System.out.println("======================================");

        //模块名称
        String moduleName = "tAuthorizationConsent";

        //test为自定义的文件生成路径，根据业务进行变更
        //父包名，test为最终生成的包位置，替换成自己的即可
        String parentPackage = "com.sample.modules";
        //mapper.xml生成位置，test变更为自己的即可
        String resources = "/src/main/resources/";
        String mapperXmlPath = "/mappers/"+moduleName;
        //作者名字
        String author = "Li.HongKun";

        //数据库连接信息
        String url = "jdbc:mysql://localhost:3306/springauthserver?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "root";

        //表名集合
        List<String> tables = new ArrayList<>();
        tables.add("t_authorization_consent");


        FastAutoGenerator.create(url, username, password)
                //全局配置
                .globalConfig(builder -> {
                    builder.disableOpenDir()  //禁止打开输出目录
                            .outputDir(outputDir + "/src/main/java")   //指定输出目录
                            .author(author)   //作者名
//                            .enableSwagger()     //开启 swagger 模式
                            .dateType(DateType.ONLY_DATE)   //时间策略
//                          .fileOverride() //全局覆盖已有文件的配置已失效，已迁移到策略配置中
                            .commentDate("yyyy-MM-dd HH:mm:ss");   //注释日期
                })
                //包配置
                .packageConfig(builder -> {
                    builder.parent(parentPackage)     //父包名
                            .moduleName(moduleName)           // 模块名称
                            .entity("entity")               //Entity 包名
                            .service("service")             //Service 包名
                            .serviceImpl("service.impl")    //Service Impl 包名
                            .mapper("mapper")               //Mapper 包名
                            .xml("mapper.xml")              //Mapper XML 包名
                            .controller("controller")       //Controller 包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outputDir + resources + mapperXmlPath));//指定xml位置
                })
                //策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
//                            .enableCapitalMode() // 开启大写命名
//                            .enableSkipView() // 开启跳过视图
//                            .disableSqlFilter() // 禁用 SQL 过滤
//                            .likeTable(new LikeTable("USER")) // 模糊匹配表名
//                            .addTablePrefix("app_")//表名前缀，配置后生成的代码不会有此前缀，建议表名的驼峰格式命名，方便寻找对应的文件资料
                            .serviceBuilder()
                            .formatServiceFileName("%sService")//服务层接口名后缀
                            .formatServiceImplFileName("%sServiceImpl")//服务层实现类名后缀

                            .entityBuilder()
//                            .enableLombok()   //实体类使用lombok,需要自己引入依赖
                            .logicDeleteColumnName("del_flag")//逻辑删除字段，使用delete方法删除数据时会将status设置为1。调用update方法时并不会将该字段放入修改字段中，而是在条件字段中
                            .enableTableFieldAnnotation() //加上字段注解@TableField
                            .controllerBuilder()
                            .formatFileName("%sController")//控制类名称后缀
                            .enableRestStyle()

                            .mapperBuilder()
                            .superClass(BaseMapper.class)
                            .formatMapperFileName("%sMapper")
//                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatXmlFileName("%sMapper");
                })
//              .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
