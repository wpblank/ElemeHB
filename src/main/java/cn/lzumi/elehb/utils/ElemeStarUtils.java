package cn.lzumi.elehb.utils;

import cn.lzumi.elehb.bean.ElemeCookie;
import cn.lzumi.elehb.bean.ElemeStarCookie;
import cn.lzumi.elehb.bean.ElemeStarHb;
import cn.lzumi.elehb.mapper.ElemeStarMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.awt.event.WindowFocusListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.lzumi.elehb.utils.ResponseUtils.*;

/**
 * @author izumi
 * @date 2019-08-29 15:09:50
 */
@Component
public class ElemeStarUtils {
    @Autowired
    ElemeStarMapper elemeStarMapper;

    @Value("${cn.lzumi.utilElemeStarCookie}")
    public String utilElemeStarCookie;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    public final int COOKIE_NUM = 10;
    private RestTemplate restTemplate = new RestTemplate();
    private RegexUtils regexUtils = new RegexUtils();

    /**
     * 初始化requestHeaders
     *
     * @param requestHeaders 请求头
     * @param cookie
     * @param app            0:微信 1:钉钉
     */
    public void requestInit(HttpHeaders requestHeaders, String cookie, int app) {
        requestHeaders.add("Accept", "*/*");
        switch (app) {
            case 0:
                requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 9; MIX 3 Build/PKQ1.180729.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044813 Mobile Safari/537.36 MMWEBID/8168 MicroMessenger/7.0.5.1440(0x27000534) Process/tools NetType/WIFI Language/zh_CN");
                break;
            case 1:
                requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; U; Android 9; zh-CN; MIX 3 Build/PKQ1.180729.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/11.9.4.974 UWS/2.13.2.46 Mobile Safari/537.36 AliApp(DingTalk/4.7.7) com.alibaba.android.rimet/11964676 Channel/700159 language/zh-CN");
                break;
            default:
                break;
        }
        requestHeaders.add("Connection", "Keep-Alive");
        requestHeaders.add("Cookie", cookie);
    }

    /**
     * 领取一次饿了么星选红包
     *
     * @param elemeStarHb     饿了么星选红包
     * @param elemeStarCookie 饿了么星选cookie
     * @return html
     */
    public String getOne(ElemeStarHb elemeStarHb, ElemeStarCookie elemeStarCookie) {
        String cookie = elemeStarCookie.getCookie();
        HttpHeaders requestHeaders = new HttpHeaders();
        //初始化requestHeaders
        requestInit(requestHeaders, cookie, elemeStarCookie.getApp());
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange
                (elemeStarHb.getUrl(), HttpMethod.GET, requestEntity, String.class);
        logger.debug(responseEntity.toString());
        // 领取成功, cookie使用次数+1
        if (SUCCESS == getStatus(responseEntity.getBody())) {
            elemeStarCookie.add();
        }
        return responseEntity.getBody();
    }

    /**
     * 领取饿了么星选红包
     *
     * @param elemeStarHb
     * @param elemeStarCookies
     * @param userElemeStarCookie 想要领取最大红包的cookie
     * @return
     */
    public Map<String, Object> getAllHb(ElemeStarHb elemeStarHb, List<ElemeStarCookie> elemeStarCookies, ElemeStarCookie userElemeStarCookie) {
        String result = getOneByUtil(elemeStarHb);
        if (getStatus(result) == OVERDUE) {
            return myResponse("红包已过期", HB_OVERDUE);
        }
        int luckyNum = getLuckyNumberFromHtml(result);
        int nowNum = getNowNumberFromHtml(result);
        if (luckyNum - nowNum < 1) {
            logger.debug("红包已被领取{}/{},{}", nowNum, luckyNum, elemeStarHb.getUrl());
            return myResponse("红包已被领取:" + nowNum + "/" + luckyNum + ","
                    + elemeStarHb.getUrl(), HB_RECEIVED);
        } else if (luckyNum - nowNum > 1) {
            for (int i = 0; luckyNum - nowNum > 1 && i < elemeStarCookies.size(); i++) {
                nowNum = getNowNumberFromHtml(getOne(elemeStarHb, elemeStarCookies.get(i)));
            }
        }
        // 判断是否领取完毕
        if (luckyNum - nowNum == 1) {
            if (userElemeStarCookie == null) {
                return myResponse("红包已领取到最大前一个:" + nowNum + "/" + luckyNum + ","
                        + elemeStarHb.getUrl(), GET_SUCCESS);
            }
            // 帮助用户领取最大的红包
            else {
                String userResult = getOne(elemeStarHb, userElemeStarCookie);
                switch (getStatus(userResult)) {
                    case SUCCESS:
                        return myResponse("领取成功,红包金额:" + getAmountFromHtml(userResult) + "元", GET_SUCCESS);
                    case RECEIVED:
                        return myResponse("你已经领取过该红包" + nowNum + "/" + luckyNum + ","
                                + elemeStarHb.getUrl(), USER_RECEIVED);
                    default:
                        logger.error("未知红包状态码:{}", userResult);
                        return myResponse("红包已领取到最大前一个:" + nowNum + "/" + luckyNum + ","
                                + elemeStarHb.getUrl(), FAIL_TO_RECEIVE);
                }
            }
        } else {
            // 未能成功领取
            return myResponse("红包领取失败:" + nowNum + "/" + luckyNum + ","
                    + elemeStarHb.getUrl(), GET_PARTIAL);
        }
    }

