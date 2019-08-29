package cn.lzumi.elehb.controller;

import cn.lzumi.elehb.bean.ElemeCookie;
import cn.lzumi.elehb.bean.ElemeStarCookie;
import cn.lzumi.elehb.utils.ElemeStarUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author izumi
 * @date 2019/08/10
 */
@RestController
@RequestMapping(value = "/eleme_star")
public class ElemeStarController {

    @Autowired
    ElemeStarUtils elemeStarUtils;

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么星选红包领取", tags = {"饿了么星选"})
    public String get() {
        return "hello eleme star";
    }

    /**
     * 领取一次红包
     */
    @GetMapping("/get_one")
    @ApiOperation(value = "领取一次红包", tags = {"饿了么"})
    public Object getOneHb(String caseid, String sign) {
        String s = elemeStarUtils.getOne(caseid, sign, new ElemeStarCookie());
        return elemeStarUtils.getNowNumberFromHtml(s);
    }
}
