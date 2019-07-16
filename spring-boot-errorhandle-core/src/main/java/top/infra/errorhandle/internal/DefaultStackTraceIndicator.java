package top.infra.errorhandle.internal;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM;

import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

import top.infra.errorhandle.api.StackTraceIndicator;

/**
 * Created by zhanghaolun on 16/7/1.
 */
public class DefaultStackTraceIndicator implements StackTraceIndicator {

    private IncludeStacktrace includeStacktrace;

    public DefaultStackTraceIndicator(final IncludeStacktrace includeStacktrace) {
        this.includeStacktrace = includeStacktrace;
    }

    @Override
    public Boolean stackTrace(final HttpServletRequest request, final MediaType produces) {
        final Boolean result;

        if (ON_TRACE_PARAM == includeStacktrace) {
            result = request != null && request.getParameterMap().containsKey("trace");
        } else if (ALWAYS == includeStacktrace) {
            result = TRUE;
        } else { // NEVER or null
            result = FALSE;
        }

        return result;
    }

    public void setIncludeStacktrace(final IncludeStacktrace includeStacktrace) {
        this.includeStacktrace = includeStacktrace;
    }
}
