package cn.lzumi.elehb.mapper;

import cn.lzumi.elehb.domain.ElemeStarCookie;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author izumi
 * @date 2019-08-30 11:19:25
 */
@Mapper
@Component
public interface ElemeStarMapper {
    /**
     * 获取饿了么星选cookie，暂存内存中用于小号垫包
     *
     * @param num 获取cookie的条数（推荐10以上）
     * @return 饿了么星选小号cookie的一个列表
     */
    @Select("select * from eleme_star_cookie where margin>0 and level=0 and is_lose=0 limit #{num}")
    List<ElemeStarCookie> getElemeStarCookies(@Param("num") int num);

    /**
     * 获取用户的饿了么星选cookie
     *
     * @param name 用户名
     * @return 饿了么星选cookie
     */
    @Select("SELECT e.* FROM eleme_star_cookie e,`user` u WHERE e.id=u.eleme_star_cookie_id and u.`name`=#{name}")
    ElemeStarCookie getUserElemeStarCookie(@Param("name") String name);

    /**
     * 更新星选cookie的使用次数信息
     *
     * @param elemeStarCookies
     * @return 更新数量
     */
    int updateElemeStarCookieUseInfo(List<ElemeStarCookie> elemeStarCookies);

    /**
     * 将所有星选cookie的今日使用次数清空
     *
     * @return
     */
    @Update("UPDATE eleme_star_cookie SET today_use=0")
    int clearElemeStarCookieTodayUse();
}
