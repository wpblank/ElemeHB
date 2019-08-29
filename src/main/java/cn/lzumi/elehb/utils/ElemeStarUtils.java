package cn.lzumi.elehb.utils;

import cn.lzumi.elehb.bean.ElemeCookie;
import cn.lzumi.elehb.bean.ElemeStarCookie;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author izumi
 * @date 2019-08-29 15:09:50
 */
@Component
public class ElemeStarUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 初始化requestHeaders
     *
     * @param requestHeaders 请求头
     * @param cookie
     */
    public void requestInit(HttpHeaders requestHeaders, String cookie) {
        requestHeaders.add("Accept", "*/*");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 9; MIX 3 Build/PKQ1.180729.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044813 Mobile Safari/537.36 MMWEBID/8168 MicroMessenger/7.0.5.1440(0x27000534) Process/tools NetType/WIFI Language/zh_CN");
        //requestHeaders.add("Accept-Language", "zh-CN,zh-HK;q=0.9,zh;q=0.8,en-US;q=0.7,en;q=0.6,zh-TW;q=0.5");
        requestHeaders.add("Connection", "Keep-Alive");
        requestHeaders.add("Cookie", cookie);
    }

    /**
     * 领取一次饿了么星选红包
     *
     * @param caseid
     * @param sign
     * @param elemeStarCookie
     * @return
     */
    public String getOne(String caseid, String sign, ElemeStarCookie elemeStarCookie) {
        String getElemeStarUrl = "https://star.ele.me/hongbao/wpshare?";
        caseid = "caseid=" + caseid + "&";
        sign = "sign=" + sign;
        //数据库读
        String cookie = elemeStarCookie.cookie;
        HttpHeaders requestHeaders = new HttpHeaders();

        //初始化requestHeaders和requestBody
        requestInit(requestHeaders, cookie);
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange
                (getElemeStarUrl + caseid + sign, HttpMethod.GET, requestEntity, String.class);
        logger.debug(responseEntity.toString());
        return responseEntity.getBody();
    }

    public void getMaxNumberFromHtml(String html){


    }

    public int getNowNumberFromHtml(String html){
        // 在字符串中匹配：friends_info (已领取人员信息)
        String patternStr = "\"friends_info\":.*\\]";
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(patternStr);
        // 现在创建 matcher 对象
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            JSONArray jsonArray = JSON.parseArray(matcher.group(0).substring(15));
            return jsonArray.size();
        } else {
            logger.error("饿了么星选红包领取个数获取失败:{}",matcher.group(0));
            return -1;
        }
    }

    public List<ElemeStarCookie> elemeStarCookiesInit(List<ElemeStarCookie> elemeStarCookies) {
        //如果cookies不存在或者数量小于10，则向数据库请求获得新的cookies
        if (elemeStarCookies == null || elemeStarCookies.size() < 5) {
            //elemeCookies = elemeMapper.getElemeCookies(COOKIE_NUM);
            logger.info("获取新的星选cookies，数目为：" + elemeStarCookies.size());
            return elemeStarCookies;
        } else {
            return elemeStarCookies;
        }
    }
}
