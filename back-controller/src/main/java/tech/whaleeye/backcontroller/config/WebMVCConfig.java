package tech.whaleeye.backcontroller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

import static tech.whaleeye.misc.constants.Values.FILE_UPLOAD_PATH;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:".concat((new File(FILE_UPLOAD_PATH)).toString()).concat(File.separator));
    }
}