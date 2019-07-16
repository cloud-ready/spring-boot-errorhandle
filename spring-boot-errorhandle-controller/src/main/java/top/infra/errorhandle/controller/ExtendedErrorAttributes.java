package top.infra.errorhandle.controller;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import top.infra.errorhandle.api.ExceptionResolver;
import top.infra.errorhandle.api.ResolvedError;
import top.infra.errorhandle.api.ResolvedErrorException;

/**
 * {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes}
 *
 * <p>
 * Created by zhanghaolun on 16/8/9.
 * </p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExtendedErrorAttributes //
  implements ErrorAttributes, HandlerExceptionResolver, Ordered {

  private static final String ERROR_ATTRIBUTE = ExtendedErrorAttributes.class.getName() + ".ERROR";

  @Autowired
  @Setter
  private ExceptionResolver<Throwable> resolver;

  @Override
  public Map<String, Object> getErrorAttributes( //
      final WebRequest webRequest, //
      final boolean includeStackTrace //
  ) {
    final Throwable error = getError(webRequest);
    final ResolvedError resolved = ResolvedErrorException.isResolvedError(error) ? //
        ((ResolvedErrorException) error).getError() : //
        this.resolver.resolve(webRequest, error);
    return resolved != null ? resolved.toErrorAttributes() : ImmutableMap.of();
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  @Override
  public ModelAndView resolveException( //
    final HttpServletRequest request, //
    final HttpServletResponse response, //
    final Object handler, //
    final Exception ex //
  ) {
    final ResolvedError resolved = this.resolver.resolve(request, ex);
    this.storeErrorAttributes(request, resolved != null ? new ResolvedErrorException(resolved) : ex);
    return null;
  }

  private void storeErrorAttributes(final HttpServletRequest request, final Exception ex) {
    request.setAttribute(ERROR_ATTRIBUTE, ex);
  }

  @Override
  public Throwable getError(WebRequest webRequest) {
    final Throwable found = getAttribute(webRequest, ERROR_ATTRIBUTE);
    return found != null ? found : getAttribute(webRequest, "javax.servlet.error.exception");
  }

  @SuppressWarnings("unchecked")
  private <T> T getAttribute(final RequestAttributes requestAttributes, final String name) {
    return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
  }
}
