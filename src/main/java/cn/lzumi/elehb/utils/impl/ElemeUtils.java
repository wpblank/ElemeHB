package cn.lzumi.elehb.utils.impl;

import cn.lzumi.elehb.domain.ElemeCookie;
import cn.lzumi.elehb.mapper.ElemeMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * @author izumi
 * @date 2019年08月15日13:50:20
 */
@Component
public class ElemeUtils {

    @Autowired
    ElemeMapper elemeMapper;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public final int COOKIE_NUM = 20;
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 初始化requestHeaders和requestBody
     *
     * @param requestHeaders 请求头
     * @param requestBody    请求体
     * @param sid
     * @param sign
     * @param sn
     */
    public void requestInit(HttpHeaders requestHeaders, MultiValueMap<String,
            String> requestBody, String sid, String sign, String sn) {
        requestHeaders.add("Accept", "*/*");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 5.1; m1 metal Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043409 Safari/537.36 V1ANDSQ7.2.5744YYBD QQ/7.2.5.3305 NetType/WIFI WebP/0.3.0 Pixel/1080");
        requestHeaders.add("Accept-Language", "zh-CN,zh-HK;q=0.9,zh;q=0.8,en-US;q=0.7,en;q=0.6,zh-TW;q=0.5");
        requestHeaders.add("Connection", "Keep-Alive");
        requestHeaders.add("Cookie", "SID=" + sid);

        requestBody.add("sign", sign);
        requestBody.add("group_sn", sn);
    }

    /**
     * 初始化requestHeaders和requestBody
     *
     * @param requestHeaders
     * @param requestBody
     * @param sid
     * @param sign
     * @param sn
     * @param weixinAvatar   小号头像
     * @param weixinName     小号名称
     */
    public void requestInit(HttpHeaders requestHeaders, MultiValueMap<String,
            String> requestBody, String sid, String sign, String sn, String weixinAvatar, String weixinName) {
        requestInit(requestHeaders, requestBody, sid, sign, sn);
        requestBody.add("weixin_avatar", weixinAvatar);
        requestBody.add("weixin_username", weixinName);
    }

    /**
     * 领取一次红包
     * @param sn          红包sn
     * @param elemeCookie cookie
     * @return 领取结果的json对象
     */
    public JSONObject getOne(String sn, ElemeCookie elemeCookie) {
        String getElemeUrl = "https://h5.ele.me/restapi/marketing/v2/promotion/weixin/";
        String openId = elemeCookie.getOpenId();
        HttpHeaders requestHeaders = new HttpHeaders();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        //初始化requestHeaders和requestBody
        requestInit(requestHeaders, requestBody, elemeCookie.getSid(), elemeCookie.getSign(), sn);
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange
                (getElemeUrl + openId, HttpMethod.POST, requestEntity, String.class);
        return JSON.parseObject(responseEntity.getBody());
    }

    public List<ElemeCookie> elemeCookiesInit(List<ElemeCookie> elemeCookies) {
        //如果cookies不存在或者数量小于10，则向数据库请求获得新的cookies
        if (elemeCookies == null || elemeCookies.size() < 10) {
            elemeCookies = elemeMapper.getElemeCookies(COOKIE_NUM);
            logger.info("获取新的cookies，数目为：" + elemeCookies.size());
            return elemeCookies;
        } else {
            return elemeCookies;
        }
    }


    /**
     * @param url 红包链接
     * @return 红包的sn
     */
    public String getSnByUrl(String url) {
        return url.substring(url.indexOf("&sn=") + 4, url.indexOf("&sn=") + 22);
    }
}
