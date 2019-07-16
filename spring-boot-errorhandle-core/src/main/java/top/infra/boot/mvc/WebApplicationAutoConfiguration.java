package top.infra.boot.mvc;

import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import top.infra.boot.autoconfigure.ConditionalOnNotProductionEnv;
import top.infra.boot.mvc.api.DomainResolver;
import top.infra.boot.mvc.api.RequestResolver;
import top.infra.boot.mvc.internal.ContentCachingRequestFilter;
import top.infra.boot.mvc.internal.DefaultDomainResolver;
import top.infra.boot.mvc.internal.DefaultRequestResolver;

@AutoConfigureBefore({ErrorMvcAutoConfiguration.class, WebMvcAutoConfiguration.class})
@ComponentScan(basePackages = {"top.infra.boot.mvc.internal"})
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication
@Configuration
public class WebApplicationAutoConfiguration {

    @Autowired
    private ServerProperties serverProperties;

    @Bean
    public DomainResolver domainResolver() {
        return new DefaultDomainResolver(this.serverProperties);
    }

    @Bean
    public RequestResolver requestResolver() {
        return new DefaultRequestResolver();
    }

    @Bean
    @ConditionalOnNotProductionEnv
    public ContentCachingRequestFilter contentCachingRequestFilter() {
        return new ContentCachingRequestFilter();
    }
}
