package space.astralbridge.eeg4asd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dashscope.aigc.generation.Generation;

@Configuration
public class LLMConfiguration {

    @Bean
    public Generation generation() {
        return new Generation();
    }
}
