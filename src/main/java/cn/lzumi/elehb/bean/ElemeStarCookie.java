package cn.lzumi.elehb.bean;

/**
 * @author izumi
 * @date 2019-08-29 15:34:38
 */
public class ElemeStarCookie {
    private int id;
    public String cookie;
    /**
     * 一天内该cookie使用次数
     */
    public int todayUse;
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