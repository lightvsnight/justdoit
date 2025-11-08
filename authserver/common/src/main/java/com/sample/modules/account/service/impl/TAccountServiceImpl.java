package com.sample.modules.account.service.impl;

import com.sample.modules.account.entity.TAccount;
import com.sample.modules.account.mapper.TAccountMapper;
import com.sample.modules.account.service.TAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Li.HongKun
 * @since 2025-11-05 22:05:24
 */
@Service
public class TAccountServiceImpl extends ServiceImpl<TAccountMapper, TAccount> implements TAccountService {

}
