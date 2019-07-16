package top.infra.errorhandle.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhanghaolun on 16/7/12.
 */
@Configuration
@ConditionalOnClass(name = {"feign.Feign", "com.fasterxml.jackson.databind.ObjectMapper"})
public class FeignErrorDecoderConfiguration {

    @Bean
    public GenericFeignErrorDecoder errorDecoder() {
        return new GenericFeignErrorDecoder();
    }
}
