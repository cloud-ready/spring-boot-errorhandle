package top.infra.localization;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhuowan on 2018/3/30 16:15.
 * Description:
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocalizedMessageBundle {

    @AliasFor("locale") MessageLocale value() default MessageLocale.US;

    @AliasFor("value") MessageLocale locale() default MessageLocale.US;
}
