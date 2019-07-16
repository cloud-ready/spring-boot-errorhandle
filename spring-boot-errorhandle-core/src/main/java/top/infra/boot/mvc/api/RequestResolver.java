package top.infra.boot.mvc.api;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhanghaolun on 16/8/18.
 */
public interface RequestResolver {

  Boolean isAjaxRequest(HttpServletRequest request);
}
