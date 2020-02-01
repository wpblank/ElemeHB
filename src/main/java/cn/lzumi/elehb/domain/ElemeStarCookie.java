package cn.lzumi.elehb.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author izumi
 * @date 2019-08-29 15:34:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElemeStarCookie extends Cookie{
    private String cookie;

    public ElemeStarCookie(String cookie,int app){
        this.cookie = cookie;
        this.app = app;
    }
}
