package cn.lzumi.elehb.service;

import cn.lzumi.elehb.domain.Hb;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author izumi
 */
public interface HbService {
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
     * 初始化cookies
     * 如果cookies不存在或者数量过少，则向数据库请求获得新的cookies
     * 同时将旧cookie的使用次数更新至数据库
     */
    void cookiesInit();

    /**
     * 通过工具人小号，查询红包信息
     *
     * @param hb 红包对象
     * @return 领取情况
     */
    String getOneByUtil(Hb hb);
}
