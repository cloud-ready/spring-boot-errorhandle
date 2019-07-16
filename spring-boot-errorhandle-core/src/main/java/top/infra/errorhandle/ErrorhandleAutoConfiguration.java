package top.infra.errorhandle;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import top.infra.boot.mvc.WebApplicationAutoConfiguration;
import top.infra.errorhandle.internal.DefaultStackTraceIndicator;

@AutoConfigureAfter({WebApplicationAutoConfiguration.class})
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class, WebMvcAutoConfiguration.class, SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"top.infra.errorhandle.internal"})
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication
@Configuration
@EnableConfigurationProperties(value = {ErrorProperties.class, DefaultStackTraceIndicator.class})
public class ErrorhandleAutoConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * see: {@link WebMvcConfigurationSupport#handlerExceptionResolver()}.
     *
     * @param resolvers resolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(final List<HandlerExceptionResolver> resolvers) {
        // call this addDefaultHandlerExceptionResolvers ?
    }

    @Deprecated
    protected final void addDefaultHandlerExceptionResolvers(final List<HandlerExceptionResolver> resolvers) {

    }

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
