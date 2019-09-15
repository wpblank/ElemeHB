package cn.lzumi.elehb.bean;

/**
 * @author pyrde
 */
public class ElemeStarHb {
    public int id;
    public String url;
    public String caseid;
    public String sign;

    public ElemeStarHb(String url, String caseid, String sign) {
        this.url = url;
        this.caseid = caseid;
        this.sign = sign;
    }
}
