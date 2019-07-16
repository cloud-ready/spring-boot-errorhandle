package top.infra.boot.mvc.internal;

import static lombok.AccessLevel.PACKAGE;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.server.Session;

import top.infra.boot.mvc.api.DomainResolver;

/**
 * Created by zhanghaolun on 16/8/18.
 */
@Getter
@Setter(PACKAGE)
public class DefaultDomainResolver implements DomainResolver {

    private String domain;

    @Autowired
    public DefaultDomainResolver(final ServerProperties serverProperties) {
        final Session.Cookie sessionCookie = serverProperties.getServlet().getSession().getCookie();
        this.domain = sessionCookie.getDomain();
    }

    public DefaultDomainResolver(final String domain) {
        this.domain = domain != null ? domain : "";
    }

    @Override
    public String resolveDomain(final HttpServletRequest request) {
        final String domain;

        final String requestServerName = request.getServerName();
        if (this.domain.equals(requestServerName)) { // || notProductionEnvironment
            // domain = request.getServerPort() != 80 ? serverName + ":" + request.getServerPort() :
            domain = ""; // use current domain in cookie
        } else {
            domain = this.domain;
        }

        return domain;
    }
}
