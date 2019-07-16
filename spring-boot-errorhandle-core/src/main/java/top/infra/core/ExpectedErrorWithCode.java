package top.infra.core;

/**
 * ExpectedErrorWithCode is thrown by user, not by libraries or frameworks,
 * with an error code to identify error more clearly.
 */
public class ExpectedErrorWithCode extends ExpectedError {

    private final ErrorCode code;

    /**
     * Constructs a new expected exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     *
     * @param code the detail error code and message (which is saved for later retrieval
     *             by the {@link #getCode()} and {@link #getMessage()} method).
     */
    public ExpectedErrorWithCode(final ErrorCode code) {
        super(code != null ? code.getMessage().getText() : "");
        this.code = code;
    }

    /**
     * Constructs a new expected exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this expected exception's detail message.
     *
     * @param code  the detail error code and message (which is saved for later retrieval
     *              by the {@link #getCode()} and {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public ExpectedErrorWithCode(final ErrorCode code, final Throwable cause) {
        super(code != null ? code.getMessage().getText() : "", cause);
        this.code = code;
    }

    /**
     * Constructs a new expected exception with the specified detail
     * message, cause, suppression enabled or disabled, and writable
     * stack trace enabled or disabled.
     *
     * @param code               the detail error code and message (which is saved for later retrieval
     *                           by the {@link #getCode()} and {@link #getMessage()} method).
     * @param cause              the cause.  (A {@code null} value is permitted,
     *                           and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     */
    protected ExpectedErrorWithCode( //
        final ErrorCode code, //
        final Throwable cause, //
        boolean enableSuppression, //
        boolean writableStackTrace //
    ) {
        super(code != null ? code.getMessage().getText() : "", cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public ErrorCode getCode() {
        return this.code;
    }
}
