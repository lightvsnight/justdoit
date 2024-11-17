/*
SQLyog 企业版 - MySQL GUI v8.14
MySQL - 5.7.17-log : Database - spring_oauth2_server
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`spring_oauth2_server` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `spring_oauth2_server`;

/*Table structure for table `oauth2_authorization` */

DROP TABLE IF EXISTS `oauth2_authorization`;

CREATE TABLE `oauth2_authorization` (
    id varchar(100) NOT NULL,
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorization_grant_type varchar(100) NOT NULL,
    attributes blob DEFAULT NULL,
    state varchar(500) DEFAULT NULL,
    authorization_code_value blob DEFAULT NULL,
    authorization_code_issued_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    authorization_code_expires_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    authorization_code_metadata blob DEFAULT NULL,
    access_token_value blob DEFAULT NULL,
    access_token_issued_at timestamp DEFAULT NULL,
    access_token_expires_at timestamp DEFAULT NULL,
    access_token_metadata blob DEFAULT NULL,
    access_token_type varchar(100) DEFAULT NULL,
    access_token_scopes varchar(1000) DEFAULT NULL,
    refresh_token_value blob DEFAULT NULL,
    refresh_token_issued_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    refresh_token_expires_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    refresh_token_metadata blob DEFAULT NULL,
    oidc_id_token_value blob DEFAULT NULL,
    oidc_id_token_issued_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    oidc_id_token_expires_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    oidc_id_token_metadata blob DEFAULT NULL,
    oidc_id_token_claims varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `oauth2_authorization` */

insert  into `oauth2_authorization`(`id`,`registered_client_id`,`principal_name`,`authorization_grant_type`,`attributes`,`state`,`authorization_code_value`,`authorization_code_issued_at`,`authorization_code_expires_at`,`authorization_code_metadata`,`access_token_value`,`access_token_issued_at`,`access_token_expires_at`,`access_token_metadata`,`access_token_type`,`access_token_scopes`,`oidc_id_token_value`,`oidc_id_token_issued_at`,`oidc_id_token_expires_at`,`oidc_id_token_metadata`,`refresh_token_value`,`refresh_token_issued_at`,`refresh_token_expires_at`,`refresh_token_metadata`) values ('296c1939-fee2-4184-a0e4-85f228cd6ac7','4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','user1','authorization_code','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"java.security.Principal\":{\"@class\":\"org.springframework.security.authentication.UsernamePasswordAuthenticationToken\",\"authorities\":[\"java.util.Collections$UnmodifiableRandomAccessList\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"details\":{\"@class\":\"org.springframework.security.web.authentication.WebAuthenticationDetails\",\"remoteAddress\":\"0:0:0:0:0:0:0:1\",\"sessionId\":\"C1DC4BEFAC83852A15794196CC4D23B9\"},\"authenticated\":true,\"principal\":{\"@class\":\"org.springframework.security.core.userdetails.User\",\"password\":null,\"username\":\"user1\",\"authorities\":[\"java.util.Collections$UnmodifiableSet\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true},\"credentials\":null},\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\":{\"@class\":\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\",\"authorizationUri\":\"http://localhost:9000/oauth2/authorize\",\"authorizationGrantType\":{\"value\":\"authorization_code\"},\"responseType\":{\"value\":\"code\"},\"clientId\":\"messaging-client\",\"redirectUri\":\"http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc\",\"scopes\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"state\":\"GcbIufzNrRpGFuVuqJtVvjZnHeLqc-V2t9mMzcHL-VA=\",\"additionalParameters\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"nonce\":\"x9xQdknvYM4L4GRJAOLTzn2mQSIzCrrhdm-bu3u5xzU\"},\"authorizationRequestUri\":\"http://localhost:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid&state=GcbIufzNrRpGFuVuqJtVvjZnHeLqc-V2t9mMzcHL-VA%3D&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc&nonce=x9xQdknvYM4L4GRJAOLTzn2mQSIzCrrhdm-bu3u5xzU\",\"attributes\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"}},\"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]]}',NULL,'daGTVV-bv03aMinCT3DffkRq4sdpfpqwKvnVaP5kwHOWvgQut741w5zAybjhho_UebMmoZPeK5jqXFJU5FJh4GkSYrLExySn0_WEBn5I8NHVxxb0WbxWSrvjD51gVg9W','2022-02-06 22:35:53','2022-02-06 22:40:53','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}',NULL,'2022-02-06 22:35:52','2022-02-06 22:35:52',NULL,NULL,NULL,NULL,'2022-02-06 22:35:52','2022-02-06 22:35:52',NULL,NULL,'2022-02-06 22:35:52','2022-02-06 22:35:52',NULL),('3007c5fb-7ab7-4107-98bc-414223393bad','4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','user1','authorization_code','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"java.security.Principal\":{\"@class\":\"org.springframework.security.authentication.UsernamePasswordAuthenticationToken\",\"authorities\":[\"java.util.Collections$UnmodifiableRandomAccessList\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"details\":{\"@class\":\"org.springframework.security.web.authentication.WebAuthenticationDetails\",\"remoteAddress\":\"0:0:0:0:0:0:0:1\",\"sessionId\":\"7B56BAC1978DE9DC42BF82BA0161173D\"},\"authenticated\":true,\"principal\":{\"@class\":\"org.springframework.security.core.userdetails.User\",\"password\":null,\"username\":\"user1\",\"authorities\":[\"java.util.Collections$UnmodifiableSet\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true},\"credentials\":null},\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\":{\"@class\":\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\",\"authorizationUri\":\"http://localhost:9000/oauth2/authorize\",\"authorizationGrantType\":{\"value\":\"authorization_code\"},\"responseType\":{\"value\":\"code\"},\"clientId\":\"messaging-client\",\"redirectUri\":\"http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc\",\"scopes\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"state\":\"tz4nXZONQdsMfIz3R04FdUDqvi8zySbWb0O3KHp77g0=\",\"additionalParameters\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"nonce\":\"T7IRr88GBI6s4ydxFndDnXU7Odwi5vC7RpMiHSvDhvM\"},\"authorizationRequestUri\":\"http://localhost:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid&state=tz4nXZONQdsMfIz3R04FdUDqvi8zySbWb0O3KHp77g0%3D&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc&nonce=T7IRr88GBI6s4ydxFndDnXU7Odwi5vC7RpMiHSvDhvM\",\"attributes\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"}},\"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]]}',NULL,'_2QQ7h4DuNrxWOniaMzaUZ0kToMGX3ilokbBrMGI2UkRNcSVof57UP4qrd2XdjwnqS4l-ylr28BK9TEKA2hZgaEDFSQp2nfMQalRACKdsKGaNhigJ8Fq2NeNcRYNi8cM','2022-02-07 21:20:15','2022-02-07 21:25:15','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}',NULL,'2022-02-07 21:20:14','2022-02-07 21:20:14',NULL,NULL,NULL,NULL,'2022-02-07 21:20:14','2022-02-07 21:20:14',NULL,NULL,'2022-02-07 21:20:14','2022-02-07 21:20:14',NULL),('422e170d-b4bb-4efd-be97-bf0c845f00da','4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','user1','authorization_code','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\":{\"@class\":\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\",\"authorizationUri\":\"http://localhost:9000/oauth2/authorize\",\"authorizationGrantType\":{\"value\":\"authorization_code\"},\"responseType\":{\"value\":\"code\"},\"clientId\":\"messaging-client\",\"redirectUri\":\"http://127.0.0.1:8080/authorized\",\"scopes\":[\"java.util.Collections$UnmodifiableSet\",[\"message.read\",\"message.write\"]],\"state\":\"lO60JQp8JxqmrfoLcXVhvVnsn29kvf_X7OjUFp8nQ7E=\",\"additionalParameters\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"},\"authorizationRequestUri\":\"http://localhost:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message.read%20message.write&state=lO60JQp8JxqmrfoLcXVhvVnsn29kvf_X7OjUFp8nQ7E%3D&redirect_uri=http://127.0.0.1:8080/authorized\",\"attributes\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"}},\"java.security.Principal\":{\"@class\":\"org.springframework.security.authentication.UsernamePasswordAuthenticationToken\",\"authorities\":[\"java.util.Collections$UnmodifiableRandomAccessList\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"details\":{\"@class\":\"org.springframework.security.web.authentication.WebAuthenticationDetails\",\"remoteAddress\":\"0:0:0:0:0:0:0:1\",\"sessionId\":\"C1DC4BEFAC83852A15794196CC4D23B9\"},\"authenticated\":true,\"principal\":{\"@class\":\"org.springframework.security.core.userdetails.User\",\"password\":null,\"username\":\"user1\",\"authorities\":[\"java.util.Collections$UnmodifiableSet\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true},\"credentials\":null},\"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE\":[\"java.util.HashSet\",[\"message.read\",\"message.write\"]]}',NULL,'l15PKO16yMil8zzQ2PDr-sja8BStv9Pij0YQ1LCP4YWkr7ep_ujFuli-TtBxphXGj2Y8fa1lJ66A11qDQPAQZ6YIQNBuTlrSQWjRqD7lr0LUFVMT3bN-kTqhXPyGYYSl','2022-02-06 22:36:00','2022-02-06 22:41:00','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":true}','eyJraWQiOiJhZjMyZmU0YS1kZTAyLTQwMjItYTI1Ny1mNDUwMWMyMGExZmIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE2NDQxNTgxNjAsInNjb3BlIjpbIm1lc3NhZ2UucmVhZCIsIm1lc3NhZ2Uud3JpdGUiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2NDQxNTg0NjAsImlhdCI6MTY0NDE1ODE2MH0.C6WN6FCDw10lhikQWpDTaxnSkROcqWIVEaWnZBf-WT8EfZACSvc_gfMpMgufuSe71sVsT8uoEk5FgeUOdCUnKdNcRvgtQFwgPCaiRkWmSSYoNhvmIpj9G7ob7Sotpdngg7i0gJWnKkR9ukTv_zdMs58yOSs1XsDOQvAu5bRUK5XxEcdH8u43vR7Bek5v9C7TDhas37r0DPSiPEp_6Fo3ajGs7WD65PDtp3jTZiNJdeyx2yjROm3Nn3ZvfuCFLvDPK6qp1eAtNXG6QXqJpAVj48-AozfC4V5WAstxZN5WP1--3fNSuWsGC_aW0jdN2sOg0uP4RYzeZabW1HxK8cqdVg','2022-02-06 22:36:01','2022-02-06 22:41:01','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"user1\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"nbf\":[\"java.time.Instant\",1644158160.569000000],\"scope\":[\"java.util.HashSet\",[\"message.read\",\"message.write\"]],\"iss\":[\"java.net.URL\",\"http://localhost:9000\"],\"exp\":[\"java.time.Instant\",1644158460.569000000],\"iat\":[\"java.time.Instant\",1644158160.569000000]},\"metadata.token.invalidated\":false}','Bearer','message.read,message.write',NULL,'2022-02-06 22:36:00','2022-02-06 22:36:00',NULL,'nf2WjISLiMzJBUTKrp-mhCWnH5NN9PZ3GkT0-5PoacvOeKp0-9GwjQeaXt3hKRlQCr_lVTq6kzrnZy6_gfco5u3a4sXV6ID1hh5ByFPnCWsq-1-d_QzGcB2FKUSD7gXx','2022-02-06 22:36:01','2022-02-06 23:36:01','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}'),('46a1cbc7-3515-4f1b-8156-d6e5e171bab3','4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','user1','authorization_code','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\":{\"@class\":\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\",\"authorizationUri\":\"http://localhost:9000/oauth2/authorize\",\"authorizationGrantType\":{\"value\":\"authorization_code\"},\"responseType\":{\"value\":\"code\"},\"clientId\":\"messaging-client\",\"redirectUri\":\"http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc\",\"scopes\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"state\":\"HMbO_mIsuktiFoOBlW1OaHkNVV0cLf9OBUDtIN-bTcc=\",\"additionalParameters\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"nonce\":\"nurarmDrvbnEwGvXEIBBDP2-LPfmrf3ySUlcsnWQxic\"},\"authorizationRequestUri\":\"http://localhost:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid&state=HMbO_mIsuktiFoOBlW1OaHkNVV0cLf9OBUDtIN-bTcc%3D&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc&nonce=nurarmDrvbnEwGvXEIBBDP2-LPfmrf3ySUlcsnWQxic\",\"attributes\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"}},\"java.security.Principal\":{\"@class\":\"org.springframework.security.authentication.UsernamePasswordAuthenticationToken\",\"authorities\":[\"java.util.Collections$UnmodifiableRandomAccessList\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"details\":{\"@class\":\"org.springframework.security.web.authentication.WebAuthenticationDetails\",\"remoteAddress\":\"0:0:0:0:0:0:0:1\",\"sessionId\":\"7B56BAC1978DE9DC42BF82BA0161173D\"},\"authenticated\":true,\"principal\":{\"@class\":\"org.springframework.security.core.userdetails.User\",\"password\":null,\"username\":\"user1\",\"authorities\":[\"java.util.Collections$UnmodifiableSet\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true},\"credentials\":null},\"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]]}',NULL,'A9DqO3aXEYDYQoijD5MZS50PLFIQ2LV1AzMnjjqwp2INeaUYN2Nwgt4dbjGDKeEywmNrNU-9bQiQx5NtF6L3qPozPlyic6MfR0_ZOeR6XKCdyNoFxGX-QzzK_VDi1dP6','2022-02-07 21:20:15','2022-02-07 21:25:15','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":true}','eyJraWQiOiI0M2M0MWUzYS01ZTZmLTQzZDUtOTgzMS04YzM5OGRiNzUzYTIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE2NDQyNDAwMTUsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6OTAwMCIsImV4cCI6MTY0NDI0MDMxNSwiaWF0IjoxNjQ0MjQwMDE1fQ.oFOong5xjYXF8bQUaMM4hocKAMey_K4oNEmVnU6uf0bpwYszj8yDXVcx3rCRwzqyjcHJYJOZs_hrI7XHgSNvYM-KzSUeazJzegJB-D9BHwuvy-1rJh2SV-xDPGs1LlU3I_MmUDBq0Ew-MLaZuqUR49hqccHQoq55t4EzYq7_mAzp93QG5yZGFFpy8dUfonFcUDg7eVGudc7ZRWYVGCjFYrpDggBoKy4VCRAPDN7W7BOJHUbeytdrzok49vAKwGs34X5q5czPRect293-7NtZt6k8v5NvCyxeHRG9VkdYLvkmx9mkHJ8H1p0zidyoCtbFXAsAsnJQhjbu9VDAQSLF8g','2022-02-07 21:20:15','2022-02-07 21:25:15','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"user1\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"nbf\":[\"java.time.Instant\",1644240015.078000000],\"scope\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"iss\":[\"java.net.URL\",\"http://localhost:9000\"],\"exp\":[\"java.time.Instant\",1644240315.078000000],\"iat\":[\"java.time.Instant\",1644240015.078000000]},\"metadata.token.invalidated\":false}','Bearer','openid','eyJraWQiOiI0M2M0MWUzYS01ZTZmLTQzZDUtOTgzMS04YzM5OGRiNzUzYTIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJhenAiOiJtZXNzYWdpbmctY2xpZW50IiwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2NDQyNDE4MTUsImlhdCI6MTY0NDI0MDAxNSwibm9uY2UiOiJudXJhcm1EcnZibkV3R3ZYRUlCQkRQMi1MUGZtcmYzeVNVbGNzbldReGljIn0.WmS56vkizZSNAfZ3_-5wMy11VsBpMFgZjfB7GWYMQ4V-SMxFMLwpT82JEw6eCtYaDpKYT2QxSg7xbUpK98gGYeuIAMHiRfSaAC0YxvMtWMeLuEngPDisk1F0_WTK9MSfpyElCgunwfbjQzPcvvCYSQkzI9ImH7GFlsR-PKLOx_e0vC73XTbOAqnjMI5WUCsuZ8U2T6wNtc1Ctj4zz8orje9mcDNP-LOwftXt4rHUBD4QVoC7Wj9iOISJVDu9-tzDeewJi2uDISuAI1rJOZR2lRqHIsNfOZ7wr8NLbb17A30d-2ozG8jwAlpdC_ig1N2_QVVsUEa4uAauw69qJLFcgw','2022-02-07 21:20:15','2022-02-07 21:50:15','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"user1\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"azp\":\"messaging-client\",\"iss\":[\"java.net.URL\",\"http://localhost:9000\"],\"exp\":[\"java.time.Instant\",1644241815.135000000],\"iat\":[\"java.time.Instant\",1644240015.135000000],\"nonce\":\"nurarmDrvbnEwGvXEIBBDP2-LPfmrf3ySUlcsnWQxic\"},\"metadata.token.invalidated\":false}','Wb79kbQqNtnAM1ymVyaIZWwqY1XQRjHnvEuL9ryvcR1ie3ZWfjukcy9rOnpllNLiggc7lCIGeyqUvX5gx-5Mrzes5Hy-6YnXOwDnJ2mN1SXImaEtBWmqrpPe61e7Sfym','2022-02-07 21:20:15','2022-02-07 22:20:15','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}'),('663b45f4-9c4c-45f2-bab3-d0cc53dfb004','4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','user1','authorization_code','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\":{\"@class\":\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\",\"authorizationUri\":\"http://localhost:9000/oauth2/authorize\",\"authorizationGrantType\":{\"value\":\"authorization_code\"},\"responseType\":{\"value\":\"code\"},\"clientId\":\"messaging-client\",\"redirectUri\":\"http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc\",\"scopes\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"state\":\"VMFEFprMw_EXCRh2_SQQZGe26K69OjCpeP7rpK7x2Vg=\",\"additionalParameters\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"nonce\":\"nGUtkUe4NbGjRqd-y0FKdQcLh5E3C4m3jg39eif5onc\"},\"authorizationRequestUri\":\"http://localhost:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid&state=VMFEFprMw_EXCRh2_SQQZGe26K69OjCpeP7rpK7x2Vg%3D&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc&nonce=nGUtkUe4NbGjRqd-y0FKdQcLh5E3C4m3jg39eif5onc\",\"attributes\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"}},\"java.security.Principal\":{\"@class\":\"org.springframework.security.authentication.UsernamePasswordAuthenticationToken\",\"authorities\":[\"java.util.Collections$UnmodifiableRandomAccessList\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"details\":{\"@class\":\"org.springframework.security.web.authentication.WebAuthenticationDetails\",\"remoteAddress\":\"0:0:0:0:0:0:0:1\",\"sessionId\":\"C1DC4BEFAC83852A15794196CC4D23B9\"},\"authenticated\":true,\"principal\":{\"@class\":\"org.springframework.security.core.userdetails.User\",\"password\":null,\"username\":\"user1\",\"authorities\":[\"java.util.Collections$UnmodifiableSet\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true},\"credentials\":null},\"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]]}',NULL,'O7G_uXT-vvva6XbVJ0khE0LyJK6SomBCLRoXSdWBe3EJUXpKGn_YPrrCBFY4GtW2w-kDjH14CBiJe6yLna595R8TNGZRyuJcLfpSm1Dj8wGH0PSkwGTLq6p-C8NOGdLG','2022-02-06 22:35:53','2022-02-06 22:40:53','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":true}','eyJraWQiOiJhZjMyZmU0YS1kZTAyLTQwMjItYTI1Ny1mNDUwMWMyMGExZmIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE2NDQxNTgxNTIsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6OTAwMCIsImV4cCI6MTY0NDE1ODQ1MiwiaWF0IjoxNjQ0MTU4MTUyfQ.Chi65dxFpl08orkSvCY1lM_auaUjqszMu8QrYl-9zddSBiWzYpjaobE6xr1Q4AEZ8fgDZnuEXdiO3kWq3Duc5aUPHipx66UASbk_-trY8lKTUgQAhLpX2CbePJbkFJdtBe3dE5SvjdSMv-jlpMKiHjBrsNq9k9G-oMyvPSKBLYBHHT_LofTENAO9hj9rPwYJ5CiGlXoijykT3SKWZTrlL-wF3q5RG4UKXxe5repv-vLyMPVKp4dBuiOPB4PE8gUViGuNEdk1puJO_NPk2FREeIZ1APE1Ipx5tjsvn76QIsSFDJil9QTmvb_Jd1DKz5PtK3u-tbODC9qJHEEUo463_g','2022-02-06 22:35:53','2022-02-06 22:40:53','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"user1\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"nbf\":[\"java.time.Instant\",1644158152.981000000],\"scope\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"iss\":[\"java.net.URL\",\"http://localhost:9000\"],\"exp\":[\"java.time.Instant\",1644158452.981000000],\"iat\":[\"java.time.Instant\",1644158152.981000000]},\"metadata.token.invalidated\":false}','Bearer','openid','eyJraWQiOiJhZjMyZmU0YS1kZTAyLTQwMjItYTI1Ny1mNDUwMWMyMGExZmIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJhenAiOiJtZXNzYWdpbmctY2xpZW50IiwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2NDQxNTk5NTMsImlhdCI6MTY0NDE1ODE1Mywibm9uY2UiOiJuR1V0a1VlNE5iR2pScWQteTBGS2RRY0xoNUUzQzRtM2pnMzllaWY1b25jIn0.YnQL4I0gNofnr4bIbNwgEGoP1dJ21CaVb0gBaxLssEpznCCECqzHNN55sRmWC7jEudDTGDeT-zKWTj6n9Dabk2jrgc_6PrhYgcoYmwDoLTtFKEBKja7Pe0qWob0Pa6ZiQT27iPLiNEI5H81lkv9h6xPe3UdYDOWXuPKKNRTJ7YXUyuVuuK2ctK7FQ-A0ThkH_Y22EgVAyo-wX9MBBMYqAvdHh48DUcVQiqv4Yss9jZ2pCFlV_EWw6HkCPXByvd6--abxZpKDhnSFqiTGtlcw3UJ_sxnX39ih_BtE52CNp_jOiZs5b9pbf3FCfrXj6tctUwsIvACJSQkKHJsqmLhKQQ','2022-02-06 22:35:53','2022-02-06 23:05:53','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"user1\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"azp\":\"messaging-client\",\"iss\":[\"java.net.URL\",\"http://localhost:9000\"],\"exp\":[\"java.time.Instant\",1644159953.036000000],\"iat\":[\"java.time.Instant\",1644158153.036000000],\"nonce\":\"nGUtkUe4NbGjRqd-y0FKdQcLh5E3C4m3jg39eif5onc\"},\"metadata.token.invalidated\":false}','CErHfh5DEIc4m1vSBhvpfc_GQ_1IvkqhfqkbgA_FFUGWpXKCqOOwJZSyOtEE0bOZTu_ueSnYR1VACQ7pvRypNNjkbCkWsG8jGami5lkm3xt1oRl92tsCc3JYFJkeT6al','2022-02-06 22:35:53','2022-02-06 23:35:53','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}'),('ba519806-6547-4cd6-9c51-45e69abbb9c0','f38e09b4-c9e6-4f07-a615-289aa55b2eb5','user1','authorization_code','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\":{\"@class\":\"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest\",\"authorizationUri\":\"http://localhost:9000/oauth2/authorize\",\"authorizationGrantType\":{\"value\":\"authorization_code\"},\"responseType\":{\"value\":\"code\"},\"clientId\":\"messaging-client\",\"redirectUri\":\"http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc\",\"scopes\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"state\":\"QIgA3cZ-DK-Xw1nQvsgM0JHFbI1wC23tKlAYVTCzttc=\",\"additionalParameters\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"nonce\":\"DB5DzQFK6SgbPiQIVhtMowvd3Os9ugPWiiSKGwegqCo\"},\"authorizationRequestUri\":\"http://localhost:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid&state=QIgA3cZ-DK-Xw1nQvsgM0JHFbI1wC23tKlAYVTCzttc%3D&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc&nonce=DB5DzQFK6SgbPiQIVhtMowvd3Os9ugPWiiSKGwegqCo\",\"attributes\":{\"@class\":\"java.util.Collections$UnmodifiableMap\"}},\"java.security.Principal\":{\"@class\":\"org.springframework.security.authentication.UsernamePasswordAuthenticationToken\",\"authorities\":[\"java.util.Collections$UnmodifiableRandomAccessList\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"details\":{\"@class\":\"org.springframework.security.web.authentication.WebAuthenticationDetails\",\"remoteAddress\":\"0:0:0:0:0:0:0:1\",\"sessionId\":\"BE37A06326CFBFD2A6C2916CD727CE59\"},\"authenticated\":true,\"principal\":{\"@class\":\"org.springframework.security.core.userdetails.User\",\"password\":null,\"username\":\"user1\",\"authorities\":[\"java.util.Collections$UnmodifiableSet\",[{\"@class\":\"org.springframework.security.core.authority.SimpleGrantedAuthority\",\"authority\":\"ROLE_USER\"}]],\"accountNonExpired\":true,\"accountNonLocked\":true,\"credentialsNonExpired\":true,\"enabled\":true},\"credentials\":null},\"org.springframework.security.oauth2.server.authorization.OAuth2Authorization.AUTHORIZED_SCOPE\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]]}',NULL,'N_-EQHnGIzscLn2p5_2ZQSEj5qX0-Z4QuAtrqrFjmx0RHl9CQ5bF8A-LcHmrlfkD_HdogfDfmf49i4ZcnFqjAFVFfl-yoDT5NSXyjgzojlTroMs5gSddfJLOjtJYh-GJ','2022-02-05 23:05:54','2022-02-05 23:10:54','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":true}','eyJraWQiOiIwMTRiMWJmMi02MWExLTRlMjItOTcxZi02NGY5N2EzMDViMTkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJuYmYiOjE2NDQwNzM1NTQsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6OTAwMCIsImV4cCI6MTY0NDA3Mzg1NCwiaWF0IjoxNjQ0MDczNTU0fQ.f49_3_qtscjczKbGous17DXY1TYj9b9oMcqFDwpkdi3QhiENUiQa4C6QtT0Z8nXIEVvT7x4u7h70AK_lZgrLX5F8RwDxaYd6cSpQ8XbLioOZN78jo5-Qxmz9_X1-p2-7BTalE5Bo2ULGmrXfAfnnIj1WdEPgeNa95ojvjHoTMiWQTLrQyuSVUenJs6ZnNv4cKSlQdw9_GpCko372XNKrwCas1YhkZBOCUEvIYbyz4O5UUKlCOWnAGx_7ijMtGMI8b4RNzcB2W1eoPTmRFOisk_RkELBhelbCc0JMxfJuOLuLh0_gta9w-TtprZhZkSF8AgkCDrP8ibxv46U-K5w4bQ','2022-02-05 23:05:54','2022-02-05 23:10:54','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"user1\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"nbf\":[\"java.time.Instant\",1644073554.450000000],\"scope\":[\"java.util.Collections$UnmodifiableSet\",[\"openid\"]],\"iss\":[\"java.net.URL\",\"http://localhost:9000\"],\"exp\":[\"java.time.Instant\",1644073854.450000000],\"iat\":[\"java.time.Instant\",1644073554.450000000]},\"metadata.token.invalidated\":false}','Bearer','openid','eyJraWQiOiIwMTRiMWJmMi02MWExLTRlMjItOTcxZi02NGY5N2EzMDViMTkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImF1ZCI6Im1lc3NhZ2luZy1jbGllbnQiLCJhenAiOiJtZXNzYWdpbmctY2xpZW50IiwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2NDQwNzUzNTQsImlhdCI6MTY0NDA3MzU1NCwibm9uY2UiOiJEQjVEelFGSzZTZ2JQaVFJVmh0TW93dmQzT3M5dWdQV2lpU0tHd2VncUNvIn0.V7f4RlHbMFHLA75_iYZdsApf8zkmUb3Q5S6J6dD6A2XxjfHSDSdTUwC7BqBROhEv4YEc79Dg4FkBPdWa6p9jzjZ1YSeEfOd6im1lMTj59YbEckbKSva4ocxTVYe2P_3bLIsQ6y9_dtvTlCPiqbFM5ThoCukPmph-JwarM-VB-mF9fSh1eOglA02IKZtyRx7c3uHZPSvczhC5c5W0rlmyL5HXC2jfgNBQ0fwR6YPpKB9wPsbXaL7sN5ZbPqUoEQ_ugZbBRFHcbzkapnV4FwjtjLjmRaEQsmCEnLaPaLGLpspTKQYq6uMuDDMKt6ghNFYhGpWgZvYb5osfs97Pu8XWmA','2022-02-05 23:05:54','2022-02-05 23:35:54','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"user1\",\"aud\":[\"java.util.Collections$SingletonList\",[\"messaging-client\"]],\"azp\":\"messaging-client\",\"iss\":[\"java.net.URL\",\"http://localhost:9000\"],\"exp\":[\"java.time.Instant\",1644075354.465000000],\"iat\":[\"java.time.Instant\",1644073554.465000000],\"nonce\":\"DB5DzQFK6SgbPiQIVhtMowvd3Os9ugPWiiSKGwegqCo\"},\"metadata.token.invalidated\":false}','-gyMsZRRGVB43fI36xAeS1TDxGzLgh3HL6ldbgpPYfZRVEOIK_5BNKq7EIKE5YJC5iuSSNbCu-qUE4T0IU_Z1GLt6Zr-Rp2h2QOcZHnuMsVnhfLmncxeNFoDU9PuZU1Q','2022-02-05 23:05:54','2022-02-06 00:05:54','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.invalidated\":false}');

/*Table structure for table `oauth2_authorization_consent` */

DROP TABLE IF EXISTS `oauth2_authorization_consent`;

CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` varchar(100) NOT NULL,
  `principal_name` varchar(200) NOT NULL,
  `authorities` varchar(1000) NOT NULL,
  PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `oauth2_authorization_consent` */

insert  into `oauth2_authorization_consent`(`registered_client_id`,`principal_name`,`authorities`) values ('4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','user1','SCOPE_message.read,SCOPE_message.write'),('f38e09b4-c9e6-4f07-a615-289aa55b2eb5','user1','SCOPE_message.read,SCOPE_message.write');

/*Table structure for table `oauth2_registered_client` */

DROP TABLE IF EXISTS `oauth2_registered_client`;

CREATE TABLE `oauth2_registered_client` (
  `id` varchar(45) NOT NULL,
  `client_id` varchar(100) NOT NULL,
  `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `client_secret` varchar(200) DEFAULT NULL,
  `client_secret_expires_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `client_name` varchar(200) NOT NULL,
  `client_authentication_methods` varchar(1000) NOT NULL,
  `authorization_grant_types` varchar(1000) NOT NULL,
  `redirect_uris` varchar(1000) DEFAULT NULL,
  `scopes` varchar(1000) NOT NULL,
  `client_settings` varchar(2000) NOT NULL,
  `token_settings` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `oauth2_registered_client` */

/* 客户端申请表 */
CREATE TABLE `client_apply` (
  `id` varchar(45) NOT NULL,
  `client_id` varchar(100) NOT NULL,
  `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `client_secret` varchar(200) DEFAULT NULL,
  `client_secret_expires_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `client_name` varchar(200) NOT NULL,
  `client_authentication_methods` varchar(1000) NOT NULL,
  `authorization_grant_types` varchar(1000) NOT NULL,
  `redirect_uris` varchar(1000) DEFAULT NULL,
  `scopes` varchar(1000) NOT NULL,
  `client_settings` varchar(2000) NOT NULL,
  `token_settings` varchar(2000) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




insert  into `oauth2_registered_client`(`id`,`client_id`,`client_id_issued_at`,`client_secret`,`client_secret_expires_at`,`client_name`,`client_authentication_methods`,`authorization_grant_types`,`redirect_uris`,`scopes`,`client_settings`,`token_settings`) values ('4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','messaging-client','2022-02-06 22:31:48','{noop}secret','2022-02-06 22:31:48','4b1b1e3a-64b8-47ca-aaed-29c8c6fa9f44','client_secret_basic','refresh_token,client_credentials,authorization_code','http://127.0.0.1:8080/authorized,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc','openid,message.read,message.write','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000]}'),('989b9d33-45ee-4c62-bc9e-24fb19620c1e','messaging-client','2022-02-06 22:28:31','{noop}secret','2022-02-06 22:28:30','989b9d33-45ee-4c62-bc9e-24fb19620c1e','client_secret_basic','refresh_token,client_credentials,authorization_code','http://127.0.0.1:8080/authorized,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc','openid,message.read,message.write','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000]}'),('a880c769-6bb1-465a-bee3-dd97beff23da','messaging-client','2022-02-07 21:03:40','{noop}secret','2022-02-07 21:03:40','a880c769-6bb1-465a-bee3-dd97beff23da','client_secret_basic','refresh_token,client_credentials,authorization_code','http://127.0.0.1:8080/authorized,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc','openid,message.read,message.write','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000]}'),('b822d3fd-0042-427b-b721-d3306c6b2eb3','messaging-client','2022-02-06 22:27:33','{noop}secret','2022-02-06 22:27:33','b822d3fd-0042-427b-b721-d3306c6b2eb3','client_secret_basic','refresh_token,client_credentials,authorization_code','http://127.0.0.1:8080/authorized,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc','openid,message.read,message.write','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000]}'),('f38e09b4-c9e6-4f07-a615-289aa55b2eb5','messaging-client','2022-02-05 23:01:24','{noop}secret','2022-02-05 23:01:23','f38e09b4-c9e6-4f07-a615-289aa55b2eb5','client_secret_basic','refresh_token,client_credentials,authorization_code','http://127.0.0.1:8080/authorized,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc','openid,message.read,message.write','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000]}');

/*Table structure for table `t_account_role` */

DROP TABLE IF EXISTS `t_account_role`;

CREATE TABLE `t_account_role` (
  `id` varchar(45) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

/*Data for the table `t_account_role` */

insert  into `t_account_role`(`id`,`user_id`,`role_id`) values (1,1,1);

/*Table structure for table `t_permission` */

DROP TABLE IF EXISTS `t_permission`;

CREATE TABLE `t_permission` (
  `id` varchar(45) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `enname` varchar(64) NOT NULL COMMENT '权限英文名称',
  `url` varchar(255) NOT NULL COMMENT '授权路径',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='权限表';

/*Data for the table `t_permission` */

insert  into `t_permission`(`id`,`parent_id`,`name`,`enname`,`url`,`description`,`created`,`updated`) values (1,0,'系统管理','System','/',NULL,'2019-04-04 23:22:54','2019-04-04 23:22:56'),(2,37,'用户管理','SystemUser','/users/',NULL,'2019-04-04 23:25:31','2019-04-04 23:25:33'),(3,38,'查看用户','SystemUserView','',NULL,'2019-04-04 15:30:30','2019-04-04 15:30:43'),(4,38,'新增用户','SystemUserInsert','',NULL,'2019-04-04 15:30:31','2019-04-04 15:30:44'),(5,38,'编辑用户','SystemUserUpdate','',NULL,'2019-04-04 15:30:32','2019-04-04 15:30:45'),(6,38,'删除用户','SystemUserDelete','',NULL,'2019-04-04 15:30:48','2019-04-04 15:30:45');

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` varchar(45) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父角色',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `enname` varchar(64) NOT NULL COMMENT '角色英文名称',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `t_role` */

insert  into `t_role`(`id`,`parent_id`,`name`,`enname`,`description`,`created`,`updated`) values (1,0,'超级管理员','admin',NULL,'2019-04-04 23:22:03','2019-04-04 23:22:05');

/*Table structure for table `t_role_permission` */

DROP TABLE IF EXISTS `t_role_permission`;

CREATE TABLE `t_role_permission` (
  `id` varchar(45) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

/*Data for the table `t_role_permission` */

insert  into `t_role_permission`(`id`,`role_id`,`permission_id`) values (1,1,1),(2,1,2);

/*Table structure for table `users` */
DROP TABLE IF EXISTS `t_account`;

CREATE TABLE `t_account` (
  `id` varchar(45) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) NOT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `is_enabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已失效',
  `disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否开启',
  `account_expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否过期',
  `account_locked` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否锁定',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '最新更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COMMENT='用户表';

/*Data for the table `users` */
INSERT INTO `t_account`(`id`,`username`,`password`,`phone`,`is_enabled`,`email`,`created`,`updated`) values (1,'user1','{MD5}24c9e15e52afc47c225b757e7bee1f9d','15888888888',1,'','2022-02-05 01:20:50','2019-04-04 23:21:29');
INSERT INTO `t_account`(`id`,`username`,`password`,`phone`,`email`,`is_enabled`,`disabled`,`account_expired`,`account_locked`,`del_flag`,`created`,`updated`) VALUES ('2', 'user2', '{MD5}24c9e15e52afc47c225b757e7bee1f9d', '13483666246', 'ziyou1102@aliyun.com', '1', '0', '0', '0', '0', '2022-02-05 01:20:50', '2019-04-04 23:21:29');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
