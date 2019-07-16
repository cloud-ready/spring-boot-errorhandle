package top.infra.localization;

import org.reflections.Reflections;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import top.infra.core.ErrorMessage;

/**
 * Created by zhuowan on 2018/3/30 18:04.
 * Description:
 */
public class LocalizedMessages {

    private transient static final Map<MessageLocale, Map<String, String>> messageCache;
    private static boolean initialized = false;

    static {
        messageCache = new HashMap<>();
        init();
    }

    /**
     * 1. 第一步加载所有的locales及所有的messages
     * 2. 获取message优先级：
     * priority 1: Forced 强制加载的类型message
     * priority 2: 指定类型的message类型
     * priority 3: 默认的message类型 （目前把US当作默认类型）
     * priority 4: 抛出异常
     *
     * @param message ErrorMessage
     * @param locale  Locale
     * @return localized message
     */
    public static String getMessageByLocale(final ErrorMessage message, final Locale locale) {
        String resultMsg = getMessageByLocaleAndMessage(message, getLocale(locale));
        if (resultMsg != null) {
            return resultMsg;
        }
        throw new RuntimeException("Invalid common locale message usage");
    }

    /**
     * Initialize all messages
     */
    private static void init() {
        if (!initialized) {
            final Reflections reflections = new Reflections();
            final Set<Class<?>> bundles = reflections.getTypesAnnotatedWith(LocalizedMessageBundle.class);
            for (final MessageLocale locale : MessageLocale.values()) { // all locales
                final Map<String, String> enumConstants = new HashMap<>();
                for (final Class bundle : bundles) {
                    final LocalizedMessageBundle annotation = (LocalizedMessageBundle) bundle.getAnnotation(LocalizedMessageBundle.class);
                    final MessageLocale bundleLocale = annotation.value();
                    if (locale == bundleLocale) {
                        arrayToMap(enumConstants, bundle.getEnumConstants());
                    }
                }
                messageCache.putIfAbsent(locale, enumConstants);
            }
            initialized = true;
        }
    }

    private static void arrayToMap(final Map<String, String> map, final Object[] enums) {
        if (map != null && enums != null) {
            for (final Object enu : enums) {
                final ErrorMessage message = (ErrorMessage) enu;
                map.putIfAbsent(message.getName(), message.getText());
            }
        }
    }

    private static MessageLocale getLocale(final Locale locale) {
        final String country = locale != null ? locale.getCountry() : LocaleContextHolder.getLocale().getCountry();
        return MessageLocale.of(country);
    }

    private static String getMessageByLocaleAndMessage(final ErrorMessage message, final MessageLocale messageLocale) {
        //priority 1 Forced locale Message
        String finalMessage = messageCache.get(MessageLocale.FORCED).get(message.getName());
        if (finalMessage == null) {
            //priority 2 Specific locale message
            finalMessage = messageCache.get(messageLocale).get(message.getName());
            if (finalMessage == null) {
                //priority 3 Default message
                finalMessage = messageCache.get(MessageLocale.DEFAULT).get(message.getName());
            }
        }
        return finalMessage;
    }
}
