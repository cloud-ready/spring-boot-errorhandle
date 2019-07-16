package commonscodec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringUtils {

    /**
     * Calls {@link String#getBytes(Charset)}
     *
     * @param string
     *            The string to encode (if null, return null).
     * @param charset
     *            The {@link Charset} to encode the <code>String</code>
     * @return the encoded bytes
     */
    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    /**
     * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
     * array.
     *
     * @param string
     *            the String to encode, may be <code>null</code>
     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
     * @throws NullPointerException
     *             Thrown if Charsets#US_ASCII is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     */
    public static byte[] getBytesUsAscii(final String string) {
        return getBytes(string, StandardCharsets.US_ASCII);
    }

    /**
     * Constructs a new <code>String</code> by decoding the specified array of bytes using the US-ASCII charset.
     *
     * @param bytes
     *            The bytes to be decoded into characters
     * @return A new <code>String</code> decoded from the specified array of bytes using the US-ASCII charset,
     *         or {@code null} if the input byte array was {@code null}.
     * @throws NullPointerException
     *             Thrown if Charsets#US_ASCII is not initialized, which should never happen since it is
     *             required by the Java platform specification.
     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
     */
    public static String newStringUsAscii(final byte[] bytes) {
        return new String(bytes, StandardCharsets.US_ASCII);
    }
}
