package com.fse.gateway.config;

import com.fse.gateway.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class JWTTokenValidationConfig extends OncePerRequestFilter {

    @Autowired
    private CommonService commonService;

    @Value("${zuul.routes.user.url}")
    private String userUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("doFilterInternal :: JWT validation started");
        String url = request.getRequestURI();
        if (!url.contains("swagger") && !url.contains("csrf") && !url.equals("/web/")
                && !url.startsWith("/web/v2") && !url.startsWith("/web/auth/login")
                && !url.startsWith("/web/auth/logout") && !url.startsWith("/web/auth/success")
                && !url.endsWith("/user/login") && !url.endsWith("/user/createUser")) {
            String authToken = request.getHeader("auth-token");
            if (!ObjectUtils.isEmpty(authToken)) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("auth-token", authToken);
                try {
                    Map<String, String> res = (Map) commonService.getAllTypeResponseRestTemplate(
                            userUrl + "/validateToken",
                            new HttpEntity<>(headers),
                            Map.class, HttpMethod.GET);
                    response.setHeader("auth-token", res.get("auth-token"));
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }
        }
        log.info("doFilterInternal :: JWT validation ended");
        filterChain.doFilter(request, response);
    }
}
