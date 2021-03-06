package cn.lzumi.elehb.service;

import cn.lzumi.elehb.domain.Cookie;
import cn.lzumi.elehb.domain.Hb;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author izumi
 */
public interface HbService {

    Object get() throws InterruptedException;

    /**
     * 领取饿了么星选红包
     *
     * @param name        需要领取红包的用户名
     * @param requestBody 包含红包的 url
     * @return
     */
    Map<String, Object> getAllHb(String name, Map<String, String> requestBody);

    /**
     * 查询红包当前领取状态 (通过工具人小号)
     *
     * @param requestBody
     * @return
     */
    Map<String, Object> getHbNumber(Map<String, String> requestBody);

    /**
     * 领取一次，并查询红包信息 （当requestBody内未指定cookie时 使用配置文件中的cookie）
     */
    JSONObject getOne(Map<String, String> requestBody);

    /**
     * 初始化cookies
     * 如果cookies不存在或者数量过少，则向数据库请求获得新的cookies
     * 同时将旧cookie的使用次数更新至数据库
     */
    void cookiesInit();
}
