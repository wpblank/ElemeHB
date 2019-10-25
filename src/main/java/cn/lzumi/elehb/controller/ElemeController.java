package cn.lzumi.elehb.controller;

import cn.lzumi.elehb.domain.ElemeHb;
import cn.lzumi.elehb.mapper.ElemeMapper;
import cn.lzumi.elehb.domain.ElemeCookie;
import cn.lzumi.elehb.utils.impl.ElemeUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private ElemeUtils elemeUtils;

    private RestTemplate restTemplate = new RestTemplate();
    private List<ElemeCookie> elemeCookies;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String elemeNumUrl = "https://h5.ele.me/restapi/marketing/themes/1/group_sns/";
    private String getElemeUrl = "https://h5.ele.me/restapi/marketing/v2/promotion/weixin/";

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么红包领取", tags = {"饿了么"})
    public Object get() {
        return elehb;
    }

    @GetMapping("/lucky_number/{sn}")
    @ApiOperation(value = "获取第几个红包是大红包", tags = {"饿了么"})
    public Object getLuckyNumber(@PathVariable(value = "sn") String sn) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String strBody = restTemplate.exchange(elemeNumUrl + sn, HttpMethod.GET, entity, String.class).getBody();
        JSONObject jsonObject = JSON.parseObject(strBody);
        if (jsonObject.get("lucky_number") == null) {
            return 404;
        }
        return jsonObject.get("lucky_number");
    }

    /**
     * 指定用户名领取一次红包
     *
     * @param sn   红包的sn
     * @param name 用户名
     * @return 领取结果字符串
     */
    @PostMapping("/get_one")
    @ApiOperation(value = "领取一次红包", tags = {"饿了么"})
    public Object getOneHb(String sn, String name) {
        ElemeCookie elemeCookie = elemeMapper.getElemeCookiesByName(name);
        if (elemeCookie == null) {
            return "未查到对应用户";
        }
        JSONObject jsonObject = elemeUtils.getOne(sn, elemeCookie);
        return jsonObject.toJSONString();
    }

    /**
     * 自动帮用户领取红包，如没传用户名参数，则领取到最大前一个
     *
     * @param sn   红包sn
     * @param name 用户名
     * @return
     */
    @PostMapping("/get_all")
    @ApiOperation(value = "领取红包", tags = {"饿了么"})
    public Object getAllHb(String sn, String name) {
        //初始化cookies
        elemeCookies = elemeUtils.elemeCookiesInit(elemeCookies);
        int luckyNumber = (int) getLuckyNumber(sn);
        int nowNumber = (int) getNowNumber(sn);
        if (luckyNumber == 404) {
            logger.info("红包{}不存在", sn);
            return "红包链接不存在";
        }else if(nowNumber == 500){
            return "服务器出错，请联系管理员解决";
        }
        logger.debug("红包{}的领取状况：{}/{}", sn, nowNumber, luckyNumber);
        //循环领取红包、直到最大红包前一个
        for (int i = 0; luckyNumber > nowNumber + 1 && i < elemeCookies.size(); i++) {
            //cookie的每天使用次数为5次
            if (elemeCookies.get(i).getTodayUse() < 5) {
                JSONObject jsonObject = elemeUtils.getOne(sn, elemeCookies.get(i));
                logger.debug((jsonObject.toJSONString()));
                switch (jsonObject.getInteger("ret_code")) {
                    case 2:
                        logger.info("该cookie领取过此红包,id={}", elemeCookies.get(i).getId());
                        break;
                    case 4:
                        elemeCookies.get(i).setTodayUse(elemeCookies.get(i).getTodayUse() + 1);
                        elemeCookies.get(i).setTotalUse(elemeCookies.get(i).getTotalUse() + 1);
                        nowNumber = jsonObject.getJSONArray("promotion_records").size();
                        logger.debug("红包{}的领取状况：{}/{}", sn, nowNumber, luckyNumber);
                        break;
                    default:
                        break;
                }
            }
        }
        if (name == null) {
            String s = "红包" + sn + "的领取状况：" + nowNumber + "/" + luckyNumber;
            logger.info(s);
            return s;
        } else {
            //直接领最大红包可能会导致金额过小、慎用。
            return getOneHb(sn, name);
        }
    }

    /**
     *
     */
    @GetMapping("/now_number/{sn}")
    @ApiOperation(value = "查询红包当前领取数量", tags = {"饿了么"})
    public Object getNowNumber(@PathVariable(value = "sn") String sn) {
        String openId = "A2DFA518682C27A84402AC8A1F7A4E06";
        HttpHeaders requestHeaders = new HttpHeaders();
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        //指定工具人小号
        elemeUtils.requestInit(requestHeaders, requestBody,
                "eFYsvCamsdNkx22MqVQbG3Y5m8BbxYeaIhqA", "cd5dc635f37194eb3b8f59110653311f", sn);
        try {
            //添加hearer和body，发送post请求
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(getElemeUrl + openId, HttpMethod.POST,
                            new HttpEntity<>(requestBody, requestHeaders), String.class);
            JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
            logger.info(jsonObject.toJSONString());
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("promotion_records").toString());
            return jsonArray.size();
        } catch (Exception e) {
            // 401 Unauthorized 是手机号失效, 400是qq失效(正常情况不会出现)
            logger.error("工具人小号出现异常，请尽快处理：{}", e.toString());
            return 500;
        }
    }

    /**
     *
     */
    @GetMapping("/add_hongbao/{url}")
    @ApiOperation(value = "添加一个红包链接", tags = {"饿了么"})
    public Object addHb(@PathVariable(value = "url") String url) {
        String sn = elemeUtils.getSnByUrl(url);
        int maxNum = (int) getLuckyNumber(sn);
        int nowNum = (int) getNowNumber(sn);
        ElemeHb elemeHb = new ElemeHb(url, sn, maxNum > nowNum ? 0 : 1, maxNum, nowNum);
        if (elemeMapper.addElemeHb(elemeHb) == 1) {
            return "红包添加成功，sn=" + sn;
        } else {
            return "红包添加失败:" + elemeHb.toString();
        }
    }

    @GetMapping("/get_hongbao/{num}")
    @ApiOperation(value = "获取红包链接列表", tags = {"饿了么"})
    public List<ElemeHb> getHb(@PathVariable(value = "num") int num) {
        List<ElemeHb> elemeHbList = elemeMapper.getElemeHb(num);
        return elemeHbList;
    }

    @GetMapping("/get_cookie")
    public Object getCookie() {
        List<ElemeCookie> elemeCookies = elemeMapper.getElemeCookies(elemeUtils.COOKIE_NUM);
        for (ElemeCookie elemeCookie : elemeCookies) {
            logger.info(elemeCookie.getOpenId());
        }
        return elemeCookies.get(0).getWeixinAvatar();
    }
}