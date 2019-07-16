package top.infra.boot.mvc.internal;

import static lombok.AccessLevel.PACKAGE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

import top.infra.boot.mvc.api.RequestResolver;

/**
 * Created by zhanghaolun on 16/8/18.
 */
@Getter
@Setter(PACKAGE)
@Slf4j
public class DefaultRequestResolver implements RequestResolver {

    /**
     * TODO improve this.
     */
    @Override
    public Boolean isAjaxRequest(final HttpServletRequest request) {
        final HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        // final String contentTypeHeader = request.getHeader(CONTENT_TYPE);
        // final String content = isNotBlank(contentTypeHeader) ? contentTypeHeader : "";
        final String acceptHeader = request.getHeader(ACCEPT);
        final String accept = isNotBlank(acceptHeader) ? acceptHeader : "";
        final String underscore = request.getParameter("_");
        final boolean result = POST == httpMethod || PUT == httpMethod || DELETE == httpMethod //
            || accept.contains(APPLICATION_JSON_VALUE) //
            || isNotBlank(underscore);
        return result;
    }
}
