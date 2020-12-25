
package top.aruoxi.webmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("top.aruoxi.webmall.dao")
@SpringBootApplication
public class WebMallAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMallAPIApplication.class, args);
    }

}
