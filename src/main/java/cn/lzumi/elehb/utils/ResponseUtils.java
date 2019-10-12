package cn.lzumi.elehb.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author izumi
 */
public class ResponseUtils {
    /**
     * 成功领取
     */
    public static final int SUCCESS = 2;
    /**
     * 红包过期
     */
    public static final int OVERDUE = 4;
    /**
     * 该cookie已领过此红包
     */
    public static final int RECEIVED = 5;
    /**
     * 成功领取
     */
    public static final int GET_SUCCESS = 200;
    /**
     * 最大红包已被领取
     */
    public static final int HB_RECEIVED = 201;
    /**
     * 已领取至最大前一个，主号领取失败
     */
    public static final int FAIL_TO_RECEIVE = 202;
    /**
     * 红包过期
     */
    public static final int HB_OVERDUE = 204;
    /**
     * 已领取至最大前一个，但主号已领取
     */
    public static final int USER_RECEIVED = 205;
    /**
     * 红包待继续领取
     */
    public static final int GET_PARTIAL = 206;

    public static Map<String, Object> myResponse(Object body, int status) {
        Map<String, Object> responseMap = new HashMap<>(4);
        responseMap.put("status", status);
        responseMap.put("message", body);
        return responseMap;
    }
}
