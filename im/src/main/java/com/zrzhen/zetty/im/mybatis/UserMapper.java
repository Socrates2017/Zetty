package com.zrzhen.zetty.im.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {


    @Select("select title from article where id=#{id}")
    String selectName(@Param("id") Integer id);

}
