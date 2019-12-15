package cn.lzumi.elehb.service.impl;

import cn.lzumi.elehb.domain.ElemeStarCookie;
import cn.lzumi.elehb.domain.ElemeStarHb;
import cn.lzumi.elehb.domain.Hb;
import cn.lzumi.elehb.mapper.ElemeStarMapper;
import cn.lzumi.elehb.service.HbService;
import cn.lzumi.elehb.utils.impl.ElemeStarUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static cn.lzumi.elehb.utils.ResponseUtils.*;

/**
 * @author izumi
 */
@Service
public class ElemeStarServiceImpl implements HbService {
    @Autowired
    ElemeStarMapper elemeStarMapper;

    @Value("${cn.lzumi.utilElemeStarCookie}")
    public String utilElemeStarCookie;
    @Value("${cn.lzumi.utilElemeStarApp}")
    public int utilElemeStarApp;
    @Value("${cn.lzumi.elehb}")
    public String eleme;

    private final int COOKIE_NUM = 10;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ElemeStarUtils esUtils = new ElemeStarUtils();

    public List<ElemeStarCookie> elemeStarCookies = new ArrayList<>();

    @Override
    public Object get() throws InterruptedException {
        logger.info("开始任务");
        Thread.sleep(5000);
        logger.info("结束任务");
        return eleme;
    }

    /**
     * 领取饿了么星选红包
     *
     * @param name        需要领取红包的用户名
     * @param requestBody 包含红包的 url
     * @return
     */
    @Override
    public Map<String, Object> getAllHb(String name, Map<String, String> requestBody) {
        ElemeStarHb elemeStarHb = esUtils.hbInit(requestBody);
        //初始化cookies
        cookiesInit();
        ElemeStarCookie userElemeStarCookie = null;
        if (name != null) {
            userElemeStarCookie = elemeStarMapper.getUserElemeStarCookie(name);
        }
        JSONObject jsonResult = getOneByUtil(elemeStarHb);
        if (esUtils.getStatus(jsonResult) == OVERDUE) {
            return myResponse("红包已过期", HB_OVERDUE);
        }
        int luckyNum = esUtils.getLuckyNumberFromResult(jsonResult.get("share").toString());
        int nowNum = esUtils.getNowNumber(jsonResult);
        if (luckyNum - nowNum < 1) {
            logger.debug("红包已被领取{}/{},{}", nowNum, luckyNum, elemeStarHb.getUrl());
            return myResponse("红包已被领取:" + nowNum + "/" + luckyNum + ","
                    + elemeStarHb.getUrl(), HB_RECEIVED, esUtils.getFriendsInfo(jsonResult));
        } else if (luckyNum - nowNum > 1) {
            for (int i = 0; luckyNum - nowNum > 1 && i < elemeStarCookies.size(); i++) {
                jsonResult = esUtils.getOne(elemeStarHb, elemeStarCookies.get(i));
                if (jsonResult == null) {
                    continue;
                }
                if (jsonResult.containsKey("friends_info")) {
                    nowNum = esUtils.getNowNumber(jsonResult);
                }
            }
        }
        // 判断是否领取完毕
        if (luckyNum - nowNum == 1) {
            if (userElemeStarCookie == null) {
                return myResponse("红包已领取到最大前一个:" + nowNum + "/" + luckyNum + ","
                        + elemeStarHb.getUrl(), GET_SUCCESS, esUtils.getFriendsInfo(jsonResult));
            }
            // 帮助用户领取最大的红包
            else {
                JSONObject userJsonResult = esUtils.getOne(elemeStarHb, userElemeStarCookie);
                switch (esUtils.getStatus(userJsonResult)) {
                    case SUCCESS:
                        return myResponse("领取成功,红包金额:" + esUtils.getAmount(userJsonResult) + "元",
                                GET_SUCCESS, esUtils.getFriendsInfo(userJsonResult));
                    case RECEIVED:
                        return myResponse("你已经领取过该红包" + nowNum + "/" + luckyNum + "," + elemeStarHb.getUrl(),
                                USER_RECEIVED, esUtils.getFriendsInfo(userJsonResult));
                    default:
                        logger.error("未知红包状态码:{},{}", esUtils.getStatus(userJsonResult), userJsonResult);
                        return myResponse("红包已领取到最大前一个:" + nowNum + "/" + luckyNum + ","
                                + elemeStarHb.getUrl(), FAIL_TO_RECEIVE, esUtils.getFriendsInfo(userJsonResult));
                }
            }
        } else {
            // 未能成功领取
            return myResponse("红包领取失败:" + nowNum + "/" + luckyNum + ","
                    + elemeStarHb.getUrl(), GET_PARTIAL, esUtils.getFriendsInfo(jsonResult));
        }
    }

    @Override
    public Map<String, Object> getHbNumber(Map<String, String> requestBody) {
        ElemeStarHb elemeStarHb = esUtils.hbInit(requestBody);
        JSONObject jsonResult = getOneByUtil(elemeStarHb);
        if (esUtils.getStatus(jsonResult) == OVERDUE) {
            return myResponse("红包已过期", HB_OVERDUE);
        }
        int luckyNum = esUtils.getLuckyNumberFromResult(jsonResult.get("share").toString());
        int nowNum = esUtils.getNowNumber(jsonResult);
        int code;
        if (luckyNum - nowNum == 1) {
            code = GET_SUCCESS;
        } else if (luckyNum > nowNum) {
            code = GET_PARTIAL;
        } else {
            code = HB_RECEIVED;
        }
        return myResponse("红包领取状态:" + nowNum + "/" + luckyNum, code, esUtils.getFriendsInfo(jsonResult));
    }


    /**
     * 初始化饿了么星选cookies
     * 如果cookies不存在或者数量过少，则向数据库请求获得新的cookies
     * 同时将旧cookie的使用次数更新至数据库
     */
    @Override
    public void cookiesInit() {
        if (elemeStarCookies.size() == 0) {
            elemeStarCookies = elemeStarMapper.getElemeStarCookies(COOKIE_NUM);
            logger.info("获取新的星选cookies，数目为：" + elemeStarCookies.size());
        } else if (elemeStarCookies.size() < 5) {
            logger.info("更新星选信息条数:{}", elemeStarMapper.updateElemeStarCookieUseInfo(elemeStarCookies));
            elemeStarCookies = elemeStarMapper.getElemeStarCookies(COOKIE_NUM);
            logger.info("获取新的星选cookies，数目为：" + elemeStarCookies.size());
        }
    }

    /**
     * 通过工具人小号，查询红包信息
     */
    @Override
    public JSONObject getOneByUtil(Hb hb) {
        ElemeStarHb elemeStarHb = (ElemeStarHb) hb;
        ElemeStarCookie elemeStarCookie = new ElemeStarCookie();
        elemeStarCookie.setCookie(utilElemeStarCookie);
        elemeStarCookie.setApp(utilElemeStarApp);
        return esUtils.getOne(elemeStarHb, elemeStarCookie);
    }

    public JSONObject getOneByUtil(Map<String, String> requestBody) {
        ElemeStarHb elemeStarHb = esUtils.hbInit(requestBody);
        return getOneByUtil(elemeStarHb);
    }

}
