package com.twitterclone.twitterapi;

import com.twitterclone.twitterapi.config.TestSecurityConfig;
import com.twitterclone.twitterapi.config.TestWebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import({TestSecurityConfig.class, TestWebSecurityConfig.class})
@ActiveProfiles("test")
class TwitterapiApplicationTests {

	@Test
	void contextLoads() {
	}

}
