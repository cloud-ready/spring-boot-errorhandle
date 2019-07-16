package top.infra.localization;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static top.infra.localization.BuildInErrorMessages.MESSAGE_AUTHENTICATION_FAILED;
import static top.infra.localization.BuildInErrorMessages.MESSAGE_AUTHORIZATION_FAILED;
import static top.infra.localization.BuildInErrorMessages.MESSAGE_ILLEGAL_REQUEST;
import static top.infra.localization.BuildInErrorMessages.MESSAGE_INVALID_TOKEN;
import static top.infra.localization.BuildInErrorMessages.MESSAGE_OK;
import static top.infra.localization.BuildInErrorMessages.MESSAGE_RATE_LIMITATION_REACHED;
import static top.infra.localization.BuildInErrorMessages.MESSAGE_SERVER_ERROR;

import top.infra.core.ErrorCode;
import top.infra.core.ErrorMessage;

/**
 * Created by zhuowan on 2016/11/20 22:56.
 * Description:
 */
public enum BuildInErrorCodes implements ErrorCode {

    CODE_AUTHENTICATION_FAILED(MESSAGE_AUTHENTICATION_FAILED, UNAUTHORIZED.value()), //
    CODE_AUTHORIZATION_FAILED(MESSAGE_AUTHORIZATION_FAILED, FORBIDDEN.value()), //
    CODE_ILLEGAL_REQUEST(MESSAGE_ILLEGAL_REQUEST, BAD_REQUEST.value()), //
    CODE_INVALID_TOKEN(MESSAGE_INVALID_TOKEN, UNAUTHORIZED.value()), //
    CODE_OK(MESSAGE_OK, OK.value()), //
    CODE_RATE_LIMITATION_REACHED(MESSAGE_RATE_LIMITATION_REACHED, TOO_MANY_REQUESTS.value()), //
    CODE_SERVER_ERROR(MESSAGE_SERVER_ERROR, INTERNAL_SERVER_ERROR.value());

    private final ErrorMessage message;
    private final int statusValue;

    BuildInErrorCodes(final ErrorMessage message, final int statusValue) {
        this.message = message;
        this.statusValue = statusValue;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getStatusValue() {
        return this.statusValue;
    }

    @Override
    public ErrorMessage getMessage() {
        return this.message;
    }
}
