package top.infra.errorhandle.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import top.infra.errorhandle.api.ResolvedError;
import top.infra.errorhandle.api.StackTraceIndicator;

/**
 * Override default {@link BasicErrorController}.
 *
 * Basic global error {@link Controller}, rendering {@link ResolvedError}. More specific
 * errors can be handled either using Spring MVC abstractions (e.g.
 * {@code @ExceptionHandler}) or by adding servlet
 *
 * Set container error pages by AbstractEmbeddedServletContainerFactory#setErrorPages is deprecated in spring-boot2
 *
 * {@link BasicErrorController}
 *
 * <p>
 * see: {@link ErrorMvcAutoConfiguration}, {@link BasicErrorController}
 * </p>
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    @Autowired
    @Setter
    private StackTraceIndicator traceIndicator;

    /**
     * Create a new {@link BasicErrorController} instance.
     *
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     */
    public ErrorController(final ErrorAttributes errorAttributes, final ErrorProperties errorProperties) {
        this(errorAttributes, errorProperties, Collections.emptyList());
    }

    /**
     * Create a new {@link BasicErrorController} instance.
     *
     * @param errorAttributes    the error attributes
     * @param errorProperties    configuration properties
     * @param errorViewResolvers error view resolvers
     */
    public ErrorController(final ErrorAttributes errorAttributes, final ErrorProperties errorProperties,
        final List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(final HttpServletRequest request, final HttpServletResponse response) {
        // goto WhitelabelErrorViewConfiguration.defaultErrorView if not provided.
        final ResolvedError resolvedError = this.resolvedError(request, MediaType.TEXT_HTML);

        final HttpStatus status = resolvedError.getHttpStatus();
        //final HttpStatus status = getStatus(request);
        final Map<String, Object> model = resolvedError.toErrorAttributes();
        //final Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
        //    request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        final ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<ResolvedError> error(final HttpServletRequest request, final HttpServletResponse response) {
        final ResolvedError body = this.resolvedError(request, MediaType.ALL);
        final HttpStatus status = body.getHttpStatus();
        body.copyHeadersTo(response);
        //final Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        //HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);
    }

    protected boolean isIncludeStackTrace(final HttpServletRequest request, final MediaType produces) {
        return this.traceIndicator.stackTrace(request, null);
    }

    /**
     * Provide access to the error properties.
     *
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

    private ResolvedError resolvedError(final HttpServletRequest request, final MediaType produces) {
        final Boolean includeStackTrace = this.isIncludeStackTrace(request, produces);
        final Map<String, Object> errorAttributes = this.getErrorAttributes(request, includeStackTrace);
        return ResolvedError.fromErrorAttributes(errorAttributes);
    }
}
