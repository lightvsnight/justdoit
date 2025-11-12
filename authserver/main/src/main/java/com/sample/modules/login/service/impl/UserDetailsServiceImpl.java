package com.sample.modules.login.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sample.modules.account.entity.TAccount;
import com.sample.modules.account.service.TAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @describe: TODO
 * @author: L.hk
 * @date: 2023/8/23 15:31
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TAccountService tAccountService;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryChainWrapper<TAccount> lambdaQueryChainWrapper =  tAccountService.lambdaQuery();
        //查询账户是否存在
        lambdaQueryChainWrapper.eq(TAccount::getUsername,username);
        //查询账户是否被锁定
        lambdaQueryChainWrapper.eq(TAccount::getAccountLocked, false);
        //查询账户是否过期，一般国外的软件有这个过期的概念，要定期检查账户是否还在使用
        lambdaQueryChainWrapper.eq(TAccount::getAccountExpired, false);
        // 这个查询要保证账号唯一，数据库中需要将账号设置为主键，不能重复。
        Long count = lambdaQueryChainWrapper.count();
        TAccount account = lambdaQueryChainWrapper.one();
        //检查登录用户与库中注册账号密码是否保持一致
        UserDetails user = null;
        if( account != null){
            user = User.withUsername(username)
                    .password(account.getPassword())
                    .disabled(false)
                    .accountLocked(false)
                    .accountExpired(false)
                    .roles("USER")
                    .build();
        }
        return user;
    }

}
