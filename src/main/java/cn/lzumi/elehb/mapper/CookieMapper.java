package cn.lzumi.elehb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author izumi
 */
@Mapper
@Component
public interface CookieMapper {

    /**
     * 重置饿了么星选cookie的可以使用次数
     */
    @Update("update eleme_star_cookie set margin=if(`level`=0,5,3) " +
            "where (`level`=0 or `level`=1) and is_lose=0")
    int resetMargin();
}
