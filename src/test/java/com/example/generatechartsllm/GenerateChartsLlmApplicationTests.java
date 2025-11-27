package com.example.generatechartsllm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "openai.api.key=test-key",
    "openai.api.model=gpt-4o-mini"
})
class GenerateChartsLlmApplicationTests {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }

}
