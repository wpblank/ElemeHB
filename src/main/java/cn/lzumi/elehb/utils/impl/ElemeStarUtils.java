package cn.lzumi.elehb.utils.impl;

import cn.lzumi.elehb.domain.Cookie;
import cn.lzumi.elehb.domain.ElemeStarCookie;
import cn.lzumi.elehb.domain.ElemeStarHb;
import cn.lzumi.elehb.domain.Hb;
import cn.lzumi.elehb.utils.HbUtils;
import cn.lzumi.elehb.utils.RegexUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.regex.Matcher;

import static cn.lzumi.elehb.utils.ResponseUtils.*;

/**
 * @author izumi
 * @date 2019-08-29 15:09:50
 */
public class ElemeStarUtils implements HbUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private RestTemplate restTemplate = new RestTemplate();
    private RegexUtils regexUtils = new RegexUtils();

    /**
     * 初始化requestHeaders
     *
     * @param requestHeaders 请求头
     * @param cookie
     * @param app            0:微信 1:钉钉
     */
    @Override
    public void requestInit(HttpHeaders requestHeaders,
                            MultiValueMap<String, String> requestBody, Cookie cookie, int app) {
        ElemeStarCookie elemeStarCookie = (ElemeStarCookie) cookie;
        requestHeaders.add("Accept", "*/*");
        switch (app) {
            case 0:
                requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 10; MIX 3 Build/QKQ1.190828.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/67.0.3396.87 XWEB/882 MMWEBSDK/190503 Mobile Safari/537.36 MMWEBID/614 MicroMessenger/7.0.5.1440(0x27000534) Process/tools NetType/WIFI Language/zh_CN");
                break;
            case 1:
                requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; U; Android 9; zh-CN; MIX 3 Build/PKQ1.180729.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/11.9.4.974 UWS/2.13.2.46 Mobile Safari/537.36 AliApp(DingTalk/4.7.7) com.alibaba.android.rimet/11964676 Channel/700159 language/zh-CN");
                break;
            default:
                break;
        }
        // requestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.add("Connection", "keep-alive");
        requestHeaders.add("Cookie", elemeStarCookie.getCookie());
    }

    /**
     * 领取一次饿了么星选红包
     *
     * @return html
     */
    @Override
    public JSONObject getOne(Hb hb, Cookie cookie) {
        ElemeStarHb elemeStarHb = (ElemeStarHb) hb;
        ElemeStarCookie elemeStarCookie = (ElemeStarCookie) cookie;
        HttpHeaders requestHeaders = new HttpHeaders();
        //初始化requestHeaders
        requestInit(requestHeaders, null, elemeStarCookie, elemeStarCookie.getApp());
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange
                (elemeStarHb.getUrl(), HttpMethod.GET, requestEntity, String.class);
        String result = responseEntity.getBody();
        JSONObject jsonObject = resultInit(result);
        // 领取成功, cookie使用次数+1
        if (SUCCESS == getStatus(jsonObject)) {
            elemeStarCookie.use();
        }
        return jsonObject;
    }

    /**
     * 根据JSON获取领取状态
     *
     * @param jsonObject 领取结果的字符串
     * @return 领取状态码
     */
    @Override
    public int getStatus(JSONObject jsonObject) {
        return (int) jsonObject.get("status");
    }

    @Override
    public int getNowNumber(JSONObject jsonObject) {
        return jsonObject.getJSONArray("friends_info").size();
    }

    /**
     * 根据JSON获取领取金额
     *
     * @param jsonObject 领取结果
     * @return Amount 红包金额
     */
    @Override
    public int getAmount(JSONObject jsonObject) {
        if (jsonObject.containsKey("coupons")) {
            jsonObject = jsonObject.getJSONObject("coupons");
            return (int) jsonObject.get("amount");
        }
        return 0;
    }

    @Override
    public JSONArray getFriendsInfo(JSONObject jsonObject) {
        return jsonObject.getJSONArray("friends_info");
    }

    /**
     * 领取结果转jsonObject
     *
     * @param result 领取结果字符串
     * @return
     */
    @Override
    public JSONObject resultInit(String result) {
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(result);
            jsonObject = jsonObject.getJSONObject("result");
        } catch (Exception e) {
            logger.error("result转json出错 {} {}", e.toString(), result);
            return null;
        }
        jsonObject.remove("load");
        jsonObject.remove("token");
        // jsonObject.remove("share");
        jsonObject.remove("ext_coupons");
        return jsonObject;
    }

    /**
     * 组装星选红包
     *
     * @param requestBody
     * @return ElemeStarHb
     */
    @Override
    public ElemeStarHb hbInit(Map<String, String> requestBody) {
        if (requestBody == null) {
            return new ElemeStarHb(null, null, null);
        } else if (requestBody.containsKey("url")) {
            String url = requestBody.get("url");
            // 注意了！ 此处的caseid长度不是固定的(大概是订单总数之类的)，当前为10位数，懒得写位数变换的情况！
            String caseid = url.substring(url.indexOf("caseid=") + 7, url.indexOf("caseid=") + 17);
            String sign = url.substring(url.indexOf("sign=") + 5, url.indexOf("sign=") + 37);
            url = "https://star.ele.me/hongbao/wpshare?caseid=" + caseid + "&sign=" + sign + "&display=json";
            return new ElemeStarHb(url, caseid, sign);
        } else if (requestBody.containsKey("caseid") && requestBody.containsKey("sign")) {
            String url = "https://star.ele.me/hongbao/wpshare?caseid=" +
                    requestBody.get("caseid") + "&sign=" + requestBody.get("sign") + "&display=json";
            return new ElemeStarHb(url, requestBody.get("caseid"), requestBody.get("sign"));
        } else {
            return new ElemeStarHb(null, null, null);
        }
    }

    /**
     * 根据返回结果获取第几个是最大红包
     *
     * @param result 请求返回的html
     * @return lucky_number
     */
    public int getLuckyNumberFromResult(String result) {
        // 在字符串中匹配：【饿了么星选】第 (红包最大个数)
        Matcher matcher = regexUtils.getMatcher("【饿了么星选】第[0-9]{1,2}", result);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(8));
        } else {
            logger.error("饿了么星选红包最大个数获取失败:{}", matcher.group(0));
            return -1;
        }
    }

    /**
     * 根据返回结果获取领取状态 (旧版本、暂时废弃)
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
     * 根据返回结果获取领取金额 (旧版本、暂时废弃)
     *
     * @param html 请求返回的html
     * @return Amount 红包金额
     */
    public int getAmountFromResult(String html) {
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
     * 根据返回结果获取当前已领取红包个数 (旧版本、暂时废弃)
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
     * 根据返回结果获取当前红包领取的用户信息 (旧版本、暂时废弃)
     *
     * @param html 请求返回的html
     * @return 领取人信息
     */
    public JSONArray getFriendsInfoFromHtml(String html) {
        // 在字符串中匹配：friends_info (已领取人员信息)
        Matcher matcher = regexUtils.getMatcher("\"friends_info\":.*?]", html);
        if (matcher.find()) {
            try {
                JSONArray jsonArray = JSON.parseArray(matcher.group(0).substring(15));
                logger.debug(jsonArray.toJSONString());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject friendObject = jsonArray.getJSONObject(i);
                    friendObject.remove("headimg");
                    friendObject.remove("words");
                    friendObject.remove("is_luck");
                }
                return jsonArray;
            } catch (Exception e) {
                logger.error("星选红包领取的用户信息获取失败:{},{}", matcher.group(0), e);
                return null;
            }
        } else {
            logger.error("星选红包领取的用户信息获取失败:{}", matcher.group(0));
            return null;
        }
    }

    /**
     * 2019年11月21日10:51:02 注：饿了么星选改变返回值，此方法废弃
     * 领取结果转jsonObject
     *
     * @param result 领取结果字符串
     * @return
     *
     @Override public JSONObject resultInit(String result) {
     Matcher matcher = regexUtils.getMatcher("init\\(\\{\"error_no\".*?}\\);", result);
     if (matcher.find()) {
     JSONObject jsonObject;
     try {
     result = matcher.group(0).substring(5, matcher.group(0).length() - 2);
     jsonObject = JSON.parseObject(result);
     jsonObject = jsonObject.getJSONObject("result");
     } catch (Exception e) {
     logger.error("result转json出错 {} {}", e.toString(), result);
     return null;
     }
     jsonObject.remove("load");
     jsonObject.remove("token");
     jsonObject.remove("share");
     jsonObject.remove("ext_coupons");
     return jsonObject;
     }
     return null;
     }*/
}
