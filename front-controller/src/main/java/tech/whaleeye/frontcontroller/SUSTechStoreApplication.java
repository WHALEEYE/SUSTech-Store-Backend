package tech.whaleeye.frontcontroller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(value = "tech.whaleeye", excludeFilters =
        {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "tech.whaleeye.backcontroller.*")
        })
@MapperScan(value = "tech.whaleeye.mapper")
public class SUSTechStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SUSTechStoreApplication.class, args);
    }

}
