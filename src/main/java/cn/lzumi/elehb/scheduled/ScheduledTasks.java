package cn.lzumi.elehb.scheduled;

import cn.lzumi.elehb.bean.ElemeHb;
import cn.lzumi.elehb.controller.ElemeController;
import cn.lzumi.elehb.mapper.ElemeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    ElemeMapper elemeMapper;

    private ElemeController elemeController = new ElemeController();
    private String sn = "2a4bedf48a2a0c83.2";//elemeMapper.getElemeHb();
    private int once = 0;

    @Scheduled(fixedDelay = 60000)
    public void getHb() {
        if (once == 0) {
            int luckyNumber = (int) elemeController.getLuckyNumber(sn);
            int nowNumber = (int) elemeController.getNowNumber(sn);
            if (luckyNumber - nowNumber == 1) {
                System.out.println(elemeController.getOneHb(sn,"izumi"));
                once = 1;
            } else {
                System.out.println(nowNumber + "/" + luckyNumber);
            }
        }
    }
}
