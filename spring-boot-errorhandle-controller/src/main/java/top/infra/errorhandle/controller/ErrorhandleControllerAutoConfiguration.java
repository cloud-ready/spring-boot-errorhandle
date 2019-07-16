package top.infra.errorhandle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import javax.servlet.Servlet;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import top.infra.boot.mvc.WebApplicationAutoConfiguration;
import top.infra.errorhandle.ErrorhandleAutoConfiguration;

@AutoConfigureAfter({WebApplicationAutoConfiguration.class, ErrorhandleAutoConfiguration.class})
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class, WebMvcAutoConfiguration.class, SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"top.infra.errorhandle.controller"})
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, ObjectMapper.class})
@ConditionalOnProperty(prefix = "server.error.controller", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication
@Configuration
public class ErrorhandleControllerAutoConfiguration {

    private final ServerProperties serverProperties;

    private final List<ErrorViewResolver> errorViewResolvers;

    public ErrorhandleControllerAutoConfiguration( //
        final ServerProperties serverProperties, //
        final ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        this.serverProperties = serverProperties;
        this.errorViewResolvers = errorViewResolversProvider.getIfAvailable();
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public ExtendedErrorAttributes errorAttributes() {
        return new ExtendedErrorAttributes();
    }

    @Bean
    @ConditionalOnMissingBean(
        value = org.springframework.boot.web.servlet.error.ErrorController.class,
        search = SearchStrategy.CURRENT
    )
    public ErrorController errorController(final ExtendedErrorAttributes errorAttributes) {
        return new ErrorController(errorAttributes, this.serverProperties.getError(), this.errorViewResolvers);
    }
}
