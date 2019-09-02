package cn.lzumi.elehb.scheduled;

import cn.lzumi.elehb.bean.ElemeCookie;
import cn.lzumi.elehb.bean.ElemeHb;
import cn.lzumi.elehb.bean.ElemeStarCookie;
import cn.lzumi.elehb.controller.ElemeController;
import cn.lzumi.elehb.controller.ElemeStarController;
import cn.lzumi.elehb.mapper.ElemeMapper;
import cn.lzumi.elehb.utils.ElemeStarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时监控数据库中红包是否到达可以直接领取的状态
 *
 * @author izumi
 * @date 2019年08月12日18:00:14
 */
@Component
public class ScheduledTasks {

    @Autowired
    ElemeController elemeController;
    @Autowired
    ElemeStarController elemeStarController;
    @Autowired
    ElemeStarUtils elemeStarUtils;
    private int once = 1;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ElemeStarCookie elemeStarCookie = new ElemeStarCookie();

    @Scheduled(fixedDelay = 60000)
    public void getElemeStarHb() {
        if (once != 3) {
            elemeStarCookie.cookie = "WMID=7d618e17882db95b74c92e3add15b306; whid=cjFHN2p6UFNFZkZUcGtldGxyS2I0UnZCSllpTzZRTWFBdVh4RGNJeU5MOW93OFYyM2RXMFVDYnpCcGVuWXhRVGxmTURCcFZtUmFRbnBTV2s1MGVuRkJSblJZVFE9PQ%3D%3D; WMST=1567421042";
            String result = elemeStarUtils.getOne("988078338", "1ae6facf43540c507a915eca6092e9c2", elemeStarCookie);
            int luckyNum = elemeStarUtils.getMaxNumberFromHtml(result);
            int nowNum = elemeStarUtils.getNowNumberFromHtml(result);
            if (luckyNum - nowNum == 1) {
                elemeStarCookie.cookie = "";
                result = elemeStarUtils.getOne("988078338", "1ae6facf43540c507a915eca6092e9c2", elemeStarCookie);
                logger.info(result);
                once++;
            } else if (luckyNum <= nowNum) {
                logger.info("没领到");
            }

            elemeStarCookie.cookie = "WMID=7d618e17882db95b74c92e3add15b306; whid=cjFHN2p6UFNFZkZUcGtldGxyS2I0UnZCSllpTzZRTWFBdVh4RGNJeU5MOW93OFYyM2RXMFVDYnpCcGVuWXhRVGxmTURCcFZtUmFRbnBTV2s1MGVuRkJSblJZVFE9PQ%3D%3D; WMST=1567421042";
            result = elemeStarUtils.getOne("988243043", "00488e9e61f20694a9fb28164182a847", elemeStarCookie);
            luckyNum = elemeStarUtils.getMaxNumberFromHtml(result);
            nowNum = elemeStarUtils.getNowNumberFromHtml(result);
            if (luckyNum - nowNum == 1) {
                elemeStarCookie.cookie = "";
                result = elemeStarUtils.getOne("988243043", "00488e9e61f20694a9fb28164182a847", elemeStarCookie);
                logger.info(result);
                once++;
            } else if (luckyNum <= nowNum) {
                logger.info("没领到");
            }
        }
    }
//
//    @Scheduled(fixedDelay = 86400000)
//    public void initHb() {
//        once = 0;
//    }
//
//    @Scheduled(fixedDelay = 60000)
//    public void getHb() {
//        if (once == 0) {
//            int luckyNumber = (int) elemeController.getLuckyNumber(elemeHbList.get(1).getSn());
//            int nowNumber = (int) elemeController.getNowNumber(elemeHbList.get(1).getSn());
//            if (luckyNumber - nowNumber == 1) {
//                System.out.println(elemeController.getOneHb(elemeHbList.get(1).getSn(),"izumi"));
//                once = 1;
//            } else {
//                System.out.println(nowNumber + "/" + luckyNumber);
//            }
//        }
//    }
}
