package cn.lzumi.elehb.bean;

import lombok.Data;

/**
 * @author izumi
 */
@Data
public class ElemeCookie {
    private int id;
    private String openId;
    private String sid;
    private String sign;
    private String weixinAvatar;
    private String weixinUsername;
    /**
     * 一天内该cookie使用次数
     */
    private int todayUse;
    /**
     * 总共领取了多少次
     */
    private int totalUse;
    /**
     * 领取失败次数，用于判断是否失效
     */
    private int loseEfficacy;
    /**
     * 是否是某一用户的cookie 0:不是 1:是
     */
    private int isUser;
}
