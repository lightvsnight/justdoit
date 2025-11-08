package com.sample.account;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 账号密码生成方法，密码为不可逆向生成，且每次加密的结果不重复
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String password = encoder.encode("123456");
        System.out.println(password);
    }
}
