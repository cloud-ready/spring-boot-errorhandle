package top.infra.errorhandle.api;

import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.http.HttpServletRequest;

public interface ExceptionResolver<T extends Throwable> {

    ResolvedError resolve(HttpServletRequest request, T throwable);

    ResolvedError resolve(RequestAttributes requestAttributes, T throwable);
}
