package cn.lzumi.elehb.bean;

import lombok.Data;

/**
 * @author izumi
 * @date 2019-08-29 15:34:38
 */
@Data
public class ElemeStarCookie {
    private int id;
    private String cookie;
    private int app;
    /**
     * 一天内该cookie使用次数
     */
    private int todayUse;
    /**
     * 总共领取了多少次
     */
    private int totalUse;
    /**
     * 用于判断是否失效,1为失效
     */
    private int isLose;
    /**
     * 是否是某一用户的cookie 0:不是 1:是
     */
    private int isUser;

    public void add() {
        todayUse++;
        totalUse++;
    }
}
