package com.yesmind.stok;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles({"functional-test"})
@TestPropertySource(properties = "spring.profiles.active=functional-test", locations = {"classpath:/application-functional-test.properties"})
@WebAppConfiguration
@Transactional
@SpringBootTest
public abstract class FunctionalTestBase {
}
