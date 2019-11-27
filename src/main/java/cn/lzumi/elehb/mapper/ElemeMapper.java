package cn.lzumi.elehb.mapper;

import cn.lzumi.elehb.domain.ElemeCookie;
import cn.lzumi.elehb.domain.ElemeHb;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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
     * @param num 获取cookie的条数（推荐10以上）
     * @return 饿了么小号cookie的一个列表
     */
    @Select("select * from eleme_cookie where today_use<5 and level=0 limit #{num}")
    List<ElemeCookie> getElemeCookies(@Param("num") int num);

    /**
     * 获取指定用户的cookie
     *
     * @param name 用户名
     * @return 用户的饿了么cookie
     */
    @Select("select ec.* from eleme_cookie ec,user u WHERE u.name = #{name} and u.cookie_id = ec.id")
    ElemeCookie getElemeCookiesByName(@Param("name") String name);

    /**
     * 添加一条红包链接，并判断是否领取
     *
     * @param elemeHb 红包对象
     * @return
     */
    @Insert("insert into eleme_hb (url,sn,is_get,max_num,now_num,get_time) " +
            "values (#{url}, #{sn},#{isGet},#{maxNum},#{nowNum},#{getTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addElemeHb(ElemeHb elemeHb);

    /**
     * 获取未领取红包备用
     *
     * @param num 获取红包数量
     * @return
     */
    @Select("select * from eleme_hb where is_get=0 limit #{num}")
    List<ElemeHb> getElemeHb(@Param("num") int num);
}
