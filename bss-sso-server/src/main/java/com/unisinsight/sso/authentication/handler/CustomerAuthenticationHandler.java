package com.unisinsight.sso.authentication.handler;

import com.unisinsight.sso.exception.UsernamePasswordException;
import com.unisinsight.sso.service.entity.CustomCredential;
import com.unisinsight.sso.service.entity.User;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @deprecated
 * 带验证码认证处理
 * @author yangxiaoyu
 */
public class CustomerAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAuthenticationHandler.class);

    public CustomerAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    public boolean supports(Credential credential) {
        //判断传递过来的Credential 是否是自己能处理的类型
        return credential instanceof CustomCredential;
    }

    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {

        CustomCredential customCredential = (CustomCredential) credential;
        String username = customCredential.getUsername();
        String password = customCredential.getPassword();
        String capcha = customCredential.getCapcha();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("用户登录,username:{},password:{},capcha:{}", username, password, capcha);
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String rightCapcha = attributes.getRequest().getSession().getAttribute("captcha_code").toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("session存储capcha:{}",rightCapcha);
        }
        if(!capcha.equalsIgnoreCase(rightCapcha)){
            throw new UsernamePasswordException("验证码错误");
        }

        User info = null;
        try {
            // JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://192.168.108.52:11191/zheng?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=UTC");
            dataSource.setUsername("root");
            dataSource.setPassword("root");

            // 创建JDBC模板
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(dataSource);

            String sql = "select *,nickname as username from ucenter_user where nickname = ?";

            info = (User) jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper(User.class));

            System.out.println("database username : "+ info.getUsername());
            System.out.println("database password : "+ info.getPassword());

            if (info == null) {
                throw new AccountException("用户不存在!");
            }

        } catch (Exception e){
            LOGGER.debug("用户ren认证失败", e);
        }
        //不区分大小写
        if (!info.getPassword().equalsIgnoreCase(password)) {
            throw new FailedLoginException("密码不正确!");
        } else {
            //可自定义返回给客户端的多个属性信息
            HashMap<String, Object> returnInfo = new HashMap<>();
            //returnInfo.put("expired", info.getDisabled());
            final List<MessageDescriptor> list = new ArrayList<>();
            return createHandlerResult(customCredential,
                    this.principalFactory.createPrincipal(username, returnInfo), list);
        }

    }

    public static void main(String[] args) {
        try {
            // JDBC模板依赖于连接池来获得数据的连接，所以必须先要构造连接池
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://192.168.108.52:11191/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&serverTimezone=UTC");
            dataSource.setUsername("root");
            dataSource.setPassword("root");

            // 创建JDBC模板
            JdbcTemplate jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(dataSource);

            String sql = "select *  from user where username = ?";

            User info = (User) jdbcTemplate.queryForObject(sql, new Object[]{"test"}, new BeanPropertyRowMapper(User.class));

            System.out.println("database username : "+ info.getUsername());
            System.out.println("database password : "+ info.getPassword());

            if (info == null) {
                throw new AccountException("用户不存在!");
            }

        } catch (Exception e){
            LOGGER.debug("用户ren认证失败", e);
        }
    }
}
