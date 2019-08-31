package cn.lzumi.elehb.mapper;

import cn.lzumi.elehb.bean.ElemeStarCookie;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Select("select * from eleme_star_cookie where today_use<5 and is_user=0 limit #{num}")
    List<ElemeStarCookie> getElemeStarCookies(@Param("num") int num);
}
