package top.infra.localization;

import lombok.Getter;

import java.util.Locale;

public enum MessageLocale {

    FORCED(Locale.US), // Forced locale, overrides the locale user set

    CN(Locale.SIMPLIFIED_CHINESE), // China

    ID(new Locale("in", "ID")), // Indonesia

    US(Locale.US), // United States

    DEFAULT(Locale.US); // default locale

    @Getter
    private final Locale value;

    MessageLocale(final Locale value) {
        this.value = value;
    }

    public static MessageLocale of(final String name) {
        try {
            return MessageLocale.valueOf(name);
        } catch (final Exception ignore) {
        }
        return US;
    }
}
