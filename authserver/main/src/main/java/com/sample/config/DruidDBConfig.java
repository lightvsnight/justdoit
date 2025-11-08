//package com.sample.config;
//
//import com.alibaba.druid.filter.stat.StatFilter;
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.wall.WallConfig;
//import com.alibaba.druid.wall.WallFilter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.*;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DruidDBConfig {
//
//    Logger log = LoggerFactory.getLogger(DruidDBConfig.class);
//
//
//    @Value("${spring.datasource.url}")
//    private String dbUrl;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//    @Value("${spring.datasource.driverClassName}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.druid.initialSize}")
//    private int initialSize;
////
////    @Value("${spring.datasource.druid.minIdle}")
////    private int minIdle;
////
////    @Value("${spring.datasource.druid.maxActive}")
////    private int maxActive;
////
////    @Value("${spring.datasource.druid.maxWait}")
////    private int maxWait;
////
////    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
////    private int timeBetweenEvictionRunsMillis;
////
////    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
////    private int minEvictableIdleTimeMillis;
////
////    @Value("${spring.datasource.druid.validationQuery}")
////    private String validationQuery;
////
////    @Value("${spring.datasource.druid.testWhileIdle}")
////    private boolean testWhileIdle;
////
////    @Value("${spring.datasource.druid.testOnBorrow}")
////    private boolean testOnBorrow;
////
////    @Value("${spring.datasource.druid.testOnReturn}")
////    private boolean testOnReturn;
////
////    @Value("${spring.datasource.druid.poolPreparedStatements}")
////    private boolean poolPreparedStatements;
////
////    @Value("${spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
////    private int maxPoolPreparedStatementPerConnectionSize;
////
////    @Value("${spring.datasource.druid.filters}")
////    private String filters;
////
////    @Value("{spring.datasource.druid.connectionProperties}")
////    private String connectionProperties;
//
//
//    /**
//     * stat-view-servlet
//     */
//    @Value("${spring.datasource.druid.stat-view-servlet.login-username}")
//    private String loginUserName;
//    @Value("${spring.datasource.druid.stat-view-servlet.login-password}")
//    private String loginPassword;
//
//    @Value("${spring.datasource.druid.stat-view-servlet.enabled}")
//    private String statViewDervletEnabled;
//
//    @Value("${spring.datasource.druid.stat-view-servlet.allow}")
//    private String statViewDervletAllow;
//    @Value("${spring.datasource.druid.stat-view-servlet.deny}")
//    private String statViewDervletDeny;
//
//
//
//    @Bean // 声明其为Bean实例
//    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
//    public DataSource dataSource() {
//        DruidDataSource datasource = new DruidDataSource();
//
//        datasource.setUrl(this.dbUrl);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);
//
//        // configuration
//        datasource.setInitialSize(initialSize);
////        datasource.setMinIdle(minIdle);
////        datasource.setMaxActive(maxActive);
////        datasource.setMaxWait(maxWait);
////        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
////        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
////        datasource.setValidationQuery(validationQuery);
////        datasource.setTestWhileIdle(testWhileIdle);
////        datasource.setTestOnBorrow(testOnBorrow);
////        datasource.setTestOnReturn(testOnReturn);
////        datasource.setPoolPreparedStatements(poolPreparedStatements);
////        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
////        try {
////            datasource.setFilters(filters);
////        } catch (SQLException e) {
////            log.error("druid configuration initialization filter", e);
////        }
////        datasource.setConnectionProperties(connectionProperties);
//
//        return datasource;
//    }
//
//    @Bean // 声明其为Bean实例
//    @Primary // 在同样的DataSource中，首先使用被标注的jdbcTemplate
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return  new JdbcTemplate(dataSource);
//    }
//
////    @Bean
////    public ServletRegistrationBean druidServlet() {
////        ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>();
////        servletRegistrationBean.setServlet(new StatViewServlet());
////        servletRegistrationBean.addUrlMappings("/druid/**");
////        Map<String, String> initParameters = new HashMap<>();
////        initParameters.put("resetEnable", "false"); //禁用HTML页面上的“Rest All”功能
////        //initParameters.put("allow", "10.8.9.115");  //ip白名单（没有配置或者为空，则允许所有访问）
////        initParameters.put("loginUsername", loginUserName);  //++监控页面登录用户名
////        initParameters.put("loginPassword", loginPassword);  //++监控页面登录用户密码
////        initParameters.put("deny", statViewDervletDeny); //ip黑名单
////        //如果某个ip同时存在，deny优先于allow
////        servletRegistrationBean.setInitParameters(initParameters);
////        return servletRegistrationBean;
////    }
//    @Bean
//    public StatFilter statFilter() {
//        StatFilter statFilter = new StatFilter();
//        statFilter.setLogSlowSql(true);
//        statFilter.setMergeSql(true);
//        statFilter.setSlowSqlMillis(3000);
//        return statFilter;
//    }
//    @Bean
//    public WallFilter wallFilter() {
//        WallFilter wallFilter = new WallFilter();
//        //允许执行多条SQL
//        WallConfig config = new WallConfig();
//        config.setMultiStatementAllow(true);
//        wallFilter.setConfig(config);
//        return wallFilter;
//    }
//}
