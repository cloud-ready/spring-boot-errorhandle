package top.infra.boot.autoconfigure;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created on 16/9/26.
 *
 * @author Yuliang Jin
 */
@Slf4j
public class OnNotProductionEnvCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome( //
        final ConditionContext context, //
        final AnnotatedTypeMetadata metadata //
    ) {
        final ConditionOutcome outcome;

        final MultiValueMap<String, Object> allAnnotationAttributes = allAnnotationAttributes(metadata);
        final String environment = getEnvironment(context.getEnvironment());
        final Boolean isProductionEnvironment = isProductionEnvironment(context.getEnvironment());
        if (allAnnotationAttributes != null && isProductionEnvironment) {
            outcome = ConditionOutcome.noMatch("Environment: " + environment + " not match, is production environment.");
        } else {
            outcome = ConditionOutcome.match();
        }

        return outcome;
    }

    static MultiValueMap<String, Object> allAnnotationAttributes(final AnnotatedTypeMetadata metadata) {
        return metadata.getAllAnnotationAttributes(ConditionalOnNotProductionEnv.class.getName(), true);
    }

    public static Boolean isProductionEnvironment(final Environment environment) {
        return isProductionEnvironment(getEnvironment(environment));
    }

    public static Boolean isProductionEnvironment(final String environment) {
        return isNotBlank(environment) && (environment.startsWith("prod") || environment.endsWith("prod"));
    }

    public static String getEnvironment(final Environment environment) {
        final Optional<String> envOptional = Arrays.stream(environment.getActiveProfiles())
            .filter(profile -> profile.endsWith(".env"))
            .findFirst();
        return envOptional.orElse("unknown");
    }
}
