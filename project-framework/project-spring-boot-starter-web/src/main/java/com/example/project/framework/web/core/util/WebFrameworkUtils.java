package com.example.project.framework.web.core.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.example.project.framework.common.enums.TerminalEnum;
import com.example.project.framework.common.enums.UserTypeEnum;
import com.example.project.framework.web.config.ProjectWebProperties;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 专属于 web 包的工具类
 */
public class WebFrameworkUtils {

    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_ID = "login_user_id";
    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_TYPE = "login_user_type";

    private static final String REQUEST_ATTRIBUTE_COMMON_RESULT = "common_result";

    public static final String HEADER_TENANT_ID = "tenant-id";
    public static final String HEADER_VISIT_TENANT_ID = "visit-tenant-id";
    /**
     * 终端的 Header
     *
     * @see TerminalEnum
     */
    public static final String HEADER_TERMINAL = "terminal";

    /**
     * 获得租户编号
     * <p>
     * 租户 ID 存储在请求头的 {@code tenant-id} 字段中
     *
     * @param request 请求
     * @return 租户编号
     */
    public static Long getTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        return NumberUtil.isNumber(tenantId) ? Long.valueOf(tenantId) : null;
    }

    /**
     * 获得访问的租户编号
     * <p>
     * 访问租户 ID 存储在请求头的 {@code visit-tenant-id} 字段中
     *
     * @param request 请求
     * @return 访问的租户编号
     */
    public static Long getVisitTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(HEADER_VISIT_TENANT_ID);
        return NumberUtil.isNumber(tenantId) ? Long.valueOf(tenantId) : null;
    }


    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    public static Long getLoginUserId() {
        HttpServletRequest request = getRequest();
        return getLoginUserId(request);
    }

    /**
     * 获得登录用户编号
     * 注意：该方法仅限于 framework 框架使用！！！
     *
     * @param request 请求
     * @return 登录用户编号
     */
    public static Long getLoginUserId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Long) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID);
    }

    public static Integer getLoginUserType() {
        HttpServletRequest request = getRequest();
        return getLoginUserType(request);
    }

    /**
     * 获得登录用户类型
     * <p>
     * 该方法会基于 {@link #REQUEST_ATTRIBUTE_LOGIN_USER_TYPE} 属性获取，如果没有设置，则会基于 URL 前缀进行推断。
     * <p>
     * 注意：该方法仅限于 framework 框架使用！！！
     *
     * @param request 请求
     * @return 登录用户类型
     * @see UserTypeEnum
     */
    public static Integer getLoginUserType(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 1、从 request attribute 中获取
        Integer userType = (Integer) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE);
        if (userType != null) {
            return userType;
        }
        // 2、基于 URL 前缀获取约定的用户类型
        final ProjectWebProperties properties = SpringUtil.getBean(ProjectWebProperties.class);
        boolean isAdmin = request.getRequestURI().startsWith(properties.getAdminApi().getPrefix());
        if (isAdmin) {
            return UserTypeEnum.ADMIN.getValue();
        }
        boolean isApp = request.getRequestURI().startsWith(properties.getAppApi().getPrefix());
        if (isApp) {
            return UserTypeEnum.MEMBER.getValue();
        }
        // 3、无法获取
        return null;
    }

    /**
     * 设置登录用户编号
     *
     * @param request     请求
     * @param loginUserId 登录用户编号
     */
    public static void setLoginUserId(ServletRequest request, String loginUserId) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID, loginUserId);
    }

    /**
     * 设置登录用户类型
     *
     * @param request       请求
     * @param loginUserType 登录用户类型
     */
    public static void setLoginUserType(ServletRequest request, Integer loginUserType) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_TYPE, loginUserType);
    }

    /**
     * 获得终端类型
     *
     * @return 终端类型
     * @see TerminalEnum
     */
    public static Integer getTerminal() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return TerminalEnum.UNKNOWN.getTerminal();
        }
        String terminalValue = request.getHeader(HEADER_TERMINAL);
        return NumberUtil.parseInt(terminalValue, TerminalEnum.UNKNOWN.getTerminal());
    }
}
