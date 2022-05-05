package com.lingao.snkcomm.jwt;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingao.
 * @description token过滤器
 * @date 2022/5/4 - 21:23
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if(this.isProtectedUrl(request)) {
                /*
                options 请求就是预检请求，可用于检测服务器允许的 http 方法。当发起跨域请求时，
                由于安全原因，触发一定条件时浏览器会在正式请求之前自动先发起 OPTIONS 请求，
                即 CORS 预检请求，服务器若接受该跨域请求，浏览器才继续发起正式请求。
                 */
                if(!request.getMethod().equals("OPTIONS")){
                    request = JwtUtil.validateTokenAndAddUserIdToHeader(request);
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedUrl(HttpServletRequest request) {
        //token使用的请求List
        List<String> protectedPaths = new ArrayList<String>();
        protectedPaths.add("/ums/user/info");
        protectedPaths.add("/ums/user/update");
        protectedPaths.add("/post/create");
        protectedPaths.add("/post/update");
        protectedPaths.add("/post/delete/*");
        protectedPaths.add("/comment/add_comment");
        protectedPaths.add("/relationship/subscribe/*");
        protectedPaths.add("/relationship/unsubscribe/*");
        protectedPaths.add("/relationship/validate/*");

        boolean bFind = false;
        for( String passedPath : protectedPaths ) {
            //匹配请求方法是否在List中
            bFind = pathMatcher.match(passedPath, request.getServletPath());
            if( bFind ) {
                break;
            }
        }
        return bFind;
    }
}
