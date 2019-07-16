package top.infra.common;

import static org.apache.commons.lang3.StringUtils.isBlank;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by zhanghaolun on 16/11/16.
 */
@Slf4j
public abstract class RequestUtlis {

    private RequestUtlis() {
    }

    @SneakyThrows
    public static String requestBody(final HttpServletRequest request) {
        final Charset charset = RequestUtlis.requestCharacterEncoding(request);
        try {
            final String result;
            // read raw inputStream first. (may be has not been read, for example 404)
            final String raw = StreamUtils.copyToString(request.getInputStream(), charset);
            if (isBlank(raw)) { // if no content in raw inputStream then it should has been readed, try to read cached.
                final ContentCachingRequestWrapper wrapper = findRequestWrapper(request, ContentCachingRequestWrapper.class);
                if (wrapper != null) {
                    final byte[] content = ((ContentCachingRequestWrapper) request).getContentAsByteArray();
                    result = new String(content, charset);
                } else {
                    result = "";
                }
            } else {
                result = raw;
            }
            return result;
        } catch (final IOException ex) {
            log.warn("error reading request body.", ex);
        }
        return "";
    }

    public static Charset requestCharacterEncoding(final HttpServletRequest request) {
        final Charset charset;

        final String characterEncoding = request.getCharacterEncoding();
        if (isBlank(characterEncoding)) {
            charset = StandardCharsets.UTF_8;
        } else {
            charset = Charset.forName(characterEncoding);
        }

        return charset;
    }

    @SuppressWarnings("unchecked")
    public static <T extends HttpServletRequestWrapper> T findRequestWrapper(final ServletRequest request, final Class<T> type) {
        final T result;
        if (request != null) {
            if (type.isAssignableFrom(request.getClass())) {
                result = (T) request;
            } else {
                if (HttpServletRequestWrapper.class.isAssignableFrom(request.getClass())) {
                    return findRequestWrapper(((HttpServletRequestWrapper) request).getRequest(), type);
                } else {
                    result = null;
                }
            }
        } else {
            result = null;
        }
        return result;
    }
}
