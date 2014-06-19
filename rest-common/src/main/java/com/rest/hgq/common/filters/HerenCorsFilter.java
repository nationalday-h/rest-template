package com.rest.hgq.common.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HerenCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletResponse.setCharacterEncoding("UTF-8");

        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Allow-Control-Allow-Methods", "POST,GET,OPTIONS");
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With");
        res.addHeader("Access-Control-Max-Age", "600000");

        filterChain.doFilter(servletRequest, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
