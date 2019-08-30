package cn.lzumi.elehb.controller;

import cn.lzumi.elehb.bean.ElemeCookie;
import cn.lzumi.elehb.bean.ElemeStarCookie;
import cn.lzumi.elehb.utils.ElemeStarUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author izumi
 * @date 2019/08/10
 */
@RestController
@RequestMapping(value = "/eleme_star")
public class ElemeStarController {

    @Autowired
    ElemeStarUtils elemeStarUtils;

    private List<ElemeStarCookie> elemeStarCookies = new ArrayList<>();

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么星选红包领取", tags = {"饿了么星选"})
    public String get() {
        return "hello eleme star";
    }

    /**
     * 领取一次红包
     */
    @GetMapping("/get_one")
    @ApiOperation(value = "领取一次红包", tags = {"饿了么星选"})
    public Object getOneHb(String caseid, String sign) {
        //初始化cookies
        //elemeStarCookies = elemeStarUtils.elemeStarCookiesInit(elemeStarCookies);
        elemeStarCookies.add(new ElemeStarCookie());
        elemeStarCookies.get(0).cookie = "WMID=f0f071c9a70a4b73d1026b141521a9eb; whid=WWNNR0N4N1RqdzNhNHRaUWZXcjhJa0tuZWJ6QnBlbll4VFdGS1MwRm1aMjkwZEdSaFVHRkpiMDlSTUU5Mk5BPT0%3D; WMST=1567060390";
        String s = elemeStarUtils.getOne(caseid, sign, elemeStarCookies.get(0));
        return elemeStarUtils.getNowNumberFromHtml(s);
    }

    /**
     * 领取红包到最大一个以前，如果name存在，则帮name领取最大红包
     *
     * @param caseid 红包caseid
     * @param sign   红包sign
     * @param name   用户名
     * @return
     */
    @GetMapping("/get_all")
    @ApiOperation(value = "领取红包", tags = {"饿了么星选"})
    public Object getAllHb(String caseid, String sign, String name) {

        return "";
    }
}
