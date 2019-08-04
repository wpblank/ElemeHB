package cn.lzumi.elehb.eleme.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import cn.lzumi.elehb.eleme.config.RestConfiguration;

/**
 * @author izumi
 * @date 2019/08/03日
 */
@RestController
@RequestMapping(value = "/eleme")
public class ElemeController {
    private String url = "http://140.143.155.68:8080/customer/";

    @GetMapping("/")
    @ApiOperation(value = "欢迎使用饿了么红包领取", tags = {"饿了么"})
    public String get(){
        return "hello eleme";
    }

    @GetMapping("/{company}")
    @ApiOperation(value = "欢迎使用饿了么红包领取", tags = {"饿了么"})
    public Object getCompany(@PathVariable(value = "company") int company){
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String strbody=restTemplate.exchange(url + company, HttpMethod.GET, entity,String.class).getBody();
        return strbody;
    }
}