package top.infra.errorhandle;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.NEVER;
import static top.infra.errorhandle.SearchStrategy.HIERARCHY_FIRST;

import lombok.Data;

import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <p>
 * Configuration properties for spring-boot applications.
 * </p>
 * Created by zhanghaolun on 16/8/17.
 */
@ConfigurationProperties(prefix = "server.error")
@SuppressWarnings({"PMD.ImmutableField", "PMD.SingularField", "PMD.UnusedPrivateField"})
@Data
public class ErrorProperties {

    @Data
    public static class Controller {

        private Boolean enabled = TRUE;
    }


    /**
     * Experimental.
     */
    @Data
    public static class Handler {

        private Boolean enabled = FALSE;
    }

    @Data
    public static class Message {

        /**
         * ORDER_FIRST, HIERARCHY_FIRST.
         */
        private SearchStrategy searchStrategy = HIERARCHY_FIRST;
    }

    @NestedConfigurationProperty
    private Controller controller = new Controller();

    @NestedConfigurationProperty
    private Handler handler = new Handler();

    private IncludeStacktrace includeStacktrace = NEVER;

    @NestedConfigurationProperty
    private Message message = new Message();
}
