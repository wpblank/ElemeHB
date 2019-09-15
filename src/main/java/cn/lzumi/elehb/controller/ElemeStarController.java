package cn.lzumi.elehb.controller;

import cn.lzumi.elehb.bean.ElemeCookie;
import cn.lzumi.elehb.bean.ElemeStarCookie;
import cn.lzumi.elehb.mapper.ElemeStarMapper;
import cn.lzumi.elehb.utils.ElemeStarUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.MultiValueMap;
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

    @Autowired
    ElemeStarMapper elemeStarMapper;

    private List<ElemeStarCookie> elemeStarCookies = new ArrayList<>();

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么星选红包领取", tags = {"饿了么星选"})
    public String get(String caseid, String sign,
                      @RequestBody(required = false) MultiValueMap<String, String> requestBody) {
        if (requestBody != null && requestBody.containsKey("url")) {
            //String caseid
            return requestBody.get("url").get(0);
        } else if (caseid != null && sign != null) {
            return caseid + " " + sign;
        } else {
            return "啥也你没有";
        }

    }

    /**
     * 领取一次红包
     *
     * @return 当前领取个数
     */
    @GetMapping("/get_one")
    @ApiOperation(value = "领取一次红包", tags = {"饿了么星选"})
    public Object getOneHb(String caseid, String sign) {
        //初始化cookies
        elemeStarCookies = elemeStarUtils.elemeStarCookiesInit(elemeStarCookies);
        String result = elemeStarUtils.getOne(caseid, sign, elemeStarCookies.get(0));
        return elemeStarUtils.getNowNumberFromHtml(result);
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
        //初始化cookies
        elemeStarCookies = elemeStarUtils.elemeStarCookiesInit(elemeStarCookies);
        ElemeStarCookie userElemeStarCookie = elemeStarMapper.getUserElemeStarCookie(name);
        return elemeStarUtils.getAllHb(caseid, sign, elemeStarCookies, userElemeStarCookie);
    }

    @GetMapping("/get_number")
    @ApiOperation(value = "查询红包当前领取数量", tags = {"饿了么星选"})
    public String getHbNumber(String caseid, String sign) {
        String result = elemeStarUtils.getOneByUtil(caseid, sign);
        int luckyNum = elemeStarUtils.getLuckyNumberFromHtml(result);
        int nowNum = elemeStarUtils.getNowNumberFromHtml(result);
        return "红包领取状态:" + nowNum + "/" + luckyNum;
    }
}
