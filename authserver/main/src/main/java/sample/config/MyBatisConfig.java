package sample.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.ReplacePlaceholderInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
@MapperScan(value = {"sample.**.**.mapper.**"})
@EnableTransactionManagement //告诉它这是个事务
public class MyBatisConfig {

    /**
     *
     * 悲观锁	每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会阻塞直到它拿到锁。
     *          传统的关系型数据库里边就用到了很多这种锁机制，比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁。
     * ---------------------------------------------------------------------------------------------------------------
     * 乐观锁	每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，
     * 可以使用版本号等机制。乐观锁适用于多读的应用类型，这样可以提高吞吐量，像数据库如果提供类似于write_condition机制的其实都是提供的乐观锁。
     *
     * ---------------------------------------------------------------------------------------------------------------
     * 锁	    总结
     * 悲观锁	写入频繁
     * 乐观锁	读取频繁
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //是一个拦截器，用于防止恶意攻击。它可以拦截一些常见的攻击方式，例如SQL注入、XSS攻击、CSRF等。使用该拦截器可以增加系统的安全性。
        interceptor.addInnerInterceptor(new DynamicTableNameInnerInterceptor());
        //添加分页插件
//        interceptor.addInnerInterceptor(new ReplacePlaceholderInnerInterceptor(DbType.MYSQL));
        //添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


}