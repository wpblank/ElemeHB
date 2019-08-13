package cn.lzumi.elehb.mapper;

import cn.lzumi.elehb.bean.ElemeCookie;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author izumi
 * @date 2019/08/12
 */
@Mapper
@Component
public interface ElemeMapper {
    /**
     * 获取cookie，暂存内存中用于小号垫包
     *
     * @return 饿了么小号cookie的一个列表
     */
    @Select("select * from eleme_cookie")
    List<ElemeCookie> getElemeCookies();

    /**
     * 获取指定用户的cookie
     *
     * @param name 用户名
     * @return 用户的饿了么cookie
     */
    @Select("select ec.* from eleme_cookie ec,user u WHERE u.name = #{name} and u.cookie_id = ec.id")
    ElemeCookie getElemeCookiesById(@Param("name") String name);

}
