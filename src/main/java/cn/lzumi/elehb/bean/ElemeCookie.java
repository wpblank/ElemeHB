package cn.lzumi.elehb.bean;

/**
 * @author izumi
 */
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWeixinAvatar() {
        return weixinAvatar;
    }

    public void setWeixinAvatar(String weixinAvatar) {
        this.weixinAvatar = weixinAvatar;
    }

    public String getWeixinUsername() {
        return weixinUsername;
    }

    public void setWeixinUsername(String weixinUsername) {
        this.weixinUsername = weixinUsername;
    }

    public int getTodayUse() {
        return todayUse;
    }

    public void setTodayUse(int todayUse) {
        this.todayUse = todayUse;
    }

    public int getTotalUse() {
        return totalUse;
    }

    public void setTotalUse(int totalUse) {
        this.totalUse = totalUse;
    }

    public int getLoseEfficacy() {
        return loseEfficacy;
    }

    public void setLoseEfficacy(int loseEfficacy) {
        this.loseEfficacy = loseEfficacy;
    }
}
