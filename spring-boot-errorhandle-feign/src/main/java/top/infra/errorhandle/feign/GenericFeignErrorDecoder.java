package top.infra.errorhandle.feign;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.toEncodedString;
import static top.infra.errorhandle.DefaultXmlStringUnmarshaller.XML_TAG_STRING_BEGIN;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import feign.Response;
import feign.codec.DecodeException;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import top.infra.errorhandle.DefaultXmlStringUnmarshaller;
import top.infra.errorhandle.XmStringlUnmarshaller;
import top.infra.errorhandle.api.ResolvedError;
import top.infra.errorhandle.api.ResolvedErrorException;

@Slf4j
public class GenericFeignErrorDecoder implements feign.codec.ErrorDecoder {

    private final feign.codec.ErrorDecoder delegate;

    private ObjectMapper objectMapper;

    /**
     * If XmlHttpMessageConverter(using XStreamMarshaller) is configured in downstream service,
     * data may be wrapped in&lt;string&gt;&lt;/string&gt;.
     */
    private XmStringlUnmarshaller xmStringlUnmarshaller = DefaultXmlStringUnmarshaller.INSTANCE;

    public GenericFeignErrorDecoder() {
        this.delegate = new Default();
    }

    @Autowired(required = false)
    public void setXmStringlUnmarshaller(final XmStringlUnmarshaller unmarshal) {
        this.xmStringlUnmarshaller = unmarshal;
    }

    @Autowired
    public void setObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(final String methodKey, final feign.Response response) {
        if (log.isDebugEnabled()) {
            log.debug("decode : {}", methodKey);
        }

        final Exception result;

        try {
            final HttpHeaders headers = toHttpHeaders(response.headers());
            if (ResolvedErrorException.isResolvedError(headers)) {
                result = this.decodeResolvedError(methodKey, response, headers);
            } else {
                result = this.decodeUnresolvedError(methodKey, response, headers);
            }
        } catch (final DecodeException cause) {
            throw cause;
        } catch (final Exception cause) {
            final String message = "Failed to decode response.";
            log.info(message, cause);
            throw new DecodeException(message, cause);
        }

        return result;
    }

    @SuppressWarnings("unused")
    ResolvedErrorException decodeResolvedError(final String methodKey, final feign.Response response, final HttpHeaders headers) {
        final ResolvedErrorException result;

        final String bodyString = bodyString(response, headers);
        try {
            final String json = bodyString.startsWith(XML_TAG_STRING_BEGIN) ? this.xmStringlUnmarshaller.unmarshal(bodyString) : bodyString;
            final ResolvedError resolvedError = this.objectMapper.readValue(json, ResolvedError.class);
            result = new ResolvedErrorException(resolvedError);
        } catch (final DecodeException cause) {
            throw cause;
        } catch (final Exception cause) {
            final String message = "Failed to decode resolvedError. responseBody: " + bodyString;
            log.info(message, cause);
            throw new DecodeException(message, cause);
        }

        return result;
    }

    Exception decodeUnresolvedError(final String methodKey, final feign.Response response, final HttpHeaders headers) {
        final Exception result;

        if (response.status() >= 400 && response.status() <= 499) {
            result = clientError(response, headers);
        } else if (response.status() >= 500 && response.status() <= 599) {
            result = serverError(response, headers);
        } else {
            result = this.delegate.decode(methodKey, response);
        }

        return result;
    }

    static HttpClientErrorException clientError(final Response response, final HttpHeaders headers) {
        final HttpStatus statusCode = HttpStatus.valueOf(response.status());
        final String statusText = response.reason(); // Nullable and not set when using http/2
        final byte[] responseBody = bodyBytes(response.body());
        return new HttpClientErrorException( //
            statusCode, //
            statusText, //
            headers, //
            responseBody, //
            null);
    }

    static HttpServerErrorException serverError(final Response response, final HttpHeaders headers) {
        final HttpStatus statusCode = HttpStatus.valueOf(response.status());
        final String statusText = response.reason(); // Nullable and not set when using http/2
        final byte[] responseBody = bodyBytes(response.body());
        return new HttpServerErrorException( //
            statusCode, //
            statusText, //
            headers, //
            responseBody, //
            null);
    }

    static String bodyString(final Response response, final HttpHeaders headers) {
        final byte[] bodyBytes = bodyBytes(response.body());
        final Charset contentCharset = getContentCharset(headers).orElse(null);
        return toEncodedString(bodyBytes, contentCharset != null ? contentCharset : UTF_8);
    }

    static byte[] bodyBytes(final feign.Response.Body body) {
        try {
            return StreamUtils.copyToByteArray(body.asInputStream());
        } catch (final Exception cause) {
            final String message = "Failed to decode response body.";
            log.info(message, cause);
            throw new DecodeException(message, cause);
        }
    }

    static HttpHeaders toHttpHeaders(final Map<String, Collection<String>> headers) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        headers.forEach((key, value) -> responseHeaders.put(key, newArrayList(value)));
        return responseHeaders;
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    static Optional<Charset> getContentCharset(final HttpHeaders headers) {
        final Optional<Charset> result;
        if (headers != null && headers.getContentType() != null) {
            return Optional.ofNullable(headers.getContentType().getCharset());
        } else {
            result = Optional.empty();
        }
        return result;
    }
}
