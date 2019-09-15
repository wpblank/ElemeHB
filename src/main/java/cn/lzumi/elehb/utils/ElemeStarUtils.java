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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.awt.event.WindowFocusListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 初始化requestHeaders
     *
     * @param requestHeaders 请求头
     * @param cookie
     */
    public void requestInit(HttpHeaders requestHeaders, String cookie, int app) {
        requestHeaders.add("Accept", "*/*");
        if (app == 0) {
            requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 9; MIX 3 Build/PKQ1.180729.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/66.0.3359.126 MQQBrowser/6.2 TBS/044813 Mobile Safari/537.36 MMWEBID/8168 MicroMessenger/7.0.5.1440(0x27000534) Process/tools NetType/WIFI Language/zh_CN");
        } else if (app == 1) {
            requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; U; Android 9; zh-CN; MIX 3 Build/PKQ1.180729.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.108 UCBrowser/11.9.4.974 UWS/2.13.2.46 Mobile Safari/537.36 AliApp(DingTalk/4.7.7) com.alibaba.android.rimet/11964676 Channel/700159 language/zh-CN");
        }
        requestHeaders.add("Connection", "Keep-Alive");
        requestHeaders.add("Cookie", cookie);
    }

    /**
     * 领取一次饿了么星选红包
     *
     * @param caseid
     * @param sign
     * @param elemeStarCookie 饿了么星选cookie
     * @return html
     */
    public String getOne(String caseid, String sign, ElemeStarCookie elemeStarCookie) {
        String getElemeStarUrl = "https://star.ele.me/hongbao/wpshare?";
        caseid = "caseid=" + caseid + "&";
        sign = "sign=" + sign;
        String cookie = elemeStarCookie.cookie;
        HttpHeaders requestHeaders = new HttpHeaders();
        //初始化requestHeaders
        requestInit(requestHeaders, cookie, elemeStarCookie.app);
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange
                (getElemeStarUrl + caseid + sign, HttpMethod.GET, requestEntity, String.class);
        logger.debug(responseEntity.toString());
        return responseEntity.getBody();
    }

    /**
     * 领取饿了么星选红包
     *
     * @param caseid
     * @param sign
     * @param elemeStarCookies
     * @param userElemeStarCookie
     * @return
     */
    public String getAllHb(String caseid, String sign, List<ElemeStarCookie> elemeStarCookies, ElemeStarCookie userElemeStarCookie) {
        String result = getOne(caseid, sign, elemeStarCookies.get(0));
        int luckyNum = getLuckyNumberFromHtml(result);
        int nowNum = getNowNumberFromHtml(result);
        if (luckyNum - nowNum < 1) {
            logger.info("红包已被领取{}/{},caseid:{},sign:{}", nowNum, luckyNum, caseid, sign);
        } else if (luckyNum - nowNum > 1) {
            for (int i = 1; luckyNum - nowNum > 1 && i < elemeStarCookies.size(); i++) {
                nowNum = getNowNumberFromHtml(getOne(caseid, sign, elemeStarCookies.get(i)));
            }
        }
        if (luckyNum - nowNum == 1) {
            return getOne(caseid, sign, userElemeStarCookie);
        } else {
            return "红包领取失败:" + nowNum + "/" + luckyNum + ",caseid:" + caseid + ",sign:" + sign;
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
        String patternStr = "【饿了么星选】第[0-9]{1,2}";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).substring(8));
        } else {
            logger.error("饿了么星选红包最大个数获取失败:{}", matcher.group(0));
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
        String patternStr = "\"friends_info\":.*?]";
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(patternStr);
        // 现在创建 matcher 对象
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            JSONArray jsonArray = JSON.parseArray(matcher.group(0).substring(15));
            logger.info(jsonArray.toJSONString());
            return jsonArray.size();
        } else {
            logger.error("饿了么星选红包领取个数获取失败:{}", matcher.group(0));
            return -1;
        }
    }

    /**
     * 通过工具人小号，查询红包信息
     *
     * @param caseid
     * @param sign
     * @return
     */
    public String getOneByUtil(String caseid, String sign) {
        String getElemeStarUrl = "https://star.ele.me/hongbao/wpshare?";
        caseid = "caseid=" + caseid + "&";
        sign = "sign=" + sign;
        HttpHeaders requestHeaders = new HttpHeaders();
        //初始化requestHeaders
        requestInit(requestHeaders, utilElemeStarCookie, 0);
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange
                (getElemeStarUrl + caseid + sign, HttpMethod.GET, requestEntity, String.class);
        logger.debug(responseEntity.toString());
        return responseEntity.getBody();
    }

//    public ElemeStarHb elemeStarHbInit(String url) {
//        ElemeStarHb elemeStarHb = new ElemeStarHb();
//        elemeStarHb.url = url;
//    }

    /**
     * 初始化饿了么星选cookies
     * 如果cookies不存在或者数量过少，则向数据库请求获得新的cookies
     *
     * @param elemeStarCookies 星选cookie列表
     * @return
     */
    public List<ElemeStarCookie> elemeStarCookiesInit(List<ElemeStarCookie> elemeStarCookies) {
        if (elemeStarCookies == null || elemeStarCookies.size() < 5) {
            elemeStarCookies = elemeStarMapper.getElemeStarCookies(COOKIE_NUM);
            logger.info("获取新的星选cookies，数目为：" + elemeStarCookies.size());
            return elemeStarCookies;
        } else {
            return elemeStarCookies;
        }
    }
}
