package com.dknapik.flowershop;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.math.BigDecimal;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {



    @Bean(name = "ModelMapper")
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        return mapper;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET, PUT, POST, DELETE, OPTIONS")
                .allowedOrigins("*")
                .allowCredentials(true);

    }
    // TODO: All attempts to register Zalando MoneyModule for automatic Money objects serialization/deserialization failed
    // Current workaround will be to map Money objects to dto by hand.
    // Currently not resolved problem is Spring object to request mapping (is is possible to register MoneyModule
    // in ObjectMapper bean, but requests will still be mapped as if this module wasn't registered.
/*
    @Primary
    @Bean
    public MoneyModule moneyModule() {
        return new MoneyModule();
    }

    @Primary
    @Bean
    public MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper().registerModule(new MoneyModule()));
        return converter;
    }

    @Primary
    @Bean
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        createMappingJacksonHttpMessageConverter().getObjectMapper().registerModule(new MoneyModule());
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        return restTemplate;
    }*/
}
