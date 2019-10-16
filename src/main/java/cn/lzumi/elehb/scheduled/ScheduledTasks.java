package cn.lzumi.elehb.scheduled;

import cn.lzumi.elehb.controller.ElemeController;
import cn.lzumi.elehb.controller.ElemeStarController;
import cn.lzumi.elehb.mapper.ElemeStarMapper;
import cn.lzumi.elehb.utils.ElemeStarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    ElemeStarMapper elemeStarMapper;
    private int once = 1;
    private final Logger logger = LoggerFactory.getLogger(getClass());

//    @Scheduled(fixedDelay = 60000)
//    public void getElemeStarHb() {
//        if (once != 2) {
//            String result = elemeStarUtils.getOneByUtil("988078338", "1ae6facf43540c507a915eca6092e9c2");
//            int luckyNum = elemeStarUtils.getLuckyNumberFromHtml(result);
//            int nowNum = elemeStarUtils.getNowNumberFromHtml(result);
//            if (luckyNum - nowNum == 1) {
//                ElemeStarCookie elemeStarCookie = elemeStarMapper.getUserElemeStarCookie("izumi");
//                result = elemeStarUtils.getOne("988078338", "1ae6facf43540c507a915eca6092e9c2", elemeStarCookie);
//                logger.info(result);
//                once++;
//            } else if (luckyNum <= nowNum) {
//                logger.info("没领到");
//            }
//        }
//    }
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
