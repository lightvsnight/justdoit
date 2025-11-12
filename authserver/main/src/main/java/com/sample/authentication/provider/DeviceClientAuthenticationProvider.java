//package com.sample.authentication.provider;
//
//import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
//import com.sample.authentication.DeviceClientAuthenticationToken;
//import com.sample.modules.account.entity.TAccount;
//import com.sample.modules.registeredClient.entity.Oauth2RegisteredClient;
//import com.sample.modules.registeredClient.service.Oauth2RegisteredClientService;
//import com.sample.web.authentication.DeviceClientAuthenticationConverter;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.jspecify.annotations.Nullable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//import static com.sample.authentication.DeviceClientAuthenticationProvider.throwInvalidClient;
//
///**
// * @author Li hongKun
// * @since 1.1
// * @see DeviceClientAuthenticationToken
// * @see DeviceClientAuthenticationConverter
// * @see OAuth2ClientAuthenticationFilter
// */
//@Component
//public class DeviceClientAuthenticationProvider implements AuthenticationProvider {
//    private final Log logger = LogFactory.getLog(getClass());
//
//    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";
//
//
//    @Autowired
//    private Oauth2RegisteredClientService registeredClientService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        DeviceClientAuthenticationToken deviceClientAuthentication =
//                (DeviceClientAuthenticationToken) authentication;
//
//        if (!ClientAuthenticationMethod.NONE.equals(deviceClientAuthentication.getClientAuthenticationMethod())) {
//            return null;
//        }
//
//        String clientId = deviceClientAuthentication.getPrincipal().toString();
//        LambdaQueryChainWrapper<Oauth2RegisteredClient> lambdaQueryChainWrapper =  registeredClientService.lambdaQuery();
//        lambdaQueryChainWrapper.eq(Oauth2RegisteredClient::getClientId,clientId);
//        Oauth2RegisteredClient oauth2RegisteredClient = registeredClientService.getOne(lambdaQueryChainWrapper);
////        RegisteredClient registeredClient = registeredClientService.findByClientId(clientId);
//        if (oauth2RegisteredClient == null) {
//            throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
//        }
//
//        if (this.logger.isTraceEnabled()) {
//            this.logger.trace("Retrieved registered client");
//        }
//
//        if (!registeredClient.getClientAuthenticationMethods().contains(
//                deviceClientAuthentication.getClientAuthenticationMethod())) {
//            throwInvalidClient("authentication_method");
//        }
//
//        if (this.logger.isTraceEnabled()) {
//            this.logger.trace("Validated device client authentication parameters");
//        }
//
//        if (this.logger.isTraceEnabled()) {
//            this.logger.trace("Authenticated device client");
//        }
//
//        return new DeviceClientAuthenticationToken(registeredClient,
//                deviceClientAuthentication.getClientAuthenticationMethod(), null);
//    }
//
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return DeviceClientAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//
//    private static void throwInvalidClient(String parameterName) {
//        OAuth2Error error = new OAuth2Error(
//                OAuth2ErrorCodes.INVALID_CLIENT,
//                "Device client authentication failed: " + parameterName,
//                ERROR_URI
//        );
//        throw new OAuth2AuthenticationException(error);
//    }
//}
