package cn.lzumi.elehb.utils;

import cn.lzumi.elehb.domain.Cookie;
import cn.lzumi.elehb.domain.Hb;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author izumi
 */
public interface HbUtils {

    /**
     * 初始化requestHeaders
     *
     * @param requestHeaders 请求头
     * @param requestBody    请求体
     * @param cookie         待领取红包的cookie
     * @param app            哪个app的cookie
     */
    void requestInit(HttpHeaders requestHeaders,
                     MultiValueMap<String, String> requestBody, Cookie cookie, int app);

    /**
     * 领取一次红包
     *
     * @param hb     红包
     * @param cookie 待领取cookie
     * @return 返回领取结果的字符串（可能是json或者html）
     */
    JSONObject getOne(Hb hb, Cookie cookie);

    /**
     * 根据JSON获取领取状态
     *
     * @param jsonObject 领取结果
     * @return 领取状态码
     */
    int getStatus(JSONObject jsonObject);

    /**
     * 根据JSON获取当前领取人数
     * @param jsonObject
     * @return
     */
    int getNowNumber(JSONObject jsonObject);

    /**
     * 根据JSON获取领取金额
     *
     * @param jsonObject 领取结果
     * @return Amount 红包金额
     */
    int getAmount(JSONObject jsonObject);

    /**
     * 根据返回结果获取当前红包领取的用户信息
     * @param jsonObject
     * @return
     */
    JSONArray getFriendsInfo(JSONObject jsonObject);

    /**
     * 领取结果转JSONObject
     *
     * @param result
     * @return
     */
    JSONObject resultInit(String result);

    /**
     * 组装红包
     *
     * @param requestBody 红包参数
     * @return
     */
    Hb hbInit(Map<String, String> requestBody);
}
