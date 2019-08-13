package cn.lzumi.elehb.bean;

public class ElemeHb {
    private int id;
    private String url;
    private String sn;
    private int isGet;
    private int maxNum;
    private int nowNum;
    private String getTime;

    public ElemeHb(String url, String sn, int isGet, int maxNum, int nowNum) {
        this.url = url;
        this.sn = sn;
        this.isGet = isGet;
        this.maxNum = maxNum;
        this.nowNum = nowNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getIsGet() {
        return isGet;
    }

    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getNowNum() {
        return nowNum;
    }

    public void setNowNum(int nowNum) {
        this.nowNum = nowNum;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }
}
