package cn.lzumi.elehb.domain;

import lombok.Data;

/**
 * @author izumi
 */
@Data
public class Cookie {
    public int id;
    /**
     * 一天内该cookie使用次数
     */
    public int todayUse;
    /**
     * 总共领取了多少次
     */
    public int totalUse;
    /**
     * 用于判断是否失效,1为失效
     */
    public int isLose;
    /**
     * 是否是某一用户的cookie 0:不是 1:是
     */
    public int isUser;

    public int app;

    public void add() {
        todayUse++;
        totalUse++;
    }
}
