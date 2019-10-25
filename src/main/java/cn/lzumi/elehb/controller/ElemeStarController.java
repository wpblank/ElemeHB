package cn.lzumi.elehb.controller;

import cn.lzumi.elehb.service.impl.ElemeStarServiceImpl;
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
    ElemeStarServiceImpl elemeStarService;

    @Value("${cn.lzumi.elehb}")
    public String eleme;

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么星选红包领取", tags = {"饿了么星选"})
    public Object get() {
        return eleme;
    }

    /**
     * 领取一次红包
     *
     * @return 领取结果
     */
    @PostMapping("/get_one")
    @ApiOperation(value = "领取一次红包", tags = {"饿了么星选"})
    public Object getOneHb(@RequestBody(required = false) Map<String, String> requestBody) {
        return elemeStarService.getOneByUtil(requestBody);
    }

    /**
     * 领取红包到最大一个以前，如果name存在，则帮name领取最大红包
     *
     * @param name 用户名
     * @return
     */
    @PostMapping("/get_all")
    @ApiOperation(value = "领取红包", tags = {"饿了么星选"})
    public Map<String, Object> getAllHb(String name,
                                        @RequestBody(required = false) Map<String, String> requestBody) {
        return elemeStarService.getAllHb(name, requestBody);
    }

    @PostMapping("/get_number")
    @ApiOperation(value = "查询红包当前领取状态", tags = {"饿了么星选"})
    public Map<String, Object> getHbNumber(@RequestBody(required = false) Map<String, String> requestBody) {
        return elemeStarService.getHbNumber(requestBody);
    }
}
