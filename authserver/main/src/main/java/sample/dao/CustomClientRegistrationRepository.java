package sample.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import sample.modules.registeredclient.entity.Oauth2RegisteredClient;
import sample.modules.registeredclient.service.Oauth2RegisteredClientService;

@Repository("clientRegistrationRepository")
public class CustomClientRegistrationRepository implements ClientRegistrationRepository {

    @Autowired
    private Oauth2RegisteredClientService oauth2RegisteredClientService;

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        Assert.hasText(registrationId, "clientId cannot be empty");
        LambdaQueryChainWrapper<Oauth2RegisteredClient> wrapper =  oauth2RegisteredClientService.lambdaQuery();
        wrapper.eq(Oauth2RegisteredClient::getClientId,registrationId);
        oauth2RegisteredClientService.getOne(wrapper);
        return null;
    }
}
