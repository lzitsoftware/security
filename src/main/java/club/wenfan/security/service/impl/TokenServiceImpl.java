package club.wenfan.security.service.impl;

import club.wenfan.security.dto.Token;
import club.wenfan.security.service.TokenService;
import club.wenfan.security.vo.LoginUser;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by wenfan on 2019/12/28 14:23
 */
@Service
public class TokenServiceImpl implements TokenService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Value("${token.expire.seconds}")
    private Integer expireSeconds;
    @Value("${token.expire.jwtSecret}")
    private String jwtSecret;

    private static Key KEY = null;
    private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    @Autowired
    private RedisTemplate<String, LoginUser> redisTemplate;

    @Override
    public Token saveToken(LoginUser user) {
        user.setToken(UUID.randomUUID().toString());
        cacheLoginUser(user);
        log.info("{} 已登录", user.getUsername());
        String jwtToken = createJWTToken(user);
        return new Token(jwtToken, user.getLoginTime());
    }

    @Override
    public void freshToken(LoginUser user) {
        cacheLoginUser(user);
    }

    @Override
    public LoginUser getLoginUser(String token) {
        String uuid = getUUIDFromJWT(token);
        if (uuid != null) {
            return redisTemplate.boundValueOps(getTokenKey(uuid)).get();
        }

        return null;
    }

    @Override
    public boolean deleteToken(String token) {
        String uuid = getUUIDFromJWT(token);
        if (uuid != null) {
            String key = getTokenKey(uuid);
            LoginUser loginUser = redisTemplate.opsForValue().get(key);
            if (loginUser != null) {
                redisTemplate.delete(key);
                // 退出日志
                //logService.save(loginUser.getId(), "退出", true, null);
                log.info("{} 已退出", loginUser.getUsername());
                return true;
            }
        }

        return false;
    }


    /**
     * 生成jwt
     *
     * @param loginUser
     * @return
     */
    private String createJWTToken(LoginUser loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(LOGIN_USER_KEY, loginUser.getToken());// 放入一个随机字符串，通过该串可找到登陆用户

        String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();

        return jwtToken;
    }


    private void cacheLoginUser(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
        // 根据uuid将loginUser缓存
        String token = loginUser.getToken();
        String tokenKey = getTokenKey(token);

        redisTemplate.boundValueOps(tokenKey).set(loginUser, expireSeconds, TimeUnit.SECONDS);
    }


    private String getTokenKey(String uuid) {
        return "tokens:" + uuid;
    }

    private Key getKeyInstance() {
        if (KEY == null) {
            synchronized (TokenServiceImpl.class) {
                if (KEY == null) {// 双重锁
                    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
                    KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
                }
            }
        }

        return KEY;
    }

    private String getUUIDFromJWT(String jwtToken) {
        if ("null".equals(jwtToken) || StringUtils.isBlank(jwtToken)) {
            return null;
        }

        try {
            Map<String, Object> jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwtToken).getBody();
            return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
        } catch (ExpiredJwtException e) {
            log.error("{}已过期", jwtToken);
        } catch (Exception e) {
            log.error("{}", e);
        }

        return null;
    }
}

