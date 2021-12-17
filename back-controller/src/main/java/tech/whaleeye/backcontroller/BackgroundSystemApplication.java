package tech.whaleeye.backcontroller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(value = "tech.whaleeye", excludeFilters =
        {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "tech.whaleeye.frontcontroller.*")
        })
@MapperScan(value = "tech.whaleeye.mapper")
public class BackgroundSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackgroundSystemApplication.class, args);
    }
}
