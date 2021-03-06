package club.wenfan.security.config;

import club.wenfan.security.dto.Token;
import club.wenfan.security.entity.SysUser;
import club.wenfan.security.filter.TokenFilter;
import club.wenfan.security.service.TokenService;
import club.wenfan.security.utils.ResponseUtil;
import club.wenfan.security.vo.LoginUser;
import club.wenfan.security.vo.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wenfan on 2019/12/28 19:16
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityHandlerConfig {

    @Autowired
    private TokenService tokenService;


    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                Token token = tokenService.saveToken(loginUser);
                ResponseUtil.responseJson(httpServletResponse, HttpStatus.OK.value(), token);
            }
        };
    }


    /**
     * 登录失败 返回401
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler loginFailHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
                String msg = null;
                if (e instanceof BadCredentialsException) {
                    msg = "密码错误";
                } else {
                    msg = e.getMessage();
                }
                ResponseInfo info = new ResponseInfo(HttpStatus.UNAUTHORIZED.value() + "", msg);
                ResponseUtil.responseJson(httpServletResponse, HttpStatus.UNAUTHORIZED.value(), info);
            }
        };
    }


    /**
     * 未登录 返回401
     *
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
                ResponseInfo info = new ResponseInfo(HttpStatus.UNAUTHORIZED.value() + "", "请先登录");
                ResponseUtil.responseJson(httpServletResponse, HttpStatus.UNAUTHORIZED.value(), info);
            }
        };
    }


    /**
     * 退出登录--删除token
     *
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

                ResponseInfo info = new ResponseInfo(HttpStatus.OK.value() + "", "退出成功");
                String token = TokenFilter.getToken(httpServletRequest);
                tokenService.deleteToken(token);
                ResponseUtil.responseJson(httpServletResponse, HttpStatus.OK.value(), info);
            }
        };
    }


}
