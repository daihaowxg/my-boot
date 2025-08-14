package com.example.project.framework.common.util.servlet;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ServletUtils {

    /**
     * 将 HTTP 请求中的参数转换为键值对映射，其中每个参数值为多个值的逗号拼接字符串。
     *
     * <p>HTTP 请求允许同一个参数名出现多次（例如多选框或重复参数），例如：
     * <pre>
     *   {@code param1=value1&param1=value2&param2=value3}
     * </pre>
     * <p>
     * 原始参数结构为 {@code Map<String, String[]>}，表示：
     * <ul>
     *   <li>{@code "param1" → ["value1", "value2"]}</li>
     *   <li>{@code "param2" → ["value3"]}</li>
     * </ul>
     * <p>
     * 本方法使用 Hutool 工具类将数组中的多个值用逗号（{@code ,}）连接成单个字符串，返回简化后的映射：
     * <ul>
     *   <li>{@code "param1" → "value1,value2"}</li>
     *   <li>{@code "param2" → "value3"}</li>
     * </ul>
     *
     * <p>适用于只需将参数简单导出为字符串映射的场景（如日志记录、参数快照等）。
     * 若需保留多值结构，请直接使用 {@link HttpServletRequest#getParameterMap()}。
     *
     * @param request HTTP 请求对象，不能为空
     * @return 参数映射，键为参数名，值为逗号分隔的参数值字符串；若无参数则返回空映射
     * @see ServletUtil#getParamMap(ServletRequest)
     * @see HttpServletRequest#getParameterMap()
     */
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        /*
         * HTTP 参数支持同名多值（如：?tag=java&tag=spring），因此原始结构为 Map<String, String[]>。
         * 此处借助 Hutool 将每个 String[] 的值通过逗号连接，转换为 Map<String, String>，
         * 便于后续处理（如日志打印、参数透传等），避免频繁处理数组。
         */
        return ServletUtil.getParamMap(request);
    }

    /**
     * 判断请求是否为 JSON 请求。
     *
     * @param request 请求对象
     * @return 如果请求的 Content-Type 以 "application/json" 开头，则返回 true；否则返回 false。
     */
    public static boolean isJsonRequest(ServletRequest request) {
        return StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 获取请求体内容。
     * <p> 如果请求是 JSON 请求，则返回请求体内容；否则返回 null。
     *
     * @param request 请求对象
     * @return 请求体内容，如果不是 JSON 请求则返回 null。
     */
    public static String getBody(HttpServletRequest request) {
        if (isJsonRequest(request)) {
            return ServletUtil.getBody(request);
        }
        return null;
    }
}
