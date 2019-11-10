package cn.lzumi.elehb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author izumi
 */
@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@EnableAsync(proxyTargetClass=true)
@MapperScan("cn.lzumi.elehb.mapper")
public class ElehbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElehbApplication.class, args);
    }
}
