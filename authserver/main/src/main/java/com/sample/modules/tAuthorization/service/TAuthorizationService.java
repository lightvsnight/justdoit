package com.sample.modules.tAuthorization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sample.modules.tAuthorization.entity.TAuthorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li.HongKun
 * @since 2025-11-30 19:46:59
 */
public interface TAuthorizationService extends IService<TAuthorization>,OAuth2AuthorizationService {
}
