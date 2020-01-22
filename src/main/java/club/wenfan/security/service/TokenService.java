package club.wenfan.security.service;

import club.wenfan.security.dto.Token;
import club.wenfan.security.vo.LoginUser;

/**
 * Created by wenfan on 2019/12/28 14:11
 */
public interface TokenService {

    Token saveToken(LoginUser user);

    void freshToken(LoginUser user);

    LoginUser getLoginUser(String token);

    boolean deleteToken(String token);

}
