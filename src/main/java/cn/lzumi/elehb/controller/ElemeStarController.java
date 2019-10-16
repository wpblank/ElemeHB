package cn.lzumi.elehb.controller;

import cn.lzumi.elehb.service.ElemeStarService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author izumi
 * @date 2019/08/10
 */
@RestController
@RequestMapping(value = "/eleme_star")
public class ElemeStarController {
    @Autowired
    ElemeStarService elemeStarService;

    @Value("${cn.lzumi.elehb}")
    public String eleme;

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么星选红包领取", tags = {"饿了么星选"})
    public Object get() {
        return eleme;
    }

//    /**
//     * 领取一次红包
//     *
//     * @return 当前领取个数
//     */
//    @GetMapping("/get_one")
//    @ApiOperation(value = "领取一次红包", tags = {"饿了么星选"})
//    public Object getOneHb(String caseid, String sign,
//                           @RequestBody(required = false) MultiValueMap<String, String> requestBody) {
//        ElemeStarHb elemeStarHb = elemeStarUtils.elemeStarHbInit(caseid, sign, requestBody);
//        //初始化cookies
//        elemeStarCookies = elemeStarService.elemeStarCookiesInit(elemeStarCookies);
//        String result = elemeStarUtils.getOne(elemeStarHb, elemeStarCookies.get(0));
//        return result;
//    }

    /**
     * 领取红包到最大一个以前，如果name存在，则帮name领取最大红包
     *
     * @param caseid 红包caseid
     * @param sign   红包sign
     * @param name   用户名
     * @return
     */
    @GetMapping("/get_all")
    @ApiOperation(value = "领取红包", tags = {"饿了么星选"})
    public Map<String, Object> getAllHb(String caseid, String sign, String name,
                                        @RequestBody(required = false) MultiValueMap<String, String> requestBody) {
        return elemeStarService.getAllHb(caseid, sign, name, requestBody);
    }

    @GetMapping("/get_number")
    @ApiOperation(value = "查询红包当前领取数量", tags = {"饿了么星选"})
    public Map<String, Object> getHbNumber(String caseid, String sign,
                                           @RequestBody(required = false) MultiValueMap<String, String> requestBody) {
        return elemeStarService.getHbNumber(caseid, sign, requestBody);
    }
}
