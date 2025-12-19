package com.sample.modules.tRegisteredClient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sample.modules.tRegisteredClient.entity.TRegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li.HongKun
 * @since 2025-11-12 20:18:22
 */
public interface TRegisteredClientService extends IService<TRegisteredClient>, RegisteredClientRepository {

}
