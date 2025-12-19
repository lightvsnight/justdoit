package com.sample.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * TODO Mybatis 插件配置
 *
 * @author Li.HongKun
 * @project authserver
 * @title mybatis插件，文件扫描与分页插件配置
 * @package com.example.config
 * @since 2023/8/22 23:22
 */

@Configuration
// 扫描 Mapper 接口所在包（替换为实际路径）
@MapperScan("com.sample.modules.**.mapper.**")
@EnableTransactionManagement //告诉它这是个事务
public class MyBatisConfig {


    /**
     * 配置 SqlSessionFactory，并注入数据源
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // 设置数据源
        sessionFactory.setDataSource(dataSource);

        // 可选：配置 MyBatis 全局属性（如驼峰命名映射、日志等）
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true); // 驼峰命名映射

        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class); // 日志输出到控制台
        sessionFactory.setConfiguration(configuration);

        // 可选：配置 Mapper XML 文件路径（如果使用 XML 方式写 SQL）
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(
                resolver.getResources("classpath:/mappers/**/*.xml") // 替换为实际 XML 路径
        );

        // 可选：配置类型别名（简化 XML 中的类全限定名）
        sessionFactory.setTypeAliasesPackage("com.sample.modules.**.entity.**"); // 实体类所在包

        // 可选：添加 MyBatis 插件（如分页插件）
        // sessionFactory.setPlugins(new Interceptor[]{new PageInterceptor()});



        return sessionFactory.getObject();
    }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setDbConfig(new com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig()
                .setIdType(IdType.NONE)); // 设置全局ID生成策略为NONE

        // 注册默认的雪花算法 ID 生成器（解决 identifierGenerator 为 null 的问题）
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());

        return globalConfig;
    }

    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator();
    }


    /**
     * 配置事务管理器（可选，如需事务支持）
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}