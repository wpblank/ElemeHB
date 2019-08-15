package cn.lzumi.elehb.utils;

import cn.lzumi.elehb.bean.ElemeCookie;
import cn.lzumi.elehb.mapper.ElemeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;


/**
 * @author izumi
 * @date 2019年08月15日13:50:20
 */
@Component
public class ElemeUtils {

    @Autowired
    ElemeMapper elemeMapper;
    public final Logger logger = LoggerFactory.getLogger(getClass());

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

    public List<ElemeCookie> elemeCookiesInit(List<ElemeCookie> elemeCookies) {
        //如果cookies不存在或者数量小于10，则向数据库请求获得新的cookies
        if (elemeCookies == null || elemeCookies.size() < 10) {
            elemeCookies = elemeMapper.getElemeCookies();
            logger.debug("获取新的cookies，数目为：" + elemeCookies.size());
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