    /**
     * 根据返回结果获取领取状态
     *
     * @param html 请求返回的html
     * @return
     */
    public int getStatus(String html) {
        Matcher matcher = regexUtils.getMatcher("\"status\":[0-9]{1,5}", html);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(9));
        } else {
            logger.error("饿了么星选领取状态获取失败:{}", matcher.group(0));
            return -1;
        }
    }

    /**
     * 根据返回结果获取第几个是最大红包
     *
     * @param html 请求返回的html
     * @return lucky_number
     */
    public int getLuckyNumberFromHtml(String html) {
        // 在字符串中匹配：【饿了么星选】第 (红包最大个数)
        Matcher matcher = regexUtils.getMatcher("【饿了么星选】第[0-9]{1,2}", html);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(8));
        } else {
            logger.error("饿了么星选红包最大个数获取失败:{}", matcher.group(0));
            return -1;
        }
    }

    /**
     * 根据返回结果获取领取金额
     *
     * @param html 请求返回的html
     * @return Amount 红包金额
     */
    public int getAmountFromHtml(String html) {
        Matcher matcher = regexUtils.getMatcher("\"amount\":[0-9]{1,2},\"phone\"", html);
        if (matcher.find()) {
            // matcher.group(0) == "amount":11,"phone"
            String amount = matcher.group(0).split(",")[0].substring(9);
            return Integer.parseInt(amount);
        } else {
            logger.error("饿了么星选红包金额获取失败:{}", html);
            return -1;
        }
    }

    /**
     * 根据返回结果获取当前已领取红包个数
     *
     * @param html 请求返回的html
     * @return 已领取个数
     */
    public int getNowNumberFromHtml(String html) {
        // 在字符串中匹配：friends_info (已领取人员信息)
        Matcher matcher = regexUtils.getMatcher("\"friends_info\":.*?]", html);
        if (matcher.find()) {
            try {
                JSONArray jsonArray = JSON.parseArray(matcher.group(0).substring(15));
                logger.debug(jsonArray.toJSONString());
                return jsonArray.size();
            } catch (Exception e) {
                logger.error("饿了么星选红包领取个数获取失败:{},{}", matcher.group(0), e);
                return -1;
            }
        } else {
            logger.error("饿了么星选红包领取个数获取失败:{}", matcher.group(0));
            return -1;
        }
    }

    /**
     * 通过工具人小号，查询红包信息
     *
     * @param elemeStarHb
     * @return
     */
    public String getOneByUtil(ElemeStarHb elemeStarHb) {
        HttpHeaders requestHeaders = new HttpHeaders();
        //初始化requestHeaders
        requestInit(requestHeaders, utilElemeStarCookie, 0);
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange
                (elemeStarHb.getUrl(), HttpMethod.GET, requestEntity, String.class);
        logger.debug(responseEntity.toString());
        return responseEntity.getBody();
    }

    /**
     * 组装星选红包
     *
     * @param caseid
     * @param sign
     * @param requestBody
     * @return ElemeStarHb
     */
    public ElemeStarHb elemeStarHbInit(String caseid, String sign, MultiValueMap<String, String> requestBody) {
        if (requestBody != null && requestBody.containsKey("url")) {
            String url = requestBody.get("url").get(0);
            //注意了！ 此处的caseid长度不是固定的(大概是订单总数之类的)，当前为10位数，懒得写位数变换的情况！
            caseid = url.substring(url.indexOf("caseid=") + 7, url.indexOf("caseid=") + 17);
            sign = url.substring(url.indexOf("sign=") + 5, url.indexOf("sign=") + 37);
            return new ElemeStarHb(url, caseid, sign);
        } else if (caseid != null && sign != null) {
            String url = "https://star.ele.me/hongbao/wpshare?caseid=" + caseid + "&sign=" + sign;
            return new ElemeStarHb(url, caseid, sign);
        } else {
            return new ElemeStarHb(null, null, null);
        }
    }

    /**
     * 初始化饿了么星选cookies
     * 如果cookies不存在或者数量过少，则向数据库请求获得新的cookies
     * 同时将旧cookie的使用次数更新至数据库
     *
     * @param elemeStarCookies 星选cookie列表
     * @return
     */
    public List<ElemeStarCookie> elemeStarCookiesInit(List<ElemeStarCookie> elemeStarCookies) {
        if (elemeStarCookies.size() == 0) {
            elemeStarCookies = elemeStarMapper.getElemeStarCookies(COOKIE_NUM);
            logger.info("获取新的星选cookies，数目为：" + elemeStarCookies.size());
            return elemeStarCookies;
        } else if (elemeStarCookies.size() < 5) {
            logger.info("更新星选信息条数:{}", elemeStarMapper.updateElemeStarCookieUseInfo(elemeStarCookies));
            elemeStarCookies = elemeStarMapper.getElemeStarCookies(COOKIE_NUM);
            logger.info("获取新的星选cookies，数目为：" + elemeStarCookies.size());
            return elemeStarCookies;
        } else {
            return elemeStarCookies;
        }
    }
}
