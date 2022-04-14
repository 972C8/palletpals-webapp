package ch.fhnw.palletpals.config;

import ch.fhnw.palletpals.business.service.UserService;
import ch.fhnw.palletpals.data.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("test")
@Configuration
public class DataGeneratorTest {

    @Autowired
    private UserService agentService;

    @PostConstruct
    private void init() throws Exception {
        demoUser();
    }

    private void demoUser() throws Exception {
        User userUser = new User();
        userUser.setEmail("user@user.com");
        userUser.setPassword("password");
        userUser.setUserName("user");
        agentService.saveUser(userUser);
    }
}
