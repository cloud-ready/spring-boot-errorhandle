package top.infra.common;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

import commonscodec.URLCodec;

/**
 * Created by zhanghaolun on 16/11/16.
 */
public abstract class CodecUtils {

    private static final URLCodec URL_CODEC = new URLCodec(StandardCharsets.UTF_8.name());

    private CodecUtils() {
    }

    /**
     * other ways to do this.
     * <pre>
     * {@code
     * new org.apache.commons.codec.binary.Base64().encode,encodeToString
     * io.jsonwebtoken.impl.TextCodec.BASE64.encode
     * com.fasterxml.jackson.core.Base64Variants.MIME_NO_LINEFEEDS.encode()
     * new String(org.apache.commons.codec.binary.Base64.encodeBase64(raw, UTF_8)
     * }
     * </pre>
     *
     * @param raw raw bytes
     * @return base64 string
     */
    public static String encodeBase64(final byte[] raw) {
        return java.util.Base64.getEncoder().encodeToString(raw);
    }

    /**
     * other ways to do this.
     * <pre>
     * {@code
     * org.springframework.security.crypto.codec.Base64.decode(base64String.getBytes(UTF_8.name()));
     * new sun.misc.BASE64Decoder().decodeBuffer(base64String);
     * org.apache.commons.codec.binary.Base64.decodeBase64(base64String);
     * }
     * </pre>
     *
     * @param base64String base64 string
     * @return raw bytes
     */
    @SneakyThrows
    public static byte[] decodeBase64(final String base64String) {
        return java.util.Base64.getDecoder().decode(base64String);
    }

    /**
     * other ways to do this.
     * <pre>
     * {@code
     * java.net.URLEncoder.encode(text, UTF_8.name())
     * }
     * </pre>
     *
     * @param text text to encode
     * @return encoded text
     */
    @SneakyThrows
    public static String urlEncode(final String text) {
        return text != null ? URL_CODEC.encode(text) : null;
    }

    @SneakyThrows
    public static String urlDecode(final String text) {
        return text != null ? URL_CODEC.decode(text) : null;
    }
}
