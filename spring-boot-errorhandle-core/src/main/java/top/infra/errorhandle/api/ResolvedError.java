package top.infra.errorhandle.api;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * Resolved error.
 *
 * <p>
 * Created by zhanghaolun on 16/7/1.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder(builderMethodName = "resolvedErrorBuilder")
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(of = {"error", "exception", "path", "status", "timestamp", "localizedMessage"})
@Setter(PRIVATE)
@Data
@Slf4j
@SuppressFBWarnings("EI_EXPOSE_REP")
public class ResolvedError {

    public static final String RESOLVED_ERROR_COOKIE = "resolvedErrorCookie";
    static final String HEADER_RESOLVED_ERROR = "RESOLVED-ERROR";

    // ------------------------------ basic ------------------------------
    private String error;
    private String exception;
    private String message;
    private String path;
    private Integer status;
    private Long timestamp;
    private String trace;

    private ValidationError[] validationErrors;

    // ------------------------------ extended ------------------------------
    /**
     * ISO8601 string.
     */
    private String datetime;

    private HttpHeader[] headers;

    private String localizedMessage;

    private String[] tracks;

    private ResolvedError() {
        this.headers = HttpHeader.fromHttpHeaders(newHttpHeaders());
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        HttpStatus result;
        try {
            result = HttpStatus.valueOf(this.status);
        } catch (final Exception ex) {
            result = HttpStatus.INTERNAL_SERVER_ERROR;
            log.debug("error parse http status {}", this.status, ex);
        }
        return result;
    }

    /**
     * before set into cookie, call this method to avoid header size exceed limit.
     *
     * @return this
     */
    public ResolvedError eraseTraces() {
        this.setTracks(null);
        this.setTrace(null);
        return this;
    }

    public ResolvedError trackPrepend(final String track) {
        this.tracks = this.tracks != null ? //
            ArrayUtils.add(this.tracks, 0, track) : //
            new String[]{track};
        return this;
    }

    public Map<String, Object> toErrorAttributes() {
        return toErrorAttributes(this);
    }

    /**
     * copy resolvedError's headers to response.
     *
     * @param response target
     */
    public void copyHeadersTo(final HttpServletResponse response) {
        if (this.getHeaders() != null) {
            for (final HttpHeader header : this.getHeaders()) {
                for (final String value : header.getValues()) {
                    response.addHeader(header.getName(), value);
                }
            }
        }
    }

    public static HttpHeaders newHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_RESOLVED_ERROR, HEADER_RESOLVED_ERROR);
        return headers;
    }

    public static Map<String, Object> toErrorAttributes(final ResolvedError error) {
        final Map<String, Object> map = newLinkedHashMap();
        // basic
        map.put("error", error.error);
        map.put("exception", error.exception);
        map.put("message", error.message);
        map.put("path", error.path);
        map.put("status", error.status);
        map.put("timestamp", error.timestamp);
        map.put("trace", error.trace);
        map.put("validationErrors", error.validationErrors);
        // extended
        map.put("datetime", error.datetime);
        map.put("headers", error.headers);
        map.put("localizedMessage", error.localizedMessage);
        map.put("tracks", error.tracks);

        map.entrySet().removeIf(e -> e.getValue() == null);
        return map;
    }

    public static ResolvedError fromErrorAttributes(final Map<String, Object> errorAttributes) {
        return errorAttributes == null ? null : ResolvedError.resolvedErrorBuilder()
            // basic
            .error((String) errorAttributes.get("error")) //
            .exception((String) errorAttributes.get("exception"))
            .message((String) errorAttributes.get("message")) //
            .path((String) errorAttributes.get("path")) //
            .status((Integer) errorAttributes.get("status")) //
            .timestamp((Long) errorAttributes.get("timestamp")) //
            .trace((String) errorAttributes.get("trace")) //
            .validationErrors((ValidationError[]) errorAttributes.get("validationErrors")) //
            // extended
            .datetime((String) errorAttributes.get("datetime"))
            .headers((HttpHeader[]) errorAttributes.get("headers")) //
            .localizedMessage((String) errorAttributes.get("localizedMessage")) //
            .tracks((String[]) errorAttributes.get("tracks")).build();
    }
}
