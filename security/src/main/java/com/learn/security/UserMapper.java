package com.learn.security;

import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User loadUserByUsername(String username);

    @Select("select *\n" +
            "        from role\n" +
            "        where id in (select rid from user_role where uid = #{uid})")
    List<Role> getRolesById(Integer id);
}
