package com.crockgold.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = "spring.security.user.password=test")
class CrockgoldCoreApplicationTests {

    @Test
    void contextLoads() {}
}
