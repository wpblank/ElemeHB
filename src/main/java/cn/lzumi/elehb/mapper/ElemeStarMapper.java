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
    @Select("select * from eleme_star_cookie where margin>0 and level=0 and is_lose=0 order by total_use limit #{num}")
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
     * 重置饿了么星选cookie的可以使用次数
     *
     * @return 重置条数
     */
    @Update("update eleme_star_cookie set margin=if(`level`=0,5,3) " +
            "where (`level`=0 or `level`=1) and is_lose=0")
    int resetMargin();
}
