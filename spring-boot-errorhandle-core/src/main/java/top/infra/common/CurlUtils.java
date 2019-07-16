package top.infra.common;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.parseMediaType;
import static top.infra.common.RequestUtlis.requestBody;

import com.google.common.collect.ImmutableSet;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;

import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * Build (Generate) curl command from request.
 *
 * <p>
 * Created by zhanghaolun on 16/7/4.
 * </p>
 */
@Slf4j
public abstract class CurlUtils {

    private static final Collection<String> RETAIN_HEADERS = ImmutableSet.copyOf(newArrayList(ACCEPT, CONTENT_TYPE, AUTHORIZATION));

    private CurlUtils() {
    }

    /**
     * Generate curl command from request.
     *
     * @param request {@link HttpServletRequest}
     * @return curl command
     */
    public static String curl(final HttpServletRequest request) {
        final String result;
        if (request == null) {
            result = "";
        } else {
            final String headers = curlHeaders(request);
            final String method = curlMethod(request);

            final StringBuilder curl = new StringBuilder("curl ")
                .append(headers)
                .append(method)
                .append(request.getRequestURL())
                .append(" ")
                .append(curlData(request));

            result = curl.toString();
        }
        return result;
    }

    static String curlData(final HttpServletRequest request) {
        final MediaType contentType = isNotBlank(request.getContentType()) ? parseMediaType(request.getContentType()) : ALL;
        final String parameters = curlParameters(request);

        final StringBuilder sb = new StringBuilder();
        if (APPLICATION_JSON.includes(contentType) || APPLICATION_XML.includes(contentType)) {
            final String body = requestBody(request);
            sb.append("--data '").append(body).append("' ");
        } else if (contentType == APPLICATION_FORM_URLENCODED) {
            sb.append("--data '").append(parameters).append("' ");
        } else if (isNotBlank(parameters)) { // params in url
            sb.append('?').append(parameters).append(' ');
        }
        return sb.toString();
    }

    static String curlHeaders(final HttpServletRequest request) {
        final Enumeration<String> headerNames = request.getHeaderNames();
        final StringBuilder sb = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            final String name = headerNames.nextElement();
            final String value = request.getHeader(name);
            if (RETAIN_HEADERS.contains(name)) {
                sb.append("-H '").append(name).append(": ").append(value).append("' ");
            }
        }
        return sb.toString();
    }

    static String curlMethod(final HttpServletRequest request) {
        return "-X " + request.getMethod() + " ";
    }

    @SneakyThrows
    static String curlParameters(final HttpServletRequest request) {
        final Enumeration<String> parameterNames = request.getParameterNames();
        final StringBuilder sb = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            final String value = request.getParameter(name);
            sb //
                .append('&') //
                .append(name) //
                .append('=') //
                .append(CodecUtils.urlEncode(value));
        }
        return sb.length() > 0 ? sb.substring(1) : "";
    }
}
