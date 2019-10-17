package com.qf.security.mapper;

import com.qf.security.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 81958
 */
@Component
public class UserMapper {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SysUser getSysUserByUsenameOrMobile(String usernameOrPhone) throws EmptyResultDataAccessException {

        // 耦合度过高
        String sql = "select * from t_user where username = ? or phone = ?";

        return jdbcTemplate.queryForObject(sql,
                new BeanPropertyRowMapper<SysUser>(SysUser.class), usernameOrPhone, usernameOrPhone);
    }

}
