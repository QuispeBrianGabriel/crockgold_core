package com.crockgold.core;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {"spring.security.user.name=test", "spring.security.user.password=test"})
@AutoConfigureMockMvc
class SecurityConfigurationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void healthIsPublic() throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());
    }

    @Test
    void otherEndpointsRequireAuthentication() throws Exception {
        mvc.perform(get("/actuator/info")).andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedRequestDoesNotCreateSession() throws Exception {
        mvc.perform(get("/actuator/info").with(httpBasic("test", "test")))
                .andExpect(status().isNotFound())
                .andExpect(request().sessionAttributeDoesNotExist("SPRING_SECURITY_CONTEXT"))
                .andExpect(header().doesNotExist("Set-Cookie"));
    }
}
