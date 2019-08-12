package cn.lzumi.elehb.scheduled;

import cn.lzumi.elehb.controller.ElemeController;
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

    ElemeController elemeController = new ElemeController();
    private int once = 0;

    @Scheduled(fixedDelay = 60000)
    public void getHB() {
        if (once == 0) {
            int luckyNumber = (int) elemeController.getLuckyNumber("1d5360653a0768a9.2");
            int nowNumber = (int) elemeController.getNowNumber("1d5360653a0768a9.2");
            if (luckyNumber - nowNumber == 1) {
                System.out.println(elemeController.getOneHb("1d5360653a0768a9.2"));
                once = 1;
            } else {
                System.out.println(nowNumber + "/" + luckyNumber);
            }
        }
    }
}
