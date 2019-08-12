package cn.lzumi.elehb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@MapperScan("cn.lzumi.elehb.mapper")
public class ElehbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElehbApplication.class, args);
    }
}
