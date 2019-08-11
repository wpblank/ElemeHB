package cn.lzumi.elehb.eleme.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author izumi
 * @date 2019/08/03
 */
@RestController
@RequestMapping(value = "/eleme")
public class ElemeController {

    @Value("${cn.lzumi.elehb}")
    private String elehb;

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
     * 不明白为什么返回 {"promotion_items":[],"promotion_records":[],"ret_code":12}
     */
    @GetMapping("/get_one/{sn}")
    @ApiOperation(value = "领取一次红包", tags = {"饿了么"})
    public Object getOneHb(@PathVariable(value = "sn") String sn) {
        String openId = "B92BCA958221F6FFEE01FCBE8AC955D7";
        String getElemeUrl = "https://h5.ele.me/restapi/marketing/v2/promotion/weixin/";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestHeaders.add("Accept", "*/*");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Linux; Android 5.1; m1 metal Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043409 Safari/537.36 V1ANDSQ7.2.5744YYBD QQ/7.2.5.3305 NetType/WIFI WebP/0.3.0 Pixel/1080");
        requestHeaders.add("Accept-Encoding", "gzip, deflate, br");
        requestHeaders.add("Accept-Language", "zh-CN,zh-HK;q=0.9,zh;q=0.8,en-US;q=0.7,en;q=0.6,zh-TW;q=0.5");
        requestHeaders.add("Connection", "Keep-Alive");
        requestHeaders.add("Content-Type", "text/plain;charset=UTF-8");
        requestHeaders.add("Cookie", "SID=ZdKkACRNv6waReXjFue6z8PkFgUWAmOGDAmg");
        requestHeaders.add("Cache-Control", "no-cache");
        requestHeaders.add("Host", "h5.ele.me");
        requestHeaders.add("content-length", "65");
        requestHeaders.add("X-Shard", "eosid=3047066502041562000;loc=116.3440686,40.0046365");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("sign", "61dcd338f5e0f7eb520788b262cb4ea");
        requestBody.add("group_sn", "2a4935b948076082.2");

        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(getElemeUrl + openId, HttpMethod.POST, requestEntity, String.class);
        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        System.out.println(responseEntity);
        System.out.println(responseEntity.getHeaders());
        return jsonObject.toJSONString();
    }
}