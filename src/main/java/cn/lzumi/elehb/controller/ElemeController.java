package cn.lzumi.elehb.controller;

import cn.lzumi.elehb.mapper.ElemeMapper;
import cn.lzumi.elehb.bean.ElemeCookie;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author izumi
 * @date 2019/08/03
 */
@RestController
@RequestMapping(value = "/eleme")
public class ElemeController {

    @Value("${cn.lzumi.elehb}")
    private String elehb;

    @Autowired
    private ElemeMapper elemeMapper;

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么红包领取", tags = {"饿了么"})
    public String get() {
        return elehb;
    }

    @GetMapping("/lucky_number/{sn}")
    @ApiOperation(value = "获取第几个红包是大红包", tags = {"饿了么"})
    public Object getLuckyNumber(@PathVariable(value = "sn") String sn) {
        String elemeNumUrl = "https://h5.ele.me/restapi/marketing/themes/1/group_sns/";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String strbody = restTemplate.exchange(elemeNumUrl + sn, HttpMethod.GET, entity, String.class).getBody();
        JSONObject jsonObject = JSON.parseObject(strbody);
        return jsonObject.get("lucky_number");
    }

    /**
     *
     */
    @GetMapping("/get_one/{sn}")
    @ApiOperation(value = "领取一次红包", tags = {"饿了么"})
    public Object getOneHb(@PathVariable(value = "sn") String sn) {
        String openId = "B92BCA958221F6FFEE01FCBE8AC955D7";
        String getElemeUrl = "https://h5.ele.me/restapi/marketing/v2/promotion/weixin/";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Accept", "*/*");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 5.1; m1 metal Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043409 Safari/537.36 V1ANDSQ7.2.5744YYBD QQ/7.2.5.3305 NetType/WIFI WebP/0.3.0 Pixel/1080");
        requestHeaders.add("Accept-Language", "zh-CN,zh-HK;q=0.9,zh;q=0.8,en-US;q=0.7,en;q=0.6,zh-TW;q=0.5");
        requestHeaders.add("Connection", "Keep-Alive");
        requestHeaders.add("Cookie", "SID=ZdKkACRNv6waReXjFue6z8PkFgUWAmOGDAmg");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("sign", "61dcd338f5e0f7eb520788b262cb4eaa");
        requestBody.add("group_sn", sn);
        requestBody.add("weixin_avatar", "http://thirdqq.qlogo.cn/g?b=oidb&k=2lh9llHjrCvDsXwmiahO9TA&s=40&t=1555107156");
        requestBody.add("weixin_username", "");

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(getElemeUrl + openId, HttpMethod.POST, requestEntity, String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        return jsonObject.toJSONString();
    }

    /**
     *
     */
    @GetMapping("/now_number/{sn}")
    @ApiOperation(value = "查询红包当前领取数量", tags = {"饿了么"})
    public Object getNowNumber(@PathVariable(value = "sn") String sn) {
        String openId = "A2DFA518682C27A84402AC8A1F7A4E06";
        String getElemeUrl = "https://h5.ele.me/restapi/marketing/v2/promotion/weixin/";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Accept", "*/*");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 5.1; m1 metal Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043409 Safari/537.36 V1ANDSQ7.2.5744YYBD QQ/7.2.5.3305 NetType/WIFI WebP/0.3.0 Pixel/1080");
        requestHeaders.add("Accept-Language", "zh-CN,zh-HK;q=0.9,zh;q=0.8,en-US;q=0.7,en;q=0.6,zh-TW;q=0.5");
        requestHeaders.add("Connection", "Keep-Alive");
        requestHeaders.add("Cookie", "SID=eFYsvCamsdNkx22MqVQbG3Y5m8BbxYeaIhqA");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("sign", "cd5dc635f37194eb3b8f59110653311f");
        requestBody.add("group_sn", sn);
        RestTemplate restTemplate = new RestTemplate();
        //添加hearer和body，发送post请求
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(getElemeUrl + openId, HttpMethod.POST,
                        new HttpEntity<>(requestBody, requestHeaders), String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("promotion_records").toString());
        return jsonArray.size();
    }

    @GetMapping("/get_cookie")
    public Object getCookie() {
        List<ElemeCookie> elemeCookies = elemeMapper.getElemeCookies();
        for (ElemeCookie elemeCookie : elemeCookies) {
            System.out.println(elemeCookie.getOpenId());
        }
        return elemeCookies.get(0).getWeixinAvatar();
    }
}