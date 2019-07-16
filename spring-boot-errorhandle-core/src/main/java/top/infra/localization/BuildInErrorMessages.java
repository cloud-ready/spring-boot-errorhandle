package top.infra.localization;

import top.infra.core.ErrorMessage;

/**
 * Created by zhuowan on 2018/3/24 17:12.
 * Description:
 */
@LocalizedMessageBundle(MessageLocale.DEFAULT)
public enum BuildInErrorMessages implements ErrorMessage {

    MESSAGE_AUTHENTICATION_FAILED("Authentication failed"), //
    MESSAGE_AUTHORIZATION_FAILED("Authorization failed"), //
    MESSAGE_ILLEGAL_REQUEST("Parameter error, please check your request whether has illegal parameters"), //
    MESSAGE_INVALID_TOKEN("Invalid or expired token"), //
    MESSAGE_OK("OK"), //
    MESSAGE_RATE_LIMITATION_REACHED("Reached the request rate limitation, please retry later"), //
    MESSAGE_SERVER_ERROR("Server error");

    private String text;

    BuildInErrorMessages(final String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
