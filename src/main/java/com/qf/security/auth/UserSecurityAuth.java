package com.qf.security.auth;

import com.qf.security.mapper.UserMapper;
import com.qf.security.pojo.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@Component
public class UserSecurityAuth implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhone) throws UsernameNotFoundException {

        SysUser sysUser = null;

        try {
            sysUser = userMapper.getSysUserByUsenameOrMobile(usernameOrPhone);
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }else {
            log.error("用户名或手机为: " + usernameOrPhone);

            User user = null;

            try {
                user = new User(usernameOrPhone,
                        sysUser.getPassword(),
                        Arrays.asList(new SimpleGrantedAuthority("admin")));
            } catch (InternalAuthenticationServiceException ex) {
                throw ex ;
            }
            return user;
        }
    }
}
