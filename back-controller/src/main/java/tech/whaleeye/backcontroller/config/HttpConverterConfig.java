package tech.whaleeye.backcontroller.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class HttpConverterConfig {
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {

        // Create an object of FastJsonHttpMessageConverter to convert message
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

        // Create a config object to modify configs
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                // Keep keys with null values in json
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat,
                // Format the json
                SerializerFeature.PrettyFormat);

        // Add the config into Converter
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        // Set default charset as UTF8
        fastJsonHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);

        // Handle Chinese garbled code
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypeList);

        // Send the Modified Converter to HttpMessageConverters Object and return
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}
