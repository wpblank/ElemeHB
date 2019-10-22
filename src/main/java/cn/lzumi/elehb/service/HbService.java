package cn.lzumi.elehb.service;

import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author izumi
 */
public interface HbService {
    /**
     * 领取饿了么星选红包
     *
     * @param caseid
     * @param sign
     * @param name        需要领取红包的用户名
     * @param requestBody 包含红包的 url
     * @return
     */
    Map<String, Object>  getAllHb(String caseid, String sign, String name, MultiValueMap<String, String> requestBody);

    Map<String, Object> getAllHb(String sn, String name, MultiValueMap<String, String> requestBody);


    /**
     * 查询红包当前领取状态 (通过工具人小号)
     * @param caseid
     * @param sign
     * @param requestBody
     * @return
     */
    Map<String, Object> getHbNumber(String caseid, String sign, MultiValueMap<String, String> requestBody);
}
