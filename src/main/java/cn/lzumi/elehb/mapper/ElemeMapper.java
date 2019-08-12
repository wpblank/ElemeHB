package cn.lzumi.elehb.mapper;

import cn.lzumi.elehb.bean.ElemeCookie;
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
     * 获取cookie
     */
    @Select("select * from eleme_cookie")
    List<ElemeCookie> getElemeCookies();

}
